package chess.engine.core

enum class Player {
    Black,
    White
}

fun Player.otherPlayer() = if (this == Player.Black) Player.White else Player.Black