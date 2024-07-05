package Pieces;

import Chess.Game.Board;
import Chess.Game.Square;
import Chess.Pieces.Pawn;
import Chess.Pieces.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    private Piece whitePiece;
    private Piece blackPiece;
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        whitePiece = new Pawn(board.getSquare(1, 0), true); // Replace with any piece subclass
        blackPiece = new Pawn(board.getSquare(6, 7), false); // Replace with any piece subclass
    }

    @Test
    void testConstructor() {
        assertTrue(whitePiece.isWhite());
        assertFalse(blackPiece.isWhite());
    }

    @Test
    void testSetOccupyingSquare() {
        Square newSquare = board.getSquare(3, 3);
        whitePiece.setOccupyingSquare(newSquare);
        assertEquals(newSquare, whitePiece.getOccupyingSquare());
    }

    @Test
    void testGetOccupyingSquare() {
        assertEquals(board.getSquare(1, 0), whitePiece.getOccupyingSquare());
    }

    @Test
    void testRemove() {
        whitePiece.remove();
        assertNull(whitePiece.getOccupyingSquare());
    }

    @Test
    void testIsWhite() {
        assertTrue(whitePiece.isWhite());
        assertFalse(blackPiece.isWhite());
    }

    @Test
    void testGetProperMoves() {
        List<Square> squares = List.of(board.getSquare(2, 0), board.getSquare(3, 0));
        List<Square> properMoves = Piece.ProperMovesHandler.getProperMoves(whitePiece, squares);
    }
}