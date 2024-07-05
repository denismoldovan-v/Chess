package Chess.Game;

import Chess.Pieces.*;

import java.io.Serializable;
import java.util.*;

/**
 * Klasa obsługująca planszę do gry. Zawiera planszę oraz listę elementów pozostałych na niej.
 */
public class Board implements Serializable{

    public final Integer BOARDSIZE = 8;

    private Square[][] squares = new Square[BOARDSIZE][BOARDSIZE];
    private final List<Piece> blackPieces;
    private final List<Piece> whitePieces;

    /**
     * Konstruktor inicjalizujący planszę. Wszystkie pola i figury.
     */
    public Board(){
        /*Inicjalizacja planszy*/
        Boolean isWhite = false;
        for (int row = 0; row < BOARDSIZE; row++){
            for(int col = 0; col < BOARDSIZE; col++){
                if(row == 0 || row == 7){
                    /*Pierwszy i ostatni rząd (figury)*/
                    squares[row][0] = new Square(row, 0);
                    squares[row][7] = new Square(row, 7);
                    squares[row][0].putPiece(new Rook(squares[row][0], isWhite));
                    squares[row][7].putPiece(new Rook(squares[row][7], isWhite));
                    squares[row][1] = new Square(row, 1);
                    squares[row][6] = new Square(row, 6);
                    squares[row][1].putPiece(new Knight(squares[row][1], isWhite));
                    squares[row][6].putPiece(new Knight(squares[row][6], isWhite));
                    squares[row][2] = new Square(row, 2);
                    squares[row][5] = new Square(row, 5);
                    squares[row][2].putPiece(new Bishop(squares[row][2], isWhite));
                    squares[row][5].putPiece(new Bishop(squares[row][5], isWhite));
                    squares[row][3] = new Square(row, 3);
                    squares[row][4] = new Square(row, 4);
                    squares[row][3].putPiece(new Queen(squares[row][3], isWhite));
                    squares[row][4].putPiece(new King(squares[row][4], isWhite));
                    col = 8;
                } else if (row == 1 || row == 6) {
                    /*Drugi i przedostatni rząd (piony)*/
                    squares[row][col] = new Square(row, col);
                    squares[row][col].putPiece(new Pawn(squares[row][col], isWhite));
                    if(col == 7) isWhite = true;
                } else {
                    /*Pozostałe pola*/
                    squares[row][col] = new Square(row, col);
                }
            }
        }

        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        /*Dodawanie figur do list*/
        for (int col = 0; col < BOARDSIZE; col++){
            addPiece(squares[0][col].getOccupyingPiece());
            addPiece(squares[1][col].getOccupyingPiece());
            addPiece(squares[6][col].getOccupyingPiece());
            addPiece(squares[7][col].getOccupyingPiece());
        }
    }

    /**
     * Konstruktor służący do kopiowania instancji klasy Board.
     * @param other Instancja klasy board, która ma być skopiowana.
     */
    public Board(Board other){
        this.whitePieces = new ArrayList<>(other.getWhitePieces());
        this.blackPieces = new ArrayList<>(other.getBlackPieces());
        this.squares = new Square[other.BOARDSIZE][other.BOARDSIZE];
        for (int i = 0; i < other.BOARDSIZE ; i++) {
            for (int j = 0; j < other.BOARDSIZE ; j++) {
                squares[i][j] = new Square(other.getSquare(i,j));
            }
        }
    }

    /**
     * Metoda wdrażająca przesłany ruch.
     *
     * @param move Ruch do wykonania.
     */
    public void makeMove(Move move)
    {
        assert move != null;

        Piece piece = move.getPiece();
        Square from = move.getFrom();
        Square to = move.getTo();

        //określenie ataku
        if(to.getOccupyingPiece()!=null) {
            move.setCapture();
            removePiece(to.getOccupyingPiece());
        }

        //En passant
        if (piece instanceof Pawn && to.getOccupyingPiece()==null && !Objects.equals(from.getRow(), to.getRow()) && !Objects.equals(from.getCol(), to.getCol())){
            removePiece(squares[from.getRow()][to.getCol()].getOccupyingPiece());
            squares[from.getRow()][to.getCol()].removeOccupyingPiece();
        }

        //Wykonanie ruchu
        piece.setOccupyingSquare(squares[to.getRow()][to.getCol()]);
        squares[to.getRow()][to.getCol()].putPiece(piece);
        squares[from.getRow()][from.getCol()].removeOccupyingPiece();

    }

    /**
     * Metoda wdrażająca roszadę.
     *
     * @param move Ruch do wykonania.
     */
    public void makeCastling(Move move)
    {
        assert move != null;

        Piece piece = move.getPiece();
        Square from = move.getFrom();
        Square to = move.getTo();
        int row = to.getRow();

        //Długa roszada
        if (to.getCol() == 0){
            //Król
            piece.setOccupyingSquare(squares[row][2]);
            squares[row][2].putPiece(piece);
            squares[row][from.getCol()].removeOccupyingPiece();
            ((King) piece).disallowCastling();

            //Wieża
            squares[row][0].getOccupyingPiece().setOccupyingSquare(squares[row][3]);
            squares[row][3].putPiece(squares[row][0].getOccupyingPiece());
            squares[row][0].removeOccupyingPiece();
            ((Rook) squares[row][3].getOccupyingPiece()).disallowCastling();
        }
        //Krótka roszada
        else{
            //Król
            piece.setOccupyingSquare(squares[row][6]);
            squares[row][6].putPiece(piece);
            squares[row][from.getCol()].removeOccupyingPiece();
            ((King) piece).disallowCastling();

            //Wieża
            squares[row][7].getOccupyingPiece().setOccupyingSquare(squares[row][5]);
            squares[row][5].putPiece(squares[row][7].getOccupyingPiece());
            squares[row][7].removeOccupyingPiece();
            ((Rook) squares[row][5].getOccupyingPiece()).disallowCastling();
        }

    }

