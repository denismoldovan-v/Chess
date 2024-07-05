package Chess.Pieces;

import Chess.Game.Board;
import Chess.Game.Main;
import Chess.Game.Square;

import java.util.ArrayList;
import java.util.List;
/**
 * Klasa reprezentująca figurę skoczka.
 * Skoczek porusza się w charakterystyczny sposób "L" (dwa pola w jednym kierunku i jedno w kierunku prostopadłym).
 */
public class Knight extends Piece {
    public final static int IMPORTANCE = 4;
    private static final String WHITE_IMAGE_PATH = "rsc/knight_w.png";
    private static final String BLACK_IMAGE_PATH = "rsc/knight_b.png";

    /**
     * Konstruktor klasy skoczka, przypisuje skoczkowi początkową pozycję i kolor.
     * @param occupyingSquare Pole, na którym umieszczony jest skoczek.
     * @param isWhite Czy skoczek jest biały (true) czy czarny (false).
     */
    public Knight(Square occupyingSquare, Boolean isWhite) {
        super(occupyingSquare, isWhite, WHITE_IMAGE_PATH, BLACK_IMAGE_PATH);
    }
    /**
     * Generuje listę wszystkich legalnych ruchów dla skoczka z jego obecnej pozycji.
     * Skoczek może skoczyć na osiem możliwych pozycji kształtem "L".
     * @return Lista pól, na które skoczek może legalnie się przemieścić.
     */
    @Override
    public List<Square> getLegalMoves(Square square) {
        List<Square> legalMoves = new ArrayList<>();
        int xCor = square.getCol();
        int yCor = square.getRow();
        Board curBoard = Main.getCurrentGame().getBoard();

        //Ruch do dołu
        if (yCor+2 < 8){
            //I w prawo
            if(xCor+1 < 8) {
                if (curBoard.getSquare(yCor + 2, xCor + 1).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor + 2, xCor + 1).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + 2, xCor + 1));
                    }
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + 2, xCor + 1));
                }
            }
            //I w lewo
            if(xCor-1 >= 0) {
                if (curBoard.getSquare(yCor + 2, xCor - 1).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor + 2, xCor - 1).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + 2, xCor - 1));
                    }
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + 2, xCor - 1));
                }
            }
        }

        //Ruch w górę
        if (yCor-2 >= 0){
            //I w prawo
            if(xCor+1 < 8) {
                if (curBoard.getSquare(yCor - 2, xCor + 1).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor - 2, xCor + 1).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - 2, xCor + 1));
                    }
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - 2, xCor + 1));
                }
            }
            //I w lewo
            if(xCor-1 >= 0) {
                if (curBoard.getSquare(yCor - 2, xCor - 1).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor - 2, xCor - 1).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - 2, xCor - 1));
                    }
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - 2, xCor - 1));
                }
            }
        }

        //Ruch w prawo
        if (xCor+2 < 8){
            //I w dół
            if(yCor+1 < 8) {
                if (curBoard.getSquare(yCor + 1, xCor + 2).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor + 1, xCor + 2).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + 1, xCor + 2));
                    }
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + 1, xCor + 2));
                }
            }
            //I w górę
            if(yCor-1 >= 0) {
                if (curBoard.getSquare(yCor - 1, xCor + 2).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor - 1, xCor + 2).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - 1, xCor + 2));
                    }
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - 1, xCor + 2));
                }
            }
        }

        //Ruch w lewo
        if (xCor-2 >=0){
            //I w dół
            if(yCor+1 < 8) {
                if (curBoard.getSquare(yCor + 1, xCor - 2).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor + 1, xCor - 2).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + 1, xCor - 2));
                    }
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor + 1, xCor - 2));
                }
            }
            //I w górę
            if(yCor-1 >= 0) {
                if (curBoard.getSquare(yCor - 1, xCor - 2).getOccupyingPiece() != null) {
                    if (curBoard.getSquare(yCor - 1, xCor - 2).getOccupyingPiece().isWhite() != this.isWhite) {
                        legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - 1, xCor - 2));
                    }
                } else {
                    legalMoves.add(Main.getCurrentGame().getBoard().getSquare(yCor - 1, xCor - 2));
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
     * Reprezentacja tekstowa skoczka.
     * @return 'K' dla białego skoczka i 'k' dla czarnego.
     */
    @Override
    public String toString(){
        if (isWhite) return "K";
        else return "k";
    }
}
