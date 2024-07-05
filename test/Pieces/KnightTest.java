package Pieces;

import Chess.Game.Main;
import Chess.Game.Square;
import Chess.Pieces.Knight;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class KnightTest {
    private Knight whiteKnight;
    private Knight blackKnight;
    private Square whiteSquare;
    private Square blackSquare;

    @Before
    public void setUp() {
        whiteSquare = new Square(0, 0);
        blackSquare = new Square(7, 7);
        whiteKnight = new Knight(whiteSquare, true);
        blackKnight = new Knight(blackSquare, false);
    }

    @Test
    public void testKnightInitialization() {
        assertNotNull(whiteKnight);
        assertNotNull(blackKnight);
        assertEquals(whiteSquare, whiteKnight.getOccupyingSquare());
        assertEquals(blackSquare, blackKnight.getOccupyingSquare());
        assertTrue(whiteKnight.isWhite());
        assertFalse(blackKnight.isWhite());
    }

    @Test
    public void testGetLegalMoves() {
        // Test legalnych ruchów dla białego skoczka
        List<Square> whiteLegalMoves = whiteKnight.getLegalMoves(whiteSquare);
        assertEquals(2, whiteLegalMoves.size());
        assertTrue(whiteLegalMoves.contains(Main.getCurrentGame().getBoard().getSquare(2, 1)));
        assertTrue(whiteLegalMoves.contains(Main.getCurrentGame().getBoard().getSquare(2, -1)));

        // Test legalnych ruchów dla czarnego skoczka
        List<Square> blackLegalMoves = blackKnight.getLegalMoves(blackSquare);
        assertEquals(2, blackLegalMoves.size());
        assertTrue(blackLegalMoves.contains(Main.getCurrentGame().getBoard().getSquare(5, 6)));
        assertTrue(blackLegalMoves.contains(Main.getCurrentGame().getBoard().getSquare(5, 4)));
    }

    @Test
    public void testToString() {
        assertEquals("K", whiteKnight.toString());
        assertEquals("k", blackKnight.toString());
    }
}
