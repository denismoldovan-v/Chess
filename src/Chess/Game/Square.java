package Chess.Game;

import Chess.Pieces.Piece;
import java.io.Serializable;

/**
 * Klasa obsługująca cechy pól. Zawiera informacje nt. położenia, postawionego nań elementu i koloru.
 */
public class Square implements Serializable {
    private Boolean occupied = false;
    private final Integer row;
    private final Integer col;
    private Piece occupyingPiece;
    private final boolean isWhite; // Atrybut przechowujący informację o kolorze pola

    /**
     * Konstruktor główny, który ustala wszystkie wartości, w tym automatycznie (na podstawie współrzędnych) kolor pola
     * @param row Nr wiersza.
     * @param col Nr kolumny.
     * @param piece Element postawiony na polu.
     */
    public Square(Integer row, Integer col, Piece piece) {
        this.row = row;
        this.col = col;
        this.occupyingPiece = piece;
        this.occupied = piece != null;
        this.isWhite = (row + col) % 2 != 0; // Przypisanie kolorów pól na szachownicy
    }

    /**
     * Alternatywny konstruktor dla pustego pola bez figury. Wywołuje główny konstruktor.
     * @param row Nr wiersza.
     * @param col Nr kolumny.
     */
    public Square(Integer row, Integer col) {
        this(row, col, null);
    }

    /**
     * Pusty konstruktor generujący niepoprawne (w rozumieniu planszy) pole.
     */
    public Square() {
        this(-1, -1, null);
    }

    /**
     * Konstruktor służący do kopiowania instancji klasy Square.
     * @param square Instancja klasy Square, która ma być skopiowana.
     */
    public Square(Square square) {
        this.occupied = square.isOccupied();
        this.col = square.getCol();
        this.row = square.getRow();
        this.isWhite = square.isWhite();
        this.occupyingPiece = square.getOccupyingPiece();
    }

    /**
     * Getter dla figury na polu.
     * @return Figura postawiona na polu.
     */
    public Piece getOccupyingPiece() {
        return occupyingPiece;
    }

    /**
     * Getter dla koloru pola.
     * @return True - białe pole.
     */
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * Ustawienie figury na polu.
     * @param piece Figura do ustawienia.
     */
    public void putPiece(Piece piece) {
        this.occupyingPiece = piece;
        this.occupied = true;
    }

    /**
     * Sprawdzenie, czy pole jest zajęte.
     * @return True - zajęte.
     */
    public Boolean isOccupied() {
        return occupied;
    }

    /**
     * Usunięcie figury z pola.
     */
    public void removeOccupyingPiece() {
        this.occupyingPiece = null;
        this.occupied = false;
    }

    /**
     * Getter dla rzędu pola.
     * @return Wartość całkowita określająca rząd pola. (0 - góra planszy; 7 - dół planszy)
     */
    public Integer getRow() {
        return row;
    }

    /**
     * Getter dla kolumny pola.
     * @return Wartość całkowita określająca rząd pola. (0 - lewa część planszy; 7 - prawa część planszy)
     */
    public Integer getCol() {
        return col;
    }
    public void setOccupyingPiece(Piece piece) {
    }
    // Metoda toString() zwracająca koordynaty i kolor pola
    @Override
    public String toString() {
        return "[" + row + ", " + col + "] " + (isWhite ? "White" : "Black");
    }


}
