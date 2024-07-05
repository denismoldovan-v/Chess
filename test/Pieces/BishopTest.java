package Pieces;

import Chess.Game.Board;
import Chess.Game.Square;
import Chess.Pieces.Bishop;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BishopTest {

    private Bishop whiteBishop;
    private Bishop blackBishop;
    private Board board;

    @Before
    public void setUp() {
        board = new Board();
        whiteBishop = new Bishop(board.getSquare(3, 3), true);
        blackBishop = new Bishop(board.getSquare(4, 4), false);
    }

    @Test
    public void constructorTest() {
        assertNotNull(whiteBishop);
        assertNotNull(blackBishop);
        assertEquals(true, whiteBishop.isWhite());
        assertEquals(false, blackBishop.isWhite());
    }

    @Test
    public void getLegalMovesTest() {
        // Test legalnych ruchów dla białego skoczka
        List<Square> whiteLegalMoves = whiteBishop.getLegalMoves(whiteBishop.getOccupyingSquare());
        assertEquals(13, whiteLegalMoves.size()); // Jest 13 legalnych ruchów dla białego skoczkaimport

        // Test legalnych ruchów dla czarnego skoczka
        List<Square> blackLegalMoves = blackBishop.getLegalMoves(blackBishop.getOccupyingSquare());
        assertEquals(13, blackLegalMoves.size()); // Jest 13 legalnych ruchów dla czarnego skoczka
    }

    @Test
    public void toStringTest() {
        assertEquals("B", whiteBishop.toString());
        assertEquals("b", blackBishop.toString());
    }
}