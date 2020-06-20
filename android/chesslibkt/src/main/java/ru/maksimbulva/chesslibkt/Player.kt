package ru.maksimbulva.chesslibkt

enum class Player {
    Black,
    White
}

fun getOtherPlayer(player: Player): Player {
    return when (player) {
        Player.Black -> Player.White
        Player.White -> Player.Black
    }
}
