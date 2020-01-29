#include "SearchEngine.h"

#include "Evaluator.h"
#include "exceptions.h"
#include "MovesCollection.h"
#include "Player.h"
#include "PositionHash.h"
#include "Random.h"
#include "require.h"
#include "ZobristHasher.h"

#include <limits>
#include <unordered_set>

namespace chesslib {

namespace {

position_hash_t getRandomHashMask(const evaluation_t evaluationRandomness)
{
    return evaluationRandomness == 0 ? 0 : (static_cast<position_hash_t>(evaluationRandomness) << 1) - 1;
}

}

SearchEngine::SearchEngine(Position position, const Player& player)
    : position_(position)
    , evaluator_(&player.getEvaluator())
    , maxEvaluations_(player.getMaxEvaluations())
    , searchDepthPly_(0)
    , randomHash_(Random{}.getNextU64())
    , randomHashMask_(getRandomHashMask(player.getEvaluationRandomness()))
    , evaluationRandomness_(player.getEvaluationRandomness())
    , isSearchAborted_(false)
    , evaluatedPositionCount_(0)
{
}

Variation SearchEngine::runSearch(int depthPly)
{
    REQUIRE(depthPly > 0 && depthPly < MovesCollection::maxCapacity());

    searchDepthPly_ = depthPly;

    std::array<EvaluationFactors, PLAYER_COUNT> evaluationFactors = {
        evaluator_->getEvaluationFactors(position_, Black),
        evaluator_->getEvaluationFactors(position_, White)
    };

    constexpr evaluation_t beta = std::numeric_limits<evaluation_t>::max();
    constexpr evaluation_t alpha = -beta;
    const PositionHash positionHash = ZobristHasher::getInstance().getValue(position_);
    isSearchAborted_ = false;

    const evaluation_t searchResult = runAlphaBetaSearch(
        evaluationFactors,
        positionHash,
        depthPly,
        alpha,
        beta);

    if (isSearchAborted_) {
        return Variation();
    }

    const evaluation_t sideMultiplier = position_.getPlayerToMove() == Black ? -1 : 1;
    const evaluation_t evaluation = sideMultiplier * searchResult;

    return Variation(evaluation, getPrincipalVariation(positionHash));
}

evaluation_t SearchEngine::runAlphaBetaSearch(
    const EvaluationFactorsArray& parentEvaluationFactors,
    const PositionHash& parentHash,
    int depthPly,
    evaluation_t alpha,
    evaluation_t beta)
{
    abortSearchIfNeeded();
    if (isSearchAborted_) {
        return 0;
    }

    int storedDepthPly = std::numeric_limits<int>::max();
    Move moveToPrioritize = Move::NullMove();

    const auto transpositionValue = transpositionTable_.findValue(parentHash.getHash());
    if (transpositionValue.isNotEmpty()) {
        storedDepthPly = transpositionValue.getDepthPly();
        moveToPrioritize = transpositionValue.getBestMove();
        if (storedDepthPly >= depthPly) {
            const evaluation_t storedEvaluation = transpositionValue.getEvaluation();
            switch (transpositionValue.getEvaluationConstraint()) {
                case TranspositionTable::Exact:
                {
                    return storedEvaluation;
                }
                case TranspositionTable::AtMost:
                {
                    if (storedEvaluation <= alpha) {
                        return alpha;
                    }
                }
                case TranspositionTable::AtLeast:
                {
                    if (storedEvaluation >= beta) {
                        return beta;
                    }
                }
            }
        }
    }

    const bool isAllowTranspositionTableUpdate = (storedDepthPly < depthPly)
        || (storedDepthPly == std::numeric_limits<int>::max());

    const player_t playerToMove = position_.getPlayerToMove();
    const evaluation_t evaluationSideMultiplier = Evaluator::getSideMultiplier(playerToMove);

    EvaluationFactorsArray childEvaluationFactors;

    auto pseudoLegalMoves = position_.generatePseudoLegalMoves(
        Position::MoveGenerationFilter::AllMoves,
        memoryPool_);
    pseudoLegalMoves->scoreMoves(*evaluator_, playerToMove, moveToPrioritize);

    Move bestMove = Move::NullMove();
    bool hasLegalMoves = false;

    for (const auto& scoredMove : *pseudoLegalMoves) {
        const Move move = scoredMove.getMove();
        position_.playMove(move);

        if (position_.isNotValid()) {
            position_.undoMove();
            continue;
        }

        hasLegalMoves = true;

        evaluator_->calculateChildEvaluationFactors(
            childEvaluationFactors,
            parentEvaluationFactors,
            scoredMove,
            playerToMove);

        const PositionHash childHash = getChildHash(parentHash, position_.getPositionFlags(), scoredMove);

        evaluation_t evaluation;
        if (depthPly == 1) {
            if (move.isCapture() || move.isPromotion()) {
                evaluation = -runQuiescentSearch(childEvaluationFactors, childHash, -beta, -alpha);
            }
            else {
                evaluation = evaluationSideMultiplier * evaluate(childEvaluationFactors, childHash.getHash());
            }
        }
        else {
            evaluation = -runAlphaBetaSearch(
                childEvaluationFactors,
                childHash,
                depthPly - 1,
                -beta,
                -alpha);
        }

        if (isSearchAborted_) {
            return 0;
        }

        if (evaluation >= beta) {
            position_.undoMove();
            if (isAllowTranspositionTableUpdate) {
                transpositionTable_.insertBetaCutoff(parentHash.getHash(), move, depthPly, beta);
            }
            return beta;
        } 
        
        if (evaluation > alpha) {
            alpha = evaluation;
            bestMove = move;
        }

        position_.undoMove();
    }

    if (!bestMove.isNullMove()) {
        if (isAllowTranspositionTableUpdate) {
            transpositionTable_.insertExactEvaluation(
                parentHash.getHash(),
                bestMove,
                depthPly,
                alpha);
        }
    } else {
        if (!hasLegalMoves) {
            const auto currentSearchDepth = getCurrentSearchDepthPly(depthPly);
            const evaluation_t evaluation = evaluateNoLegalMovesPosition(currentSearchDepth);
            transpositionTable_.insertExactEvaluation(
                parentHash.getHash(),
                Move::NullMove(),
                depthPly,
                evaluation);
            return std::max(alpha, std::min(evaluation, beta));
        }
        if (isAllowTranspositionTableUpdate) {
            transpositionTable_.insertNoAlphaImprovement(
                parentHash.getHash(),
                depthPly,
                alpha);
        }
    }

    return alpha;
}

evaluation_t SearchEngine::runQuiescentSearch(
    const EvaluationFactorsArray& parentEvaluationFactors,
    const PositionHash& parentHash,
    evaluation_t alpha,
    evaluation_t beta)
{
    abortSearchIfNeeded();
    if (isSearchAborted_) {
        return 0;
    }

    const player_t playerToMove = position_.getPlayerToMove();
    const evaluation_t evaluationSideMultiplier = Evaluator::getSideMultiplier(playerToMove);
    const evaluation_t evaluation = evaluationSideMultiplier
        * evaluate(parentEvaluationFactors, parentHash.getHash());

    if (evaluation >= beta) {
        return beta;
    }

    if (evaluation > alpha) {
        alpha = evaluation;
    }

    EvaluationFactorsArray childEvaluationFactors;

    auto pseudoLegalMoves = position_.generatePseudoLegalMoves(
        Position::MoveGenerationFilter::CapturesOnly,
        memoryPool_);
    pseudoLegalMoves->scoreMoves(*evaluator_, playerToMove, Move::NullMove());

    for (const auto& scoredMove : *pseudoLegalMoves) {
        position_.playMove(scoredMove.getMove());

        if (position_.isNotValid()) {
            position_.undoMove();
            continue;
        }

        evaluator_->calculateChildEvaluationFactors(
            childEvaluationFactors,
            parentEvaluationFactors,
            scoredMove,
            playerToMove);

        const PositionHash childHash = getChildHash(parentHash, position_.getPositionFlags(), scoredMove);
        evaluation_t subtreeEvaluation = -runQuiescentSearch(childEvaluationFactors, childHash, -beta, -alpha);

        if (isSearchAborted_) {
            return 0;
        }

        if (subtreeEvaluation >= beta) {
            position_.undoMove();
            return beta;
        }

        if (subtreeEvaluation > alpha) {
            alpha = subtreeEvaluation;
        }

        position_.undoMove();
    }

    return alpha;
}

MovesCollection SearchEngine::getPrincipalVariation(PositionHash parentHash)
{
    MovesCollection moves;
    Position position = position_;

    std::unordered_set<position_hash_t> takenHashes;

    while (takenHashes.find(parentHash.getHash()) == takenHashes.end()) {
        const auto transpositionValue = transpositionTable_.findValue(parentHash.getHash());
        if (transpositionValue.isEmpty()) {
            break;
        }

        const Move move = transpositionValue.getBestMove();
        if (move.isNullMove()) {
            break;
        }

        ScoredMove scoredMove(move, *evaluator_, position.getPlayerToMove());

        auto pseudoLegalMoves = position.generatePseudoLegalMoves(
            Position::MoveGenerationFilter::AllMoves,
            memoryPool_);

        if (!pseudoLegalMoves->isContains(move)) {
            // The move was overwritten due to hash collision, cannot continue
            break;
        }

        position.playMove(move);
        if (position.isNotValid()) {
            // The move was overwritten due to hash collision, cannot continue
            break;
        }

        takenHashes.insert(parentHash.getHash());

        parentHash = getChildHash(parentHash, position.getPositionFlags(), scoredMove);
        moves.pushBack(move);
    }

    return moves;
}

int SearchEngine::getCurrentSearchDepthPly(int remainingDepthPly) const
{
    return searchDepthPly_ - remainingDepthPly + 1;
}

void SearchEngine::abortSearchIfNeeded()
{
    if (!isSearchAborted_ && evaluatedPositionCount_ > maxEvaluations_) {
        isSearchAborted_ = true;
    }
}

evaluation_t SearchEngine::evaluate(
    const EvaluationFactorsArray& factors,
    const position_hash_t positionHash)
{
    ++evaluatedPositionCount_;
    position_hash_t randomValue = (positionHash ^ randomHash_) & randomHashMask_;
    evaluation_t evaluationRandomValue = static_cast<evaluation_t>(randomValue) - evaluationRandomness_;
    return evaluator_->evaluate(factors) + evaluationRandomValue;
}

evaluation_t SearchEngine::evaluateNoLegalMovesPosition(int currentSearchDepthPly)
{
    ++evaluatedPositionCount_;
    return evaluator_->evaluateNoLegalMovesPosition(position_, currentSearchDepthPly);
}

PositionHash SearchEngine::getChildHash(
    const PositionHash& parentHash,
    PositionFlags childPositionFlags,
    const ScoredMove& movePlayed)
{
    const uint64_t childFlagsHash = ZobristHasher::getInstance().getValue(childPositionFlags);
    const uint64_t childHash = parentHash.getHash()
        ^ parentHash.getFlagsHash()
        ^ childFlagsHash
        ^ movePlayed.getHash();
    return PositionHash(childHash, childFlagsHash);
}

}
