package Chess.Pieces;

import Chess.Game.Board;
import Chess.Game.Main;
import Chess.Game.Move;
import Chess.Game.Square;

import javax.swing.*;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
/**
 * Klasa abstrakcyjna reprezentująca generyczną figurę szachową.
 */
public abstract class Piece implements Serializable, Comparable {
    private List<Square> legalMoves; //Lista mozliwch ruchow figury
    protected Square occupyingSquare; //Pole na ktorym znajduje sie figura
    public Boolean isWhite; //Flaga okreslacjaca czy figura jest biala czy czarna
    private ImageIcon whiteImage;
    private ImageIcon blackImage;

    /**
     * Klasa sprawdzająca, czy ruchy podane przez użytkownika mogą zajść (tzn. czy nie spowodują szacha własnego króla).
     */
    public static class ProperMovesHandler{

        /**
         * Metoda zwracająca listę poprawnych ruchów.
         * @param piece Pionek, według którego ma zostać wywołana.
         * @param squares Lista możliwych ruchów.
         * @return Lista poprawnych ruchów.
         */
        //Piece.ProperMovesHandler.getProperMoves(queen, queen.getLegalMoves(queen.getOccupyingSquare()));
        public static List<Square> getProperMoves(Piece piece, List<Square> squares){
            List<Move> moves = new ArrayList<>();

            //Mapowanie ruchów
            for (Square square : squares) {
                moves.add(new Move(piece.occupyingSquare, square));
            }
            List<Square> properMoves = new ArrayList<>();

            //Tworzenie listy poprawnych ruchów
            for (Move move : moves) {
                if (!isCheck(move)) {
                    properMoves.add(move.getTo());
                }
            }
            return properMoves;
        }

        /**
         * Prywatna metoda określająca czy podany ruch spowoduje szach na własnym królu.
         * @param move Ruch.
         * @return False - jeśli ruch nie spowoduje szachu na własnym królu.
         */
        private static boolean isCheck(Move move){
            //Obecna plansza
            Board board = Main.getCurrentGame().getBoard();

            //Określanie współrzędnych ruchu
            int rowFrom = move.getFrom().getRow();
            int rowTo = move.getTo().getRow();
            int colFrom = move.getFrom().getCol();
            int colTo = move.getTo().getCol();

            //Określanie warunków niepoprawnego ruchu
            if(board.getSquare(rowFrom, colFrom).getOccupyingPiece() != null && board.getSquare(rowFrom, colFrom).getOccupyingPiece().isWhite() == !Main.getCurrentGame().isWhiteTheCurrentPlayer()) return true;
            if(board.getSquare(rowFrom, colFrom).getOccupyingPiece() == null) return true;
            if(board.getSquare(rowTo, colTo).getOccupyingPiece() != null && board.getSquare(rowTo, colTo).getOccupyingPiece().isWhite() == Main.getCurrentGame().isWhiteTheCurrentPlayer()) return true;
            if(board.getSquare(rowFrom, colFrom).getOccupyingPiece() != null && !board.getSquare(rowFrom, colFrom).getOccupyingPiece().getLegalMoves(board.getSquare(rowFrom, colFrom)).contains(board.getSquare(rowTo, colTo))) return true;

            //Wprowadzenie ruchu w życie
            Piece movPiece = board.getSquare(rowFrom, colFrom).getOccupyingPiece();
            Piece secPiece = board.getSquare(rowTo, colTo).getOccupyingPiece();

            board.getSquare(rowFrom, colFrom).removeOccupyingPiece();
            board.getSquare(rowTo, colTo).removeOccupyingPiece();

            board.getSquare(rowTo, colTo).putPiece(movPiece);

            movPiece.setOccupyingSquare(board.getSquare(rowTo, colTo));
            if (secPiece != null){
                board.removePiece(secPiece);
                secPiece.setOccupyingSquare(null);
            }

            boolean isCheck = false;
            ArrayList<Boolean> checks = isCheck(board);
            if(checks.contains(true)){
                if((checks.get(1) && !movPiece.isWhite()) || (checks.get(0) && movPiece.isWhite())) isCheck = true;
            }

            //Cofnięcie zmian
            board.getSquare(rowFrom, colFrom).removeOccupyingPiece();
            board.getSquare(rowTo, colTo).removeOccupyingPiece();

            board.getSquare(rowTo, colTo).putPiece(secPiece);
            board.getSquare(rowFrom, colFrom).putPiece(movPiece);

            movPiece.setOccupyingSquare(board.getSquare(rowFrom, colFrom));
            if (secPiece != null){
                board.addPiece(secPiece);
                secPiece.setOccupyingSquare(board.getSquare(rowTo, colTo));
            }
            return isCheck;
        }

        private static ArrayList<Boolean> isCheck(Board board) {
            King whiteKing = board.getKing(board.getWhitePieces());
            King blackKing = board.getKing(board.getBlackPieces());

            ArrayList<Boolean> checks = new ArrayList<>();
            checks.add(false);
            checks.add(false);

            // Sprawdzenie, czy gracz biały jest szachowany
            for(Piece piece : board.getBlackPieces()){
                for(Square square : piece.getLegalMoves(piece.getOccupyingSquare())){
                    if(whiteKing.getOccupyingSquare() == square){
                        checks.set(0,true);
                    }
                }
            }
            // Sprawdzenie, czy gracz czarny jest czachowany
            for(Piece piece : board.getWhitePieces()){
                for(Square square : piece.getLegalMoves(piece.getOccupyingSquare())){
                    if(blackKing.getOccupyingSquare() == square){checks.set(1,true);}
                }
            }
            return checks;
        }
    }

    /**
     * Konstruktor klasy, inicjalizujący figurę na określonym polu oraz jako określony kolor.
     * @param occupyingSquare Pole, na którym ma się znajdować figura.
     * @param isWhite Czy figura ma być biała (true) czy czarna (false).
     */
    public Piece(Square occupyingSquare, Boolean isWhite, String whiteImagePath, String blackImagePath) {
        this.occupyingSquare = occupyingSquare;
        this.isWhite = isWhite;
        this.whiteImage = new ImageIcon(new ImageIcon(whiteImagePath).getImage().getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH));
        this.blackImage = new ImageIcon(new ImageIcon(blackImagePath).getImage().getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH));
    }

    public ImageIcon getImage() {
        return isWhite ? whiteImage : blackImage;
    }

    /**
     * Ustawia nowe pole, na którym ma się znajdować figura.
     * @param occupyingSquare Nowe pole dla figury.
     */
    public void setOccupyingSquare(Square occupyingSquare) {
        this.occupyingSquare = occupyingSquare;
    }
    /**
     * Zwraca pole, na którym aktualnie znajduje się figura.
     * @return Aktualne pole figury.
     */
    public Square getOccupyingSquare() {
        return occupyingSquare;
    }
    /**
     * Usuwa figurę z pola i czyści listę jej legalnych ruchów.
     */
    public void remove(){
        occupyingSquare = null;
        legalMoves = null;
    }
    /**
     * Metoda abstrakcyjna do implementacji w klasach dziedziczących, zwracająca listę legalnych ruchów.
     * @param square Pole, według którego wykonywana jest metoda.
     * @return Lista legalnych ruchów dla figury.
     */
    public abstract List<Square> getLegalMoves(Square square);
    /**
     * Sprawdza, czy figura jest biała.
     * @return true, jeśli figura jest biała, false, jeśli czarna.
     */
    public Boolean isWhite(){
        return isWhite;
    }

    /**
     * Zwraca ważność figury używana tylko do porównań.
     * @return Ważność.
     */
    protected abstract int getImportance();
}
