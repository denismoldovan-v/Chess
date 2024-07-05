package Chess.Pieces;

import Chess.Game.Board;
import Chess.Game.Main;
import Chess.Game.Square;

import java.util.ArrayList;
import java.util.List;
/**
 * Klasa reprezentująca figurę hetmana.
 * Hetman może poruszać się w poziomie, pionie oraz na skos w każdym kierunku, na dowolną liczbę pól, dopóki nie napotka na inną figurę lub krawędź planszy.
 */
public class Queen extends Piece {
    public final static int IMPORTANCE = 9;
    private static final String WHITE_IMAGE_PATH = "rsc/queen_w.png";
    private static final String BLACK_IMAGE_PATH = "rsc/queen_b.png";
    /**
     * Konstruktor klasy hetmana.
     * @param occupyingSquare Pole, na którym początkowo znajduje się hetman.
     * @param isWhite Informacja, czy hetman jest białą (true) czy czarną (false) figurą.
     */
    public Queen(Square occupyingSquare, Boolean isWhite) {
        super(occupyingSquare, isWhite, WHITE_IMAGE_PATH, BLACK_IMAGE_PATH);
    }
    /**
     * Zwraca listę wszystkich legalnych ruchów hetmana z jego aktualnego położenia.
     * @return Lista pól, na które hetman może się przemieścić.
     */
    @Override
    public List<Square> getLegalMoves(Square square) {
        int xCor = square.getCol();
        int yCor = square.getRow();
        List<Square> legalMoves = new ArrayList<>();

        Board curBoard = Main.getCurrentGame().getBoard();
        // Wyznaczenie ruchów w pionie
        int rowMan = 1;
        boolean f1, f2, f3, f4;
        f1 = true;
        f2 = true;
        while(rowMan < 8){
            if(yCor+rowMan > 7) f1 = false;
            if(yCor-rowMan < 0) f2 = false;
            // Do dołu
            if(f1) {
                if (curBoard.getSquare(yCor + rowMan, xCor).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor + rowMan, xCor).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + rowMan, xCor));
                    }
                    f1 = false;
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + rowMan, xCor));
                }
            }
            // Do góry
            if(f2) {
                if (curBoard.getSquare(yCor - rowMan, xCor).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor - rowMan, xCor).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - rowMan, xCor));
                    }
                    f2 = false;
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - rowMan, xCor));
                }
            }
            rowMan++;
        }

        // Wyznaczanie ruchów w poziomie
        int colMan = 1;
        f1 = true;
        f2 = true;
        while(colMan < 8){
            if(xCor+colMan > 7) f1 = false;
            if(xCor-colMan < 0) f2 = false;
            // W prawo
            if(f1) {
                if (curBoard.getSquare(yCor, xCor+colMan).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor, xCor+colMan).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, xCor+colMan));
                    }
                    f1 = false;
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, xCor+colMan));
                }
            }
            // W lewo
            if(f2) {
                if (curBoard.getSquare(yCor, xCor-colMan).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor, xCor-colMan).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, xCor-colMan));
                    }
                    f2 = false;
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor, xCor-colMan));
                }
            }
            colMan++;
        }
        // Wyznaczanie ruchów na skos
        rowMan = 1;
        colMan = 1;
        f1 = true;
        f2 = true;
        f3 = true;
        f4 = true;
        while(rowMan < 8){
            if(yCor+rowMan > 7 || xCor+colMan > 7) f1 = false;
            if(yCor-rowMan < 0 || xCor+colMan > 7) f2 = false;
            if(yCor+rowMan > 7 || xCor-colMan < 0) f3 = false;
            if(yCor-rowMan < 0 || xCor-colMan < 0) f4 = false;
            // W prawo do dołu
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
            // W prawo do góry
            if(f2) {
                if (curBoard.getSquare(yCor - rowMan, xCor+colMan).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor - rowMan, xCor+colMan).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - rowMan, xCor+colMan));
                    }
                    f2 = false;
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - rowMan, xCor+colMan));
                }
            }
            // W lewo do dołu
            if(f3) {
                if (curBoard.getSquare(yCor+rowMan, xCor - colMan).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor+rowMan, xCor - colMan).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor+rowMan, xCor - colMan));
                    }
                    f3 = false;
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor+rowMan, xCor - colMan));
                }
            }
            // W lewo do góry
            if(f4) {
                if (curBoard.getSquare(yCor - rowMan, xCor-colMan).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor - rowMan, xCor-colMan).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - rowMan, xCor-colMan));
                    }
                    f4 = false;
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - rowMan, xCor-colMan));
                }
            }
            colMan++;
            rowMan++;
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
     * Reprezentacja tekstowa hetmana.
     * @return 'Q' dla białego hetmana i 'q' dla czarnego.
     */
    @Override
    public String toString(){
        if (isWhite) return "Q";
        else return "q";
    }
}
