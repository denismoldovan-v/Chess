package Pieces;

import Chess.Game.Square;
import Chess.Pieces.Rook;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RookTest {

    @Test
    public void constructorTest() {
        Square square = new Square(0, 0);
        Rook rookWhite = new Rook(square, true);
        Rook rookBlack = new Rook(square, false);

        assertNotNull(rookWhite);
        assertNotNull(rookBlack);
        assertEquals(square, rookWhite.getOccupyingSquare());
        assertEquals(square, rookBlack.getOccupyingSquare());
        assertTrue(rookWhite.isWhite());
        assertFalse(rookBlack.isWhite());
        assertTrue(rookWhite.isCastlingAllowed());
        assertTrue(rookBlack.isCastlingAllowed());
    }

    @Test
    public void isCastlingAllowedTest() {
        Square square = new Square(0, 0);
        Rook rook = new Rook(square, true);

        assertTrue(rook.isCastlingAllowed());
    }

    @Test
    public void disallowCastlingTest() {
        Square square = new Square(0, 0);
        Rook rook = new Rook(square, true);

        rook.disallowCastling();
        assertFalse(rook.isCastlingAllowed());
    }
}
