package Chess.Pieces;

import Chess.Game.Board;
import Chess.Game.Main;
import Chess.Game.Square;

import java.util.ArrayList;
import java.util.List;
/**
 * Klasa reprezentująca figurę wieży.
 * Wieża porusza się po liniach prostych w pionie lub poziomie.
 */
public class Rook extends Piece {
    private boolean isCastlingAllowed; // Flaga określająca, czy możliwa jest roszada.
    public final static int IMPORTANCE = 5;
    private static final String WHITE_IMAGE_PATH = "rsc/rook_w.png";
    private static final String BLACK_IMAGE_PATH = "rsc/rook_b.png";
    /**
     * Konstruktor klasy Rook.
     * @param occupyingSquare Pole, na którym wieża się znajduje.
     * @param isWhite Flaga określająca, czy wieża jest biała (true) czy czarna (false).
     */
    public Rook(Square occupyingSquare, Boolean isWhite) {
        super(occupyingSquare, isWhite, WHITE_IMAGE_PATH, BLACK_IMAGE_PATH);
        isCastlingAllowed = true;
    }
    /**
     * Metoda wyłączająca możliwość wykonania roszady.
     */
    public void disallowCastling(){
        isCastlingAllowed = false;
    }
    /**
     * Sprawdza, czy roszada jest dozwolona.
     * @return True, jeśli roszada jest możliwa, w przeciwnym razie False.
     */
    public Boolean isCastlingAllowed(){
        return isCastlingAllowed;
    }
    /**
     * Sprawdza legalne ruchy wieży.
     * @return Lista pól, na które wieża może się legalnie przemieścić.
     */
    @Override
    public List<Square> getLegalMoves(Square square) {
        int xCor = square.getCol(); // Aktualna kolumna wieży.
        int yCor = square.getRow(); // Aktualny rząd wieży.
        List<Square> legalMoves = new ArrayList<>(); // Lista legalnych ruchów.

        Board curBoard = Main.getCurrentGame().getBoard();  // Aktualna plansza gry.
        int rowMan = 1;
        boolean f1, f2;
        f1 = true;
        f2 = true;
        while(rowMan < 8){
            if(yCor+rowMan > 7) f1 = false; // Sprawdzenie granic planszy w dół.
            if(yCor-rowMan < 0) f2 = false; // Sprawdzenie granic planszy w górę.
            //Ruch w osi pionowej
            //W dół
            if(f1) {
                if (curBoard.getSquare(yCor + rowMan, xCor).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor + rowMan, xCor).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + rowMan, xCor));
                    }
                    f1 = false; // Napotkano inną figurę, koniec ruchu.
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + rowMan, xCor));
                }
            }
            //W górę
            if(f2) {
                if (curBoard.getSquare(yCor - rowMan, xCor).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor - rowMan, xCor).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - rowMan, xCor));
                    }
                    f2 = false; // Napotkano inną figurę, koniec ruchu.
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - rowMan, xCor));
                }
            }
            rowMan++;
        }

        int colMan = 1;
        f1 = true;
        f2 = true;
        while(colMan < 8){
            if(xCor+colMan > 7) f1 = false; // Sprawdzenie granic planszy w prawo.
            if(xCor-colMan < 0) f2 = false; // Sprawdzenie granic planszy w lewo.
            //Ruch w osi poziomej
            //W prawo
            if(f1) {
                if (curBoard.getSquare(yCor, xCor+colMan).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor, xCor+colMan).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, xCor+colMan));
                    }
                    f1 = false; // Napotkano inną figurę, koniec ruchu.
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, xCor+colMan));
                }
            }
            //W lewo
            if(f2) {
                if (curBoard.getSquare(yCor, xCor-colMan).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor, xCor-colMan).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, xCor-colMan));
                    }
                    f2 = false; // Napotkano inną figurę, koniec ruchu.
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, xCor-colMan));
                }
            }
            colMan++;
        }

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
     * Reprezentacja tekstowa wieży.
     * @return 'R' dla białej wieży i 'r' dla czarnej.
     */
    @Override
    public String toString(){
        if (isWhite) return "R";
        else return "r";
    }
}
