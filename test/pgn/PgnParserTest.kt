package pgn

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PgnParserTest {

    @Test
    fun parseTest() {
        val pgnGames = PgnParser.parse(PGN_FILE.split('\n').iterator())
        assertEquals(2, pgnGames.size)

        with (pgnGames[0]) {
            assertEquals("IBM Kasparov vs. Deep Blue Rematch", tags["Event"])
            assertEquals("6", tags["Round"])
            assertEquals("Caro-Kann: 4...Nd7", tags["Opening"])
            assertEquals("1-0", tags["Result"])
            assertEquals(37, moves.size)
        }

        with (pgnGames[1]) {
            assertEquals("C51", tags["ECO"])
            assertEquals(91, moves.size)
        }
    }

    companion object {
        private val PGN_FILE = """
            [Event "IBM Kasparov vs. Deep Blue Rematch"]
            [Site "New York, NY USA"]
            [Date "1997.05.11"]
            [Round "6"]
            [White "Deep Blue"]
            [Black "Kasparov, Garry"]
            [Opening "Caro-Kann: 4...Nd7"]
            [ECO "B17"]
            [Result "1-0"]
             
            1.e4 c6 2.d4 d5 3.Nc3 dxe4 4.Nxe4 Nd7 5.Ng5 Ngf6 6.Bd3 e6 7.N1f3 h6
            8.Nxe6 Qe7 9.O-O fxe6 10.Bg6+ Kd8 {Каспаров встряхнул головой} 
            11.Bf4 b5 12.a4 Bb7 13.Re1 Nd5 14.Bg3 Kc8 15.axb5 cxb5 16.Qd3 Bc6 
            17.Bf5 exf5 18.Rxe7 Bxe7 19.c4 1-0
            
            [Event "New Orleans m"]
            [Site "New Orleans"]
            [Date "1849.??.??"]
            [Round "?"]
            [White "Morphy, Paul "]
            [Black "Morphy, Alonzo"]
            [Result "1-0"]
            [WhiteElo ""]
            [BlackElo ""]
            [ECO "C51"]
            
            1.e4 e5 2.Nf3 Nc6 3.Bc4 Bc5 4.b4 Bxb4 5.c3 Bc5 6.d4 exd4 7.cxd4 Bb6 8.O-O Na5
            9.Bd3 Ne7 10.Nc3 O-O 11.Ba3 d6 12.e5 Bf5 13.exd6 cxd6 14.Ne4 d5 15.Nf6+ gxf6
            16.Bxe7 Qxe7 17.Bxf5 Nc4 18.Re1 Qd6 19.Ne5 fxe5 20.Qg4+ Kh8 21.Qh5 Kg7 22.Qg5+ Kh8
            23.Qh5 h6 24.Rxe5 Nxe5 25.dxe5 Qc6 26.e6 Kg7 27.g4 Qc3 28.g5 Qxa1+ 29.Kg2 Qf6
            30.gxf6+ Kxf6 31.exf7 Rxf7 32.Qg6+ Ke7 33.Qe6+ Kf8 34.Qxh6+ Rg7+ 35.Bg6 Kg8
            36.h4 d4 37.h5 d3 38.Qg5 Rd8 39.h6 d2 40.Qf6 Rgd7 41.Bf5 d1=Q 42.h7+ Rxh7
            43.Be6+ Rf7 44.Bxf7+ Kh7 45.Qg6+ Kh8 46.Qh6+  1-0            
        """.trimIndent()
    }
}