    /**
     * Metoda promująca piona na wybraną figurę
     * @param square Pole na którym zachodzi promocja
     * @param piece Figura żądana przez gracza
     * @param isWhite Kolor figury
     */
    public void promote(Square square, Piece piece, boolean isWhite){

        switch (piece) {
            case Queen _ -> {
                squares[square.getRow()][square.getCol()].removeOccupyingPiece();
                squares[square.getRow()][square.getCol()].putPiece(new Queen(squares[square.getRow()][square.getCol()], isWhite));
            }
            case Bishop _ -> {
                squares[square.getRow()][square.getCol()].removeOccupyingPiece();
                squares[square.getRow()][square.getCol()].putPiece(new Bishop(squares[square.getRow()][square.getCol()], isWhite));
            }
            case Knight _ -> {
                squares[square.getRow()][square.getCol()].removeOccupyingPiece();
                squares[square.getRow()][square.getCol()].putPiece(new Knight(squares[square.getRow()][square.getCol()], isWhite));
            }
            case Rook _ -> {
                squares[square.getRow()][square.getCol()].removeOccupyingPiece();
                squares[square.getRow()][square.getCol()].putPiece(new Rook(squares[square.getRow()][square.getCol()], isWhite));
            }
            case null, default -> throw new RuntimeException("Unrecognized piece type");
        }
    }

    /**
     * Metoda pobierająca pole o podanych koordynatach.
     * @param row Rząd pola.
     * @param col Kolumna pola.
     * @return Pole o podanych koordynatach.
     */
    public Square getSquare(Integer row, Integer col){
        if (row < 0 || row >= BOARDSIZE || col < 0 || col >= BOARDSIZE) throw new IllegalArgumentException("Koordynaty poza planszą.");
        return squares[row][col];
    }

    /**
     * Metoda zwracająca planszę w formie tablicy pól.
     * @return Tablica pól.
     */
    public Square[][] getSquares() {
        return squares;
    }

    /**
     * Metoda zwracająca listę pozostałych, czarnych elementów.
     * @return Lista czarnych elementów.
     */
    public List<Piece> getBlackPieces() {return blackPieces;}

    /**
     * Metoda zwracająca listę pozostałych, białych elementów.
     * @return Lista białych elementów.
     */
    public List<Piece> getWhitePieces() {return whitePieces;}

    /**
     * Metoda zwracająca jedyną instancję klasy King.
     * @param pieces Lista zawierająca figury gracza.
     * @return Instancja klasy king.
     */
    public King getKing(List<Piece> pieces){
        King king = null;
        for (Piece piece : pieces){
            if (piece instanceof King){
                king = (King) piece;
            }
        }
        return king;
    }

    /**
     * Metoda usuwająca wszystkie figury z planszy (stworzona w celu testów).
     */
    public void removePieces(){
        for (int i = 0; i < BOARDSIZE ; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                if(squares[i][j].isOccupied()){
                    squares[i][j].removeOccupyingPiece();
                }
            }
        }
        whitePieces.removeIf(Piece -> Piece != null);
        blackPieces.removeIf(Piece -> Piece != null);
    }

    /**
     * Metoda usuwająca figurę stojącą na danym polu.
     * @param square Pole, na którym stoi figura.
     */
    public void removePiece(Square square){
        squares[square.getRow()][square.getCol()].removeOccupyingPiece();
    }

    /**
     * Metoda odpowiadająca za usuwanie figury z listy figur.
     * @param piece Figura do usunięcia.
     */
    public void removePiece(Piece piece){
        if(piece.isWhite()){
            whitePieces.remove(piece);
        }
        else{
            blackPieces.remove(piece);
        }
    }

    /**
     * Metoda, którą dodaje się figurę do planszy (do odpowiedniej listy).
     * @param piece Figura.
     */
    public void addPiece(Piece piece){
        if(piece.isWhite()){
            whitePieces.add(piece);
            whitePieces.sort((Comparator.naturalOrder()));
        }
        else{
            blackPieces.add(piece);
            blackPieces.sort((Comparator.naturalOrder()));
        }
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(BOARDSIZE, blackPieces, whitePieces);
        result = 31 * result + Arrays.hashCode(squares);
        return result;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("|");
        for (int row = 0; row < BOARDSIZE; row++){
            for(int col = 0; col < BOARDSIZE; col++){
                Piece piece = squares[row][col].getOccupyingPiece();
                if(piece == null)sb.append(" |");
                else sb.append(piece).append("|");
            }
            if(row == BOARDSIZE-1) break;
            sb.append("\n").append("-".repeat(Math.max(0, 1 + BOARDSIZE * 2))).append("\n|");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        for (int i = 0; i < board.BOARDSIZE ; i++) {
            for (int j = 0; j < board.BOARDSIZE ; j++) {
                if(board.getSquare(i,j).getOccupyingPiece() != this.getSquare(i,j).getOccupyingPiece()){return false;}
            }
        }
        return true;
    }
    // Method to get all legal moves for a piece at a given position
    public List<Square> getLegalMovesForPiece(int row, int col) {
        Piece piece = this.squares[row][col].getOccupyingPiece();
        if (piece != null) {
            return piece.getLegalMoves(this.squares[row][col]);
        }
        return Collections.emptyList();
    }

}

