package Pieces;

import Chess.Game.Square;
import Chess.Pieces.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PawnTest {
    private Pawn whitePawn;
    private Pawn blackPawn;

    @BeforeEach
    void setUp() {
        whitePawn = new Pawn(new Square(4, 6), true);
        blackPawn = new Pawn(new Square(4, 1), false);
    }

    @Test
    void testPawnConstructor() {
        assertNotNull(whitePawn);
        assertNotNull(blackPawn);
        assertEquals(true, whitePawn.isWhite);
        assertEquals(false, blackPawn.isWhite);
    }

    @Test
    void testToString() {
        assertEquals("P", whitePawn.toString());
        assertEquals("p", blackPawn.toString());
    }
}
