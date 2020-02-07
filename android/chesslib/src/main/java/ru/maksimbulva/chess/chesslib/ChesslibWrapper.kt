package ru.maksimbulva.chess.chesslib

import ru.maksimbulva.chess.core.engine.Piece
import ru.maksimbulva.chess.core.engine.Player
import ru.maksimbulva.chess.core.engine.Variation
import ru.maksimbulva.chess.core.engine.fen.FenDecoder
import ru.maksimbulva.chess.core.engine.move.Move
import ru.maksimbulva.chess.core.engine.move.MoveGenerator
import ru.maksimbulva.chess.core.notation.CoordinateNotation
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ChesslibWrapper : AbsChesslibWrapper() {

    private val enginePointer = createEngineInstance()

    private val lock: Lock = ReentrantLock()

    private var isEngineBusy = false

    fun destroy() {
        lock.withLock {
            require(!isEngineBusy)
            releaseEngineInstance(enginePointer)
        }
    }

    fun resetGame() {
        lock.withLock {
            require(!isEngineBusy)
            resetGame(enginePointer)
        }
    }

    fun resetGame(fen: String) {
        lock.withLock {
            require(!isEngineBusy)
            resetGame(fen, enginePointer)
        }
    }

    fun currentPositionFen(): String {
        lock.withLock {
            require(!isEngineBusy)
            return getCurrentPositionFen(enginePointer)
        }
    }

    fun playMove(move: Move): Boolean {
        lock.withLock {
            require(!isEngineBusy)
            return playMove(CoordinateNotation.moveToString(move), enginePointer)
        }
    }

    // Must not be ran on UI thread
    fun findBestVariation(onlyFirstMove: Boolean): Variation {
        lock.withLock {
            require(!isEngineBusy)
            isEngineBusy = true
        }

        val variationStringTokens = findBestVariation(enginePointer).split(' ')
        isEngineBusy = false

        val evaluation = variationStringTokens.first().toInt()
        val moves = mutableListOf<Move>()
        var position = FenDecoder.decode(currentPositionFen())
        for (tokenIndex in 1 until variationStringTokens.size) {
            val variationMoveString = variationStringTokens[tokenIndex]
            val move = MoveGenerator.generateMoves(position)
                .first { move -> CoordinateNotation.moveToString(move) == variationMoveString }
            moves.add(move)
            if (onlyFirstMove) {
                break
            }
            position = position.playMove(move)
        }
        return Variation(evaluation, moves)
    }

    fun setPlayerEvaluationsLimit(player: Player, evaluationsLimit: Long) {
        lock.withLock {
            require(!isEngineBusy)
            val playerIndex = getChesslibPlayerIndex(player)
            setPlayerEvaluationsLimit(playerIndex, evaluationsLimit, enginePointer)
        }
    }

    fun setDegreeOfRandomness(player: Player, degreeOfRandomness: Int) {
        require(degreeOfRandomness == 0 || isPowerOfTwo(degreeOfRandomness))
        lock.withLock {
            require(!isEngineBusy)
            val playerIndex = getChesslibPlayerIndex(player)
            setDegreeOfRandomness(playerIndex, degreeOfRandomness, enginePointer)
        }
    }

    fun setMaterialValue(player: Player, piece: Piece, materialValue: Int) {
        require(piece in setOf(Piece.Pawn, Piece.Knight, Piece.Bishop, Piece.Rook, Piece.Queen))
        lock.withLock {
            require(!isEngineBusy)
            val playerIndex = getChesslibPlayerIndex(player)
            setMaterialValue(playerIndex, piece.toChesslibPieceType(), materialValue, enginePointer)
        }
    }

    fun getPlayer(player: Player): ChesslibPlayerWrapper {
        return ChesslibPlayerWrapper(this, player)
    }

    companion object {
        private const val CHESSLIB_BLACK_PLAYER_INDEX = 0
        private const val CHESSLIB_WHITE_PLAYER_INDEX = 1

        private fun getChesslibPlayerIndex(player: Player) = when (player) {
            Player.Black -> CHESSLIB_BLACK_PLAYER_INDEX
            Player.White -> CHESSLIB_WHITE_PLAYER_INDEX
            else -> throw IllegalArgumentException()
        }
    }
}

private fun isPowerOfTwo(x: Int) = (x > 0) && (x and (x - 1) == 0)
