#include "SearchEngine.h"

#include "Evaluator.h"
#include "MovesCollection.h"
#include "PositionHash.h"
#include "require.h"
#include "ZobristHasher.h"

#include <limits>

namespace chesslib {

SearchEngine::SearchEngine(Position position, Evaluator& evaluator)
    : position_(position)
    , evaluator_(&evaluator)
    , searchDepthPly_(0)
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

    const evaluation_t searchResult = runAlphaBetaSearch(
        evaluationFactors,
        positionHash,
        depthPly,
        alpha,
        beta);

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
        if (position_.isValid()) {

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
                    evaluation = evaluationSideMultiplier * evaluator_->evaluate(childEvaluationFactors);
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
            const evaluation_t evaluation = evaluator_->evaluateNoLegalMovesPosition(position_);
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
    const player_t playerToMove = position_.getPlayerToMove();
    const evaluation_t evaluationSideMultiplier = Evaluator::getSideMultiplier(playerToMove);
    const evaluation_t evaluation = evaluationSideMultiplier * evaluator_->evaluate(parentEvaluationFactors);

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
        if (position_.isValid()) {

            evaluator_->calculateChildEvaluationFactors(
                childEvaluationFactors,
                parentEvaluationFactors,
                scoredMove,
                playerToMove);

            const PositionHash childHash = getChildHash(parentHash, position_.getPositionFlags(), scoredMove);
            evaluation_t subtreeEvaluation = -runQuiescentSearch(childEvaluationFactors, childHash, -beta, -alpha);

            if (subtreeEvaluation >= beta) {
                position_.undoMove();
                return beta;
            }

            if (subtreeEvaluation > alpha) {
                alpha = subtreeEvaluation;
            }
        }
        position_.undoMove();
    }

    return alpha;
}

MovesCollection SearchEngine::getPrincipalVariation(PositionHash parentHash)
{
    MovesCollection moves;
    Position position = position_;

    while (true) {
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
        if (!position.isValid()) {
            // The move was overwritten due to hash collision, cannot continue
            break;
        }

        parentHash = getChildHash(parentHash, position.getPositionFlags(), scoredMove);
        moves.pushBack(move);
    }

    return moves;
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
