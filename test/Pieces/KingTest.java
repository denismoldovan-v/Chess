package Pieces;

import Chess.Game.Board;
import Chess.Game.Square;
import Chess.Pieces.King;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {
    private King whiteKing;
    private King blackKing;
    private Board board;

       @BeforeEach
    void setUp() {
        board = new Board();
        whiteKing = new King(board.getSquare(0, 4), true);
        blackKing = new King(board.getSquare(7, 4), false);
    }
    @Test
    void testConstructor() {
        assertTrue(whiteKing.isWhite());
        assertFalse(blackKing.isWhite());
    }
    @Test
    void testGetLegalMoves() {
        // Test legalnych ruchów dla białego króla
        board.getSquare(0, 4).setOccupyingPiece(whiteKing);
        List<Square> legalMoves = whiteKing.getLegalMoves(board.getSquare(0, 4));
        assertEquals(5, legalMoves.size());
        assertTrue(legalMoves.contains(board.getSquare(0, 3)));
        assertTrue(legalMoves.contains(board.getSquare(0, 5)));

        // Test legalnych ruchów dla czarnego króla
        board.getSquare(7, 4).setOccupyingPiece(blackKing);
        legalMoves = blackKing.getLegalMoves(board.getSquare(7, 4));
        assertEquals(5, legalMoves.size());
        assertTrue(legalMoves.contains(board.getSquare(7, 3)));
        assertTrue(legalMoves.contains(board.getSquare(7, 5)));
    }
    @Test
    void testDisallowCastling() {
        // Test blokowania roszady dla białego króla
        board.getSquare(0, 4).setOccupyingPiece(whiteKing);
        whiteKing.disallowCastling();
        assertFalse(whiteKing.isCastlingAllowed());

        // Test blokowania roszady dla czarnego króla
        board.getSquare(7, 4).setOccupyingPiece(blackKing);
        blackKing.disallowCastling();
        assertFalse(blackKing.isCastlingAllowed());
    }
    @Test
    void testToString() {
        assertEquals("A", whiteKing.toString());
        assertEquals("a", blackKing.toString());
    }
}
