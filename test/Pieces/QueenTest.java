package Pieces;

import Chess.Game.Board;
import Chess.Game.Square;
import Chess.Pieces.Queen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueenTest {
    private Queen whiteQueen;
    private Queen blackQueen;
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        whiteQueen = new Queen(board.getSquare(0, 3), true);
        blackQueen = new Queen(board.getSquare(7, 3), false);
    }

    @Test
    void getLegalMoves_initialPosition() {
        List<Square> legalMoves = whiteQueen.getLegalMoves(whiteQueen.getOccupyingSquare());
        assertEquals(27, legalMoves.size());
    }

    @Test
    void getLegalMoves_straightLine() {
        // Ruch białego hetmana po linii
        List<Square> legalMoves = whiteQueen.getLegalMoves(whiteQueen.getOccupyingSquare());
        assertEquals(14, legalMoves.size());

        // Ruch czarnego hetmana po linii
        legalMoves = blackQueen.getLegalMoves(blackQueen.getOccupyingSquare());
        assertEquals(14, legalMoves.size());
    }

    @Test
    void getLegalMoves_diagonal() {
        whiteQueen.getLegalMoves(board.getSquare(3, 3));
        List<Square> legalMoves = whiteQueen.getLegalMoves(whiteQueen.getOccupyingSquare());
        assertEquals(27, legalMoves.size());

        // Ruch białego hetmana po przekątnej
        legalMoves = whiteQueen.getLegalMoves(board.getSquare(3, 3));
        assertEquals(27, legalMoves.size());

        // Ruch czarnego hetmana po przekątnej
        legalMoves = blackQueen.getLegalMoves(board.getSquare(4, 3));
        assertEquals(27, legalMoves.size());
    }

    @Test
    void getLegalMoves_capture() {
        // Biały hetman zbiera czarną figurę
        board.getSquare(5, 3).setOccupyingPiece(blackQueen);
        whiteQueen.getLegalMoves(board.getSquare(3, 3));
        List<Square> legalMoves = whiteQueen.getLegalMoves(whiteQueen.getOccupyingSquare());
        assertTrue(legalMoves.contains(board.getSquare(5, 3)));

        // Czarny hetman zbiera białą figurę
        board.getSquare(3, 3).setOccupyingPiece(whiteQueen);
        blackQueen.getLegalMoves(board.getSquare(4, 3));
        legalMoves = blackQueen.getLegalMoves(blackQueen.getOccupyingSquare());
        assertTrue(legalMoves.contains(board.getSquare(3, 3)));
    }

    @Test
    void getLegalMoves_boardBoundaries() {
        // Ruch białego hetmana na granicy planszy
        whiteQueen.getLegalMoves(board.getSquare(3, 3));
        List<Square> legalMoves = whiteQueen.getLegalMoves(whiteQueen.getOccupyingSquare());
        assertEquals(27, legalMoves.size());

        // Ruch czarnego hetmana na granicy planszy
        blackQueen.getLegalMoves(board.getSquare(4, 3));
        legalMoves = blackQueen.getLegalMoves(blackQueen.getOccupyingSquare());
        assertEquals(27, legalMoves.size());
    }
}

