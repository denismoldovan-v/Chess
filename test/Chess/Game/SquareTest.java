package Chess.Game;

import Chess.Pieces.Piece;
import Chess.Pieces.Pawn;
import org.junit.Test;

import static org.junit.Assert.*;

public class SquareTest {

    @Test
    public void testSquareConstructor() {
        Square square = new Square(2, 3);
        assertNull(square.getOccupyingPiece());
        assertFalse(square.isOccupied());
        assertEquals(2, (long) square.getRow());
        assertEquals(3, (long) square.getCol());
        assertTrue(square.isWhite() ^ ((square.getRow() + square.getCol()) % 2 == 0));
    }

    @Test
    public void testSquareConstructorWithPiece() {
        Piece piece = new Pawn(new Square(2, 3), true);
        Square square = new Square(2, 3, piece);
        assertEquals(piece, square.getOccupyingPiece());
        assertTrue(square.isOccupied());
    }

    @Test
    public void testSquareCopyConstructor() {
        Piece piece = new Pawn(new Square(2, 3), true);
        Square originalSquare = new Square(2, 3, piece);
        Square copiedSquare = new Square(originalSquare);
        assertEquals(originalSquare.getOccupyingPiece(), copiedSquare.getOccupyingPiece());
        assertEquals(originalSquare.isOccupied(), copiedSquare.isOccupied());
        assertEquals((long) originalSquare.getRow(), (long) copiedSquare.getRow());
        assertEquals((long) originalSquare.getCol(), (long) copiedSquare.getCol());
        assertEquals(originalSquare.isWhite(), copiedSquare.isWhite());
    }

    @Test
    public void testGettersAndSetters() {
        Square square = new Square(2, 3);
        Piece piece = new Pawn(new Square(2, 3), true);
        square.setOccupyingPiece(piece);
        assertTrue(square.isOccupied());
        assertEquals(piece, square.getOccupyingPiece());
    }

    @Test
    public void testPutPiece() {
        Square square = new Square(2, 3);
        Piece piece = new Pawn(square, true);
        square.putPiece(piece);
        assertTrue(square.isOccupied());
        assertEquals(piece, square.getOccupyingPiece());
    }

    @Test
    public void testRemoveOccupyingPiece() {
        Square square = new Square(2, 3, new Pawn(new Square(2, 3), true));
        square.removeOccupyingPiece();
        assertNull(square.getOccupyingPiece());
        assertFalse(square.isOccupied());
    }

    @Test
    public void testToString() {
        Square square = new Square(2, 3, new Pawn(new Square(2, 3), true));
        assertEquals("[2, 3] White", square.toString());
    }
}