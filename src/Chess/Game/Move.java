package Chess.Game;

import Chess.Pieces.Pawn;
import Chess.Pieces.Piece;

import java.io.Serializable;
import java.util.Objects;

/**
 * Klasa reprezentująca wykonany ruch. Zawiera informacje nt. pola początkowego, końcowego oraz figury/piona, która wykonała ruch, a także tego, czy ruch był atakiem albo roszadą.
 */
public class Move implements Serializable {
    private final Square from;
    private final Square to;
    private final Piece piece;
    private Boolean isCapture = false;
    private Boolean isCastling = false;

    /**
     * Konstruktor ruchu. Automatycznie pobiera element z pola początkowego (nie trzeba go podawać). Miota błędem, jeśli nie ma elementu.
     * @param from Pole początkowe.
     * @param to Pole końcowe.
     */
    public Move(Square from, Square to){
        piece = from.getOccupyingPiece();
        if(piece == null) throw new IllegalArgumentException("Brak elementu");
        this.from = from;
        this.to = to;
    }

    /**
     * Pusty ruch. (brak ruchu)
     */
    public Move() {
        from = null;
        to = null;
        piece = null;
    }

    /**
     * Pole początkowe.
     * @return Obiekt klasy Square.
     */
    public Square getFrom() {
        return from;
    }

    /**
     * Pole końcowe.
     * @return Obiekt klasy Square.
     */
    public Square getTo() {
        return to;
    }

    /**
     * Przemieszczony element.
     * @return Obiekt klasy Piece.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Metoda określająca, czy został przemieszczony pion.
     * @return True, jeśli tak.
     */
    public Boolean isPawnMoved() {
        return piece instanceof Pawn;
    }

    /**
     * Metoda określająca, czy ruch był atakiem.
     * @return True, jeśli tak.
     */
    public Boolean isCapture() {
        return isCapture;
    }

    /**
     * Ustawienie flagi określającej atak na true.
     */
    public void setCapture(){
        assert !isCastling;
        isCapture = true;
    }

    /**
     * Metoda określająca, czy ruch był roszadą.
     * @return True, jeśli tak.
     */
    public Boolean isCastling() {
        return isCastling;
    }

    /**
     * Ustawienie flagi określającej roszadę na true.
     */
    public void setCastling(){
        assert !isCapture;
        isCastling = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(from, move.from) && Objects.equals(to, move.to) && Objects.equals(piece, move.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, piece);
    }

    @Override
    public String toString() {
        return piece + " moved from " + from + " to " + to + (isCapture? " (Att)" : "") + (isCastling? " (Cas)" : "");
    }
}
