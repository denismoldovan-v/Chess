package Chess.Pieces;

import Chess.Game.Board;
import Chess.Game.Main;
import Chess.Game.Square;

import java.util.ArrayList;
import java.util.List;
/**
 * Klasa reprezentująca figurę króla. Król może poruszać się o jedno pole
 * w każdym kierunku i jest jedyną figurą mogącą wykonać roszadę.
 */
public class King extends Piece {
    private boolean isCastlingAllowed; // Flaga określająca, czy roszada jest możliwa
    public final static int IMPORTANCE = 10;
    private static final String WHITE_IMAGE_PATH = "rsc/king_w.png";
    private static final String BLACK_IMAGE_PATH = "rsc/king_b.png";

    /**
     * Konstruktor klasy King inicjalizujący króla na danym polu z określonym kolorem.
     * @param occupyingSquare Pole, na którym znajduje się król.
     * @param isWhite Flaga określająca, czy król jest białą figurą.
     */
    public King(Square occupyingSquare, Boolean isWhite) {
        super(occupyingSquare, isWhite, WHITE_IMAGE_PATH, BLACK_IMAGE_PATH);
        isCastlingAllowed = true; // Domyślnie roszada jest możliwa
    }
    /**
     * Metoda uniemożliwiająca wykonanie roszady.
     */
    public void disallowCastling(){
        isCastlingAllowed = false;
    }
    /**
     * Metoda zwracająca informację, czy roszada jest możliwa.
     * @return Prawda, jeśli roszada jest dozwolona, w przeciwnym razie fałsz.
     */
    public Boolean isCastlingAllowed(){
        return isCastlingAllowed;
    }
    /**
     * Zwraca listę legalnych ruchów króla z jego obecnej pozycji.
     * @return Lista możliwych do wykonania ruchów.
     */
    @Override
    public List<Square> getLegalMoves(Square square) {
        int xCor = square.getCol(); // Aktualna kolumna króla
        int yCor = square.getRow(); // Aktualny rząd króla
        List<Square> legalMoves = new ArrayList<>(); // Lista możliwych do wykonania ruchów
        Board curBoard = Main.getCurrentGame().getBoard(); // Aktualna plansza gry


        //Ruch w prawo
        if (xCor+1 < 8){
            if (curBoard.getSquare(yCor, xCor+1).getOccupyingPiece() != null) {
                if (curBoard.getSquare(yCor, xCor + 1).getOccupyingPiece().isWhite() != this.isWhite) {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, xCor + 1));
                }
            }else{
                legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, xCor + 1));
            }
            //Ruch w prawo i w górę
            if(yCor-1 >= 0) {
                if (curBoard.getSquare(yCor-1, xCor + 1).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor-1, xCor + 1).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor-1, xCor + 1));
                    }
                }else{
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor-1, xCor + 1));
                }
            }
        }
        //Ruch w lewo
        if (xCor-1 >= 0){
            if (curBoard.getSquare(yCor, xCor-1).getOccupyingPiece() != null) {
                if (curBoard.getSquare(yCor, xCor - 1).getOccupyingPiece().isWhite() != this.isWhite) {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, xCor - 1));
                }
            }else{
                legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, xCor - 1));
            }
            //Ruch w lewo i w dół
            if(yCor+1 <8) {
                if (curBoard.getSquare(yCor+1, xCor - 1).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor+1, xCor - 1).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor+1, xCor - 1));
                    }
                }else{
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor+1, xCor - 1));
                }
            }
        }
        //Ruch w dół
        if (yCor+1 < 8){
            if (curBoard.getSquare(yCor+1, xCor).getOccupyingPiece() != null) {
                if (curBoard.getSquare(yCor+1, xCor).getOccupyingPiece().isWhite() != this.isWhite) {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor+1, xCor));
                }
            }else{
                legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor+1, xCor));
            }
            //Ruch w dół i w prawo
            if(xCor+1 < 8) {
                if (curBoard.getSquare(yCor+1, xCor +1).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor+1, xCor + 1).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor+1, xCor + 1));
                    }
                }else{
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor+1, xCor + 1));
                }
            }
        }
        //Ruch w górę
        if (yCor-1>= 0){
            if (curBoard.getSquare(yCor-1, xCor).getOccupyingPiece() != null) {
                if (curBoard.getSquare(yCor-1, xCor).getOccupyingPiece().isWhite() != this.isWhite) {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor-1, xCor));
                }
            }else{
                legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor-1, xCor));
            }
            //Ruch w górę i w lewo
            if(xCor-1 >= 0) {
                if (curBoard.getSquare(yCor-1, xCor -1).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor-1, xCor - 1).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor-1, xCor - 1));
                    }
                }else{
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor-1, xCor - 1));
                }
            }
        }
        //Roszada
        if(isCastlingAllowed){
            //Krótka roszada
            if (curBoard.getSquare(yCor, 5).getOccupyingPiece() == null && curBoard.getSquare(yCor, 6).getOccupyingPiece() == null){
                if (curBoard.getSquare(yCor, 7).getOccupyingPiece() instanceof Rook){
                    if (((Rook) curBoard.getSquare(yCor, 7).getOccupyingPiece()).isCastlingAllowed()){
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, 7));
                    }
                }
            }
            //Długa roszada
            if (curBoard.getSquare(yCor, 3).getOccupyingPiece() == null && curBoard.getSquare(yCor, 2).getOccupyingPiece() == null && curBoard.getSquare(yCor, 1).getOccupyingPiece() == null){
                if (curBoard.getSquare(yCor, 0).getOccupyingPiece() instanceof Rook){
                    if (((Rook) curBoard.getSquare(yCor, 0).getOccupyingPiece()).isCastlingAllowed()){
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, 0));
                    }
                }
            }
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
     * Reprezentacja tekstowa króla.
     * @return 'A' dla białego króla i 'a' dla czarnego.
     */
    @Override
    public String toString(){
        if (isWhite) return "A";
        else return "a";
    }
}
