package Network;

import Chess.Game.Board;
import Chess.Game.GameLayer;
import Chess.Game.Move;
import com.sun.jdi.InvalidTypeException;

import java.io.Serializable;

/**
 * Klasa obwijająca dane do przesłania, coby łatwiej zaimplementować odbieranie różnych typów danych.
 */
public class DataWrapper implements Serializable {
    private DataTypes type;
    private Pair<String, Integer> information;
    private String text;
    private Move move;
    private Board board;
    private GameLayer game;

    /**
     * Możliwe typy do przesłania.
     */
    public enum DataTypes{
        NETINFO,
        DISCONNECT,
        MOVE,
        TEXT,
        BOARD,
        GIVEUP,
        GAME
    }

    /**
     * Metoda obwijająca wiadomość tekstową (jak na czacie).
     * @param text Wiadomość.
     */
    public void wrapText(String text){
        type = DataTypes.TEXT;
        this.text = text;
    }

    /**
     * Metoda obwijająca informacje o sieci.
     * @param netInfo Para [String, Integer] -> [Adres IP, Port]
     */
    public void wrapNetInfo(Pair<String, Integer> netInfo){
        type = DataTypes.NETINFO;
        information = netInfo;
    }

    /**
     * Metoda obwijająca ruch.
     * @param move Ruch do przesłania.
     */
    public void wrapMove(Move move){
        type = DataTypes.MOVE;
        this.move = move;
    }

    /**
     * Metoda obwijająca planszę.
     * @param board Plansza do przesłania.
     */
    public void wrapBoard(Board board){
        type = DataTypes.BOARD;
        this.board = board;
    }

    /**
     * Metoda obwijająca grę.
     * @param game Gra do przesłania.
     */
    public void wrapGame(GameLayer game){
        type = DataTypes.GAME;
        this.game = game;
    }

    /**
     * Metoda obwijająca informację o rozłączeniu.
     */
    public void wrapDisconnectionInfo(){
        type = DataTypes.DISCONNECT;
    }

    /**
     * Metoda obwijająca informację o poddaniu się.
     */
    public void wrapGiveUpInfo(){
        type = DataTypes.GIVEUP;
    }

    /**
     * @return Typ obwiniętych danych.
     */
    public DataTypes getType(){
        return type;
    }

    /**
     * @return Ruch.
     * @throws InvalidTypeException Przy wywołaniu przy złym typie danych.
     */
    public Move getMove() throws InvalidTypeException {
        if(type == DataTypes.MOVE) {
            return move;
        }else throw new InvalidTypeException("Typem zmiennej przechowywanej jest: " + type);
    }

    /**
     * @return Informacje sieciowe.
     * @throws InvalidTypeException Przy wywołaniu przy złym typie danych.
     */
    public Pair<String, Integer> getNetInfo() throws InvalidTypeException {
        if(type == DataTypes.NETINFO) {
            return information;
        }else throw new InvalidTypeException("Typem zmiennej przechowywanej jest: " + type);
    }

    /**
     * @return Wiadomość.
     * @throws InvalidTypeException Przy wywołaniu przy złym typie danych.
     */
    public String getText() throws InvalidTypeException {
        if(type == DataTypes.TEXT) {
            return text;
        }else throw new InvalidTypeException("Typem zmiennej przechowywanej jest: " + type);
    }

    /**
     * @return Plansza.
     * @throws InvalidTypeException Przy wywołaniu przy złym typie danych.
     */
    public Board getBoard() throws InvalidTypeException {
        if(type == DataTypes.BOARD) {
            return board;
        }else throw new InvalidTypeException("Typem zmiennej przechowywanej jest: " + type);
    }

    /**
     * @return Gra.
     * @throws InvalidTypeException Przy wywołaniu przy złym typie danych.
     */
    public GameLayer getGame() throws InvalidTypeException {
        if(type == DataTypes.GAME) {
            return game;
        }else throw new InvalidTypeException("Typem zmiennej przechowywanej jest: " + type);
    }

    /**
     * @return Prawda - potwierdzenie informacji o rozłączeniu.
     * @throws InvalidTypeException Przy wywołaniu przy złym typie danych.
     */
    public Boolean getDisconnectionInfo() throws InvalidTypeException {
        if(type == DataTypes.DISCONNECT) {
            return true;
        }else throw new InvalidTypeException("Typem zmiennej przechowywanej jest: " + type);
    }

    /**
     * @return Prawda - potwierdzenie informacji o rozłączeniu.
     * @throws InvalidTypeException Przy wywołaniu przy złym typie danych.
     */
    public Boolean getGiveUpInfo() throws InvalidTypeException {
        if(type == DataTypes.GIVEUP) {
            return true;
        }else throw new InvalidTypeException("Typem zmiennej przechowywanej jest: " + type);
    }
}
