package Chess;

import Chess.Game.GameLayer;
import Chess.Game.Main;

import java.io.*;

/**
 * Klasa obsługująca operacje zapisu i odczytu stanu gry. Zawiera jedynie metody statyczne. Nie ma konieczności inicjalizowania obiektu.
 */
public class FileHandler {

    /**
     * Metoda zapisująca stan gry do pliku.
     * @param filePath Podana ścieżka pliku.
     */
    public static void saveCurrentGame(String filePath){
        assert filePath != null;
        assert !filePath.isEmpty();
        try {
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(Main.getCurrentGame());
            out.close();
            fileOut.close();
        } catch (IOException i) {
            System.err.println("Wystąpił problem z zapisem do pliku.");
            i.printStackTrace();
        }
    }

    /**
     * Metoda wczytująca stan gry z pliku.
     * @param filePath Plik z zapisanym stanem gry.
     */
    public static void loadGame(String filePath){
        assert filePath != null;
        assert !filePath.isEmpty();
        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            GameLayer game = (GameLayer) in.readObject();
            Main.setCurrentGame(game);
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.err.println("Wystąpił problem z odczytem z pliku.");
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.err.println("Klasa GameLayer nieznaleziona.");
            c.printStackTrace();
        }
    }
}
