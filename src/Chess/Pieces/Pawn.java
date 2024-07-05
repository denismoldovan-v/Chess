package Chess.Pieces;

import Chess.Game.Board;
import Chess.Game.Main;
import Chess.Game.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca piona.
 * Pion porusza się o jedno pole do przodu i bije na skos, też o jedno pole. Może się także poruszyć o dwa pola, przy pierwszym ruchu, wykonać bicie En Passant oraz promować.
 */
public class Pawn extends Piece {
    boolean isFirstMove = true;
    boolean canGetPassed = false;
    public final static int IMPORTANCE = 1;
    private static final String WHITE_IMAGE_PATH = "rsc/pawn_w.png";
    private static final String BLACK_IMAGE_PATH = "rsc/pawn_b.png";

    /**
     * Konstruktor klasy Pawn.
     * @param occupyingSquare Pole zajmowane przez pionek.
     * @param isWhite Kolor piona. True - biały.
     */
    public Pawn(Square occupyingSquare, Boolean isWhite) {
        super(occupyingSquare, isWhite, WHITE_IMAGE_PATH, BLACK_IMAGE_PATH);
    }

    /**
     * Metoda ustawiająca informację na temat tego, czy pion może ulec atakowi En Passant.
     * @param canGetPassed True - może ulec. False - nie.
     */
    public void setCanGetPassed(boolean canGetPassed) {
        this.canGetPassed = canGetPassed;
    }

    /**
     * Metoda zwracająca informację nt. tego, czy poprzedni ruch był pierwszym dla piona.
     * @return True - tak.
     */
    public boolean getIsFirstMove() {
        return isFirstMove;
    }

    /**
     * Metoda zwracająca informację na temat tego, czy pion może ulec atakowi En Passant.
     * @return True - tak.
     */
    public boolean getCanGetPassed() {
        return canGetPassed;
    }

    /**
     * Metoda określająca, że wykonany został drugi (kolejny) ruch piona.
     */
    public void makedMove(){
        isFirstMove = false;
    }

