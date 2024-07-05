package Chess.Game;

import Chess.Pieces.Pawn;
import Chess.Pieces.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testBoardInitialization() {
        // Test liczby figur
        assertEquals(32, board.getWhitePieces().size());
        assertEquals(32, board.getBlackPieces().size());

        // Test ustawienia pionków
        for (int col = 0; col < 8; col++) {
            Piece whitePawn = board.getSquare(1, col).getOccupyingPiece();
            Piece blackPawn = board.getSquare(6, col).getOccupyingPiece();
            assertTrue(whitePawn instanceof Pawn);
            assertTrue(blackPawn instanceof Pawn);
            assertTrue(whitePawn.isWhite());
        }
    }

    @Test
    void testPieceMovement() {
        Board board = new Board();
        // Test prawidłowych ruchów
        // Test pionka
        Piece Pawn = board.getSquare(1, 0).getOccupyingPiece();
        board.makeMove(new Move(board.getSquare(1, 0), board.getSquare(3, 0)));
        assertTrue(board.getSquare(3, 0).getOccupyingPiece() == Pawn);

        // Test skoczka
        Piece Knight = board.getSquare(0, 1).getOccupyingPiece();
        board.makeMove(new Move(board.getSquare(0, 1), board.getSquare(2, 2)));
        assertTrue(board.getSquare(2, 2).getOccupyingPiece() == Knight);

        // Test gońca
        Piece Bishop = board.getSquare(0, 2).getOccupyingPiece();
        board.makeMove(new Move(board.getSquare(0, 2), board.getSquare(3, 5)));
        assertTrue(board.getSquare(3, 5).getOccupyingPiece() == Bishop);

        // Test wieży
        Piece Rook = board.getSquare(0, 3).getOccupyingPiece();
        board.makeMove(new Move(board.getSquare(0, 3), board.getSquare(3, 3)));
        assertTrue(board.getSquare(3, 3).getOccupyingPiece() == Rook);

        // Test hetmana
        Piece Queen = board.getSquare(0, 4).getOccupyingPiece();
        board.makeMove(new Move(board.getSquare(0, 4), board.getSquare(3, 4)));
        assertTrue(board.getSquare(3, 4).getOccupyingPiece() == Queen);

        // Test króla
        Piece King = board.getSquare(0, 5).getOccupyingPiece();
        board.makeMove(new Move(board.getSquare(0, 5), board.getSquare(3, 6)));
        assertTrue(board.getSquare(3, 6).getOccupyingPiece() == King);

        // Test nieprawidłowych ruchów pionka
        Piece whitePawn = board.getSquare(1, 0).getOccupyingPiece();
        board.makeMove(new Move(board.getSquare(1, 0), board.getSquare(1, 1))); // Ruch na zajęte pole
        assertTrue(board.getSquare(1, 1).getOccupyingPiece() == whitePawn);

        board.makeMove(new Move(board.getSquare(1, 0), board.getSquare(9, 0))); // Ruch poza zakres
        assertTrue(board.getSquare(9, 0).getOccupyingPiece() == null);

        board.makeMove(new Move(board.getSquare(1, 0), board.getSquare(1, 0))); // Ruch na własne pole
        assertTrue(board.getSquare(1, 0).getOccupyingPiece() == whitePawn);

        // Test nieprawidłowych ruchów skoczka
        Piece whiteKnight = board.getSquare(0, 1).getOccupyingPiece();
        board.makeMove(new Move(board.getSquare(0, 1), board.getSquare(2, 2))); // Ruch na zajęte pole
        assertTrue(board.getSquare(2, 2).getOccupyingPiece() == whiteKnight);

        board.makeMove(new Move(board.getSquare(0, 1), board.getSquare(9, 2))); // Ruch poza zakres
        assertTrue(board.getSquare(9, 2).getOccupyingPiece() == null);

        board.makeMove(new Move(board.getSquare(0, 1), board.getSquare(0, 1))); // Ruch na własne pole
        assertTrue(board.getSquare(0, 1).getOccupyingPiece() == whiteKnight);

        // Test nieprawidłowych ruchów gońca
        Piece whiteBishop = board.getSquare(0, 2).getOccupyingPiece();
        board.makeMove(new Move(board.getSquare(0, 2), board.getSquare(2, 3))); // Ruch na zajęte pole
        assertTrue(board.getSquare(2, 3).getOccupyingPiece() == whiteBishop);

        board.makeMove(new Move(board.getSquare(0, 2), board.getSquare(9, 5))); // Ruch poza zakres
        assertTrue(board.getSquare(9, 5).getOccupyingPiece() == null);

        board.makeMove(new Move(board.getSquare(0, 2), board.getSquare(0, 2))); // Ruch na własne pole
        assertTrue(board.getSquare(0, 2).getOccupyingPiece() == whiteBishop);

        // Test nieprawidłowych ruchów wieży
        Piece whiteRook = board.getSquare(0, 3).getOccupyingPiece();
        board.makeMove(new Move(board.getSquare(0, 3), board.getSquare(2, 3))); // Ruch na zajęte pole
        assertTrue(board.getSquare(2, 3).getOccupyingPiece() == whiteRook);

        board.makeMove(new Move(board.getSquare(0, 3), board.getSquare(9, 3))); // Ruch poza zakres
        assertTrue(board.getSquare(9, 3).getOccupyingPiece() == null);

        board.makeMove(new Move(board.getSquare(0, 3), board.getSquare(0, 3))); // Ruch na własne pole
        assertTrue(board.getSquare(0, 3).getOccupyingPiece() == whiteRook);

        // Test nieprawidłowych ruchów hetmana
        Piece whiteQueen = board.getSquare(0, 4).getOccupyingPiece();
        board.makeMove(new Move(board.getSquare(0, 4), board.getSquare(2, 4)));
        assertTrue(board.getSquare(2, 4).getOccupyingPiece() == whiteQueen);
        assertTrue(board.getSquare(2, 4).getOccupyingPiece().isWhite());           // Ruch na zajęte pole

        board.makeMove(new Move(board.getSquare(0, 4), board.getSquare(3, 3)));     // Nieprawidłowy ruch na własne pole
        assertTrue(board.getSquare(3, 3).getOccupyingPiece() == null);

        board.makeMove(new Move(board.getSquare(0, 4), board.getSquare(9, 9)));
        assertTrue(board.getSquare(9, 9).getOccupyingPiece() == null);         // Ruch poza zakres

        Piece whiteKing = board.getSquare(0, 5).getOccupyingPiece();

        // Test nieprawidłowych ruchów hetmana
        board.makeMove(new Move(board.getSquare(0, 5), board.getSquare(2, 5)));
        assertTrue(board.getSquare(2, 5).getOccupyingPiece() == whiteKing);
        assertTrue(board.getSquare(2, 5).getOccupyingPiece().isWhite());        // Ruch na zajęte pole

        board.makeMove(new Move(board.getSquare(0, 5), board.getSquare(3, 3)));
        assertTrue(board.getSquare(3, 3).getOccupyingPiece() == null); // Nieprawidłowy ruch na własne pole

        board.makeMove(new Move(board.getSquare(0, 5), board.getSquare(9, 9)));
        assertTrue(board.getSquare(9, 9).getOccupyingPiece() == null);
    }
    @Test
    void testPawnPromotion() {
        // Test the promotion of a pawn
        //...
    }

    @Test
    void testEnPassant() {
        // Test the en passant move
        //...
    }

    @Test
    void testCastling() {
        // Test the castling move
        //...
    }

    @Test
    void testInvalidMoves() {
        // Test the correct handling of invalid moves
        //...
    }

    @Test
    void testGameEndConditions() {
        // Test the correct handling of game end conditions
        //...
    }

    @Test
    void testGameState() {
        // Test the correct handling of the game state
        //...
    }

    @Test
    void testGameClock() {
        // Test the correct handling of the game clock
        //...
    }

    @Test
    void testSerialization() {
        // Test the correct handling of serialization and deserialization
        //...
    }
}