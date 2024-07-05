package Chess.Pieces;

import Chess.Game.Board;
import Chess.Game.Main;
import Chess.Game.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca figurę gońca.
 * Goniec porusza się po przekątnych.
 */
public class Bishop extends Piece {
    public final static int IMPORTANCE = 3;
    private static final String WHITE_IMAGE_PATH = "rsc/bishop_w.png";
    private static final String BLACK_IMAGE_PATH = "rsc/bishop_b.png";

    /**
     * Konstruktor klasy Bishop.
     * @param occupyingSquare Pole, na którym obecnie znajduje się goniec.
     * @param isWhite Określa, czy goniec jest biały.
     */
    public Bishop(Square occupyingSquare, Boolean isWhite) {
        super(occupyingSquare, isWhite, WHITE_IMAGE_PATH, BLACK_IMAGE_PATH);
    }

    /**
     * Metoda sprawdzająca wszystkie legalne ruchy gońca.
     * @return Lista obiektów Square reprezentujących legalne pola, na które może się poruszyć goniec.
     */
    @Override
    public List<Square> getLegalMoves(Square square) {
        // Pobiera aktualne współrzędne (kolumna i rząd) gońca.
        int xCor = square.getCol();
        int yCor = square.getRow();

        // Inicjalizuje listę legalnych ruchów.
        List<Square> legalMoves = new ArrayList<>();

        // Pobiera bieżący stan planszy.
        Board curBoard = Main.getCurrentGame().getBoard();

        // Flagi do kontroli możliwości poruszania się w czterech kierunkach na przekątnych.
        boolean f1, f2, f3, f4;
        int rowMan = 1;
        int colMan = 1;
        f1 = true;
        f2 = true;
        f3 = true;
        f4 = true;

        // Sprawdza wszystkie przekątne w zasięgu gońca.
        while(rowMan < 8){
            // Ustala, czy goniec może poruszyć się w każdym z czterech kierunków.
            if(yCor+rowMan > 7 || xCor+colMan > 7) f1 = false;
            if(yCor-rowMan < 0 || xCor+colMan > 7) f2 = false;
            if(yCor+rowMan > 7 || xCor-colMan < 0) f3 = false;
            if(yCor-rowMan < 0 || xCor-colMan < 0) f4 = false;

            // Ruch w dół i w prawo
            if(f1) {
                if (curBoard.getSquare(yCor + rowMan, xCor + colMan).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor + rowMan, xCor + colMan).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + rowMan, xCor + colMan));
                    }
                    f1 = false;
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + rowMan, xCor + colMan));
                }
            }

            // Ruch w górę i w prawo
            if(f2) {
                if (curBoard.getSquare(yCor - rowMan, xCor + colMan).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor - rowMan, xCor + colMan).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - rowMan, xCor + colMan));
                    }
                    f2 = false;
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - rowMan, xCor + colMan));
                }
            }

            // Ruch w dół i w lewo
            if(f3) {
                if (curBoard.getSquare(yCor + rowMan, xCor - colMan).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor + rowMan, xCor - colMan).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + rowMan, xCor - colMan));
                    }
                    f3 = false;
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + rowMan, xCor - colMan));
                }
            }

            // Ruch w górę i w lewo
            if(f4) {
                if (curBoard.getSquare(yCor - rowMan, xCor - colMan).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor - rowMan, xCor - colMan).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - rowMan, xCor - colMan));
                    }
                    f4 = false;
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - rowMan, xCor - colMan));
                }
            }

            // Przesuwa się o jedno pole na przekątnych.
            colMan++;
            rowMan++;
        }

        // Zwraca listę legalnych ruchów.
        return legalMoves;
    }

    @Override
    public int getImportance() {
        return IMPORTANCE;
    }

    @Override
    public int compareTo(Object o) {
        Piece p = (Piece) o;
        return Integer.compare(IMPORTANCE, p.getImportance());
    }

    /**
     * Zwraca reprezentację gońca jako tekst.
     * @return "B" dla białego gońca lub "b" dla czarnego gońca.
     */
    @Override
    public String toString(){
        if (isWhite) return "B";
        else return "b";
    }
}
