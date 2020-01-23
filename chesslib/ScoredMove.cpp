#include "ScoredMove.h"

#include "Evaluator.h"
#include "squares.h"
#include "ZobristHasher.h"

namespace chesslib {

namespace {

std::array<position_hash_t, PLAYER_COUNT> shortCastleRookHash = {
    ZobristHasher::getInstance().getValue(Black, Squares::H8, Rook)
        ^ ZobristHasher::getInstance().getValue(Black, Squares::F8, Rook),
    ZobristHasher::getInstance().getValue(White, Squares::H1, Rook)
        ^ ZobristHasher::getInstance().getValue(White, Squares::F1, Rook)
};

std::array<position_hash_t, PLAYER_COUNT> longCastleRookHash = {
    ZobristHasher::getInstance().getValue(Black, Squares::A8, Rook)
        ^ ZobristHasher::getInstance().getValue(Black, Squares::D8, Rook),
    ZobristHasher::getInstance().getValue(White, Squares::A1, Rook)
        ^ ZobristHasher::getInstance().getValue(White, Squares::D1, Rook)
};

}

ScoredMove::ScoredMove()
    : score_(0)
    , myMaterialGain_(0)
    , theirMaterialGain_(0)
    , myTableValueGain_(0)
    , theirTableValueGain_(0)
    , hash_(0)
{
}

ScoredMove::ScoredMove(Move move, const Evaluator& evaluator, player_t player)
    : ScoredMove()
{
    setMove(move);
    updateScore(evaluator, player);
}

void ScoredMove::updateScore(const Evaluator& evaluator, player_t player)
{
    myMaterialGain_ = 0;
    theirMaterialGain_ = 0;
    myTableValueGain_ = 0;
    theirTableValueGain_ = 0;
    hash_ = 0;

    const piece_type_t pieceType = move_.getPieceType();
    const square_t originSquare = move_.getOriginSquare();
    const square_t destSquare = move_.getDestSquare();
    const auto& hasher = ZobristHasher::getInstance();

    // Player lose value from piece's origin square
    myTableValueGain_ -= Evaluator::getTableValue(pieceType, player, originSquare);
    hash_ ^= hasher.getValue(player, originSquare, pieceType);

    // Player gains value for piece's dest square
    if (move_.isPromotion()) {
        const piece_type_t finalPieceType = move_.getPromoteToPieceType();
        myTableValueGain_ += Evaluator::getTableValue(finalPieceType, player, destSquare);
        myMaterialGain_ += evaluator.getMaterialValue(finalPieceType) - evaluator.getMaterialValue(Pawn);
        hash_ ^= hasher.getValue(player, destSquare, finalPieceType);
    }
    else {
        myTableValueGain_ += Evaluator::getTableValue(pieceType, player, destSquare);
        hash_ ^= hasher.getValue(player, destSquare, pieceType);
    }

    if (move_.isCapture()) {
        const piece_type_t capturedPieceType = move_.getCapturedPieceType();
        square_t capturedSquare = move_.getCapturedPieceSquare();
        theirTableValueGain_ -= evaluator.getTableValue(capturedPieceType, getOtherPlayer(player), capturedSquare);
        theirMaterialGain_ -= evaluator.getMaterialValue(capturedPieceType);
        hash_ ^= hasher.getValue(getOtherPlayer(player), capturedSquare, capturedPieceType);
    }
    else if (move_.isCastle()) {
        const square_t baseRow = player == Black ? ROW_8 : ROW_1;
        if (move_.isShortCastle()) {
            myTableValueGain_ += Evaluator::getTableValue(Rook, player, encodeSquare(baseRow, COLUMN_F))
                - Evaluator::getTableValue(Rook, player, encodeSquare(baseRow, COLUMN_H));
            hash_ ^= shortCastleRookHash[player];
        }
        else {
            myTableValueGain_ += Evaluator::getTableValue(Rook, player, encodeSquare(baseRow, COLUMN_D))
                - Evaluator::getTableValue(Rook, player, encodeSquare(baseRow, COLUMN_A));;
            hash_ ^= longCastleRookHash[player];
        }
    }

    score_ = myMaterialGain_ + myTableValueGain_
        - theirMaterialGain_ - theirTableValueGain_;
}

}