    @Override
    public List<Square> getLegalMoves(Square square) {
        int xCor = square.getRow();
        int yCor = square.getCol();
        Board curBoard = Main.getCurrentGame().getBoard();
        List<Square> legalMoves = new ArrayList<>();

        //Sprawdz kolor piona
        if (!this.isWhite) {
            //Sprawdz mozliwe ruchy za pierwszym ruchem
            if (isFirstMove) {
                //Dwa pola wolne
                if (xCor + 1 < 8 && xCor + 2 < 8 && curBoard.getSquare(xCor + 1, yCor).getOccupyingPiece() == null && curBoard.getSquare(xCor+2, yCor).getOccupyingPiece() == null) {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor+2, yCor));
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor+1, yCor));
                }//Jedno pole wolne
                else {
                    if (xCor + 1 < 8 && xCor + 2 < 8 && curBoard.getSquare(xCor+1, yCor ).getOccupyingPiece() == null && curBoard.getSquare(xCor+2, yCor).getOccupyingPiece() != null) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor+1, yCor));
                    }
                }
            } //Ruch o jedno pole niebedacy pierwszym ruchem. Wymaga sprawdzenia limitu planszy
            else {
                if (xCor + 1 < 8 && curBoard.getSquare(xCor+1, yCor).getOccupyingPiece() == null) {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor+1, yCor));
                }
            }
            //Bicia "klasyczne"
            if (xCor+1>=0 && yCor+1<8 && xCor+1<8 && yCor+1>=0 && curBoard.getSquare(xCor+1, yCor + 1).getOccupyingPiece() != null && curBoard.getSquare(xCor+1, yCor + 1).getOccupyingPiece().isWhite){
                legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor+1, yCor + 1));
            }
            if (xCor+1>=0 && yCor-1<8 && xCor+1<8 && yCor-1>=0 && curBoard.getSquare(xCor+1, yCor - 1).getOccupyingPiece() != null && curBoard.getSquare(xCor+1, yCor - 1).getOccupyingPiece().isWhite) {
                legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor+1, yCor - 1));
            }
            //En Passant
            if ( xCor+1>=0 && yCor-1<8 && xCor+1<8 && yCor-1>=0 && curBoard.getSquare(xCor+1, yCor - 1).getOccupyingPiece() == null && curBoard.getSquare(xCor, yCor - 1).getOccupyingPiece() instanceof Pawn && curBoard.getSquare(xCor, yCor - 1).getOccupyingPiece().isWhite && ((Pawn) curBoard.getSquare(xCor, yCor - 1).getOccupyingPiece()).getCanGetPassed()){
                legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor+1, yCor - 1));
            }
            if (  xCor+1>=0 && yCor+1<8 && xCor+1<8 && yCor+1>=0 && curBoard.getSquare(xCor+1, yCor + 1).getOccupyingPiece() == null && curBoard.getSquare(xCor, yCor + 1).getOccupyingPiece() instanceof Pawn && curBoard.getSquare(xCor, yCor + 1).getOccupyingPiece().isWhite && ((Pawn) curBoard.getSquare(xCor, yCor + 1).getOccupyingPiece()).getCanGetPassed()){
                legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor+1, yCor + 1));
            }
        }
        //Analogicznie czarne piony
        else {
            if (isFirstMove) {
                    if (xCor - 1 >= 0 && xCor - 2 >= 0 && curBoard.getSquare(xCor-1, yCor ).getOccupyingPiece() == null && curBoard.getSquare(xCor-2, yCor ).getOccupyingPiece() == null) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor-2 , yCor ));
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor -1, yCor ));
                    } else if (xCor - 1 >= 0 && xCor - 2 >= 0 && curBoard.getSquare(xCor-1, yCor).getOccupyingPiece() == null && curBoard.getSquare(xCor-2, yCor ).getOccupyingPiece() != null) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor -1, yCor ));
                    }
            }
            else if (xCor - 1 >= 0 && curBoard.getSquare(xCor-1, yCor ).getOccupyingPiece() == null) {
                legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor-1 , yCor ));
            }
            if ( xCor+1>=0 && yCor+1<8 && xCor+1<8 && yCor+1>=0 && curBoard.getSquare(xCor-1, yCor + 1).getOccupyingPiece() != null && !curBoard.getSquare(xCor-1, yCor + 1).getOccupyingPiece().isWhite ){
                legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor-1, yCor + 1));
            }
            if ( xCor+1>=0 && yCor-1>=0 && xCor +1 < 8 && yCor-1<8 && curBoard.getSquare(xCor-1, yCor - 1).getOccupyingPiece() != null && !curBoard.getSquare(xCor-1, yCor - 1).getOccupyingPiece().isWhite ) {
                legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor-1, yCor - 1));
            }
            if ( xCor-1>=0 && yCor-1<8 && xCor+1<8 && yCor-1>=0 && curBoard.getSquare(xCor-1, yCor - 1).getOccupyingPiece() == null && curBoard.getSquare(xCor, yCor - 1).getOccupyingPiece() instanceof Pawn && !curBoard.getSquare(xCor, yCor - 1).getOccupyingPiece().isWhite && ((Pawn) curBoard.getSquare(xCor, yCor - 1).getOccupyingPiece()).getCanGetPassed() ){
                legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor-1, yCor - 1));
            }
            if ( xCor-1>=0 && yCor+1<8 && xCor+1<8 && yCor+1>=0 && curBoard.getSquare(xCor-1, yCor + 1).getOccupyingPiece() == null && curBoard.getSquare(xCor, yCor + 1).getOccupyingPiece() instanceof Pawn && !curBoard.getSquare(xCor, yCor + 1).getOccupyingPiece().isWhite && ((Pawn) curBoard.getSquare(xCor, yCor + 1).getOccupyingPiece()).getCanGetPassed() ){
                legalMoves.add(Main.getCurrentGame().getBoard().getSquare(xCor-1, yCor + 1));
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

    @Override
    public String toString(){
        if (isWhite) return "P";
        else return "p";
    }
}
