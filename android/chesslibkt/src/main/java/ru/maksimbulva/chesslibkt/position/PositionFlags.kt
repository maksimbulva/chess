package ru.maksimbulva.chesslibkt.position

import ru.maksimbulva.chesslibkt.Player

inline class PositionFlags(val encoded: Int) {

    val playerToMove: Player
        get() = when (encoded and PLAYER_MASK) {
            0 -> Player.Black
            else -> Player.White
        }

//    OptionalColumn getEnPassantColumn() const
//    {
//        auto encoded = (encoded_ & EN_PASSANT_COLUMN_MASK) >> EN_PASSANT_COLUMN_SHIFT;
//        return OptionalColumn::fromEncoded(encoded);
//    }

    fun setPlayerToMove(playerToMove: Player): PositionFlags {
        var newEncoded = encoded and PLAYER_MASK.inv()
        newEncoded = newEncoded or playerToMove.ordinal
        return PositionFlags(newEncoded)
    }

//    void setEnPassantColumn(OptionalColumn column)
//    {
//        encoded_ &= ~EN_PASSANT_COLUMN_MASK;
//        encoded_ |= column.getEncodedValue() << EN_PASSANT_COLUMN_SHIFT;
//    }

//    CastleOptions getCastleOptions(player_t player) const
//    {
//        const int shift = (player * 2) + 1;
//        return CastleOptions((encoded_ >> shift) & CASTLE_OPTIONS_MASK);
//    }
//
//    void setCastleOptions(player_t player, CastleOptions castleOptions)
//    {
//        const int shift = (player * 2) + 1;
//        const encoded_position_flags_t mask = CASTLE_OPTIONS_MASK << shift;
//        encoded_ &= ~mask;
//        encoded_ |= castleOptions.getEncodedValue() << shift;
//    }
//
//    OptionalBoolean getIsInCheck() const
//    {
//        return OptionalBoolean((encoded_ & IS_IN_CHECK_MASK) >> IS_IN_CHECK_SHIFT);
//    }
//
//    void setIsInCheck(bool value)
//    {
//        encoded_ &= ~IS_IN_CHECK_MASK;
//        encoded_ |= (OptionalBoolean(value).getEncodedValue()) << IS_IN_CHECK_SHIFT;
//    }

    fun onMovePlayed(): PositionFlags {
        return PositionFlags(encoded and IS_IN_CHECK_MASK.inv())
    }

//    static constexpr encoded_position_flags_t PLAYER_MASK = 1;
//    static constexpr encoded_position_flags_t CASTLE_OPTIONS_MASK = 3;
//    static constexpr int EN_PASSANT_COLUMN_SHIFT = 6;
//    static constexpr encoded_position_flags_t EN_PASSANT_COLUMN_MASK = 15 << EN_PASSANT_COLUMN_SHIFT;

    fun clone() = PositionFlags(encoded)

    private companion object {
        private const val PLAYER_MASK = 1

        private const val IS_IN_CHECK_SHIFT = 10
        private const val IS_IN_CHECK_MASK = 3 shl IS_IN_CHECK_SHIFT
    }
}
