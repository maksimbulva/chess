package ru.maksimbulva.chess.core.engine

enum class Player {
    Black,
    White
}

fun Player.otherPlayer() = if (this == Player.Black) Player.White else Player.Black