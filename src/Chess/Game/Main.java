package Chess.Game;

import Chess.FileHandler;
import Chess.GUI.GameUI;
import Chess.GUI.MenuUI;
import Network.NetHandler;
import Network.Sender;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Objects;
import java.util.Scanner;

/**
 * Główna klasa gry. Tutaj się wszystko zaczyna...
 */
public class Main {
    public static GameLayer currentGame;
    public static NetHandler netHandler;
    public static GameUI gui;
    public static Sender sender;
    public static MenuUI menu;

    /**
     * Metoda zwracająca obecną rozgrywkę.
     *
     * @return Obecna rozgrywka.
     */
    public static GameLayer getCurrentGame() {
        return currentGame;
    }
    /**
     * Metoda ustawiająca nową rozgrywkę.
     *
     * @param game Nowa obecna rozgrywka.
     */
    public static void setCurrentGame(GameLayer game) {
        currentGame = game;

    }
    /**
     * Metoda zwracająca obecny nethandler.
     *
     * @return Obecny nethandler.
     */
    public static NetHandler getNetHandler() {
        return netHandler;
    }
    /**
     * Metoda ustawiająca nowy nethandler.
     *
     * @param netHandler Nowy nethandler.
     */
    public static void setNetHandler(NetHandler netHandler) {
        Main.netHandler = netHandler;
    }
    /**
     * Metoda zwracająca obecny sender.
     *
     * @return Obecny sender.
     */
    public static Sender getSender() {
        return sender;
    }
    /**
     * Metoda ustawiająca nowy sender.
     *
     * @param sender Nowa sender.
     */
    public static void setSender(Sender sender) {
        Main.sender = sender;
    }

    /**
     * Main o tymczasowym działaniu (test sieci). Przy uruchamianiu stworzyć dwie konfiguracje aplikacji i w tej hosta wpisać w argumenty wywołania literkę h, a w drugiej cokolwiek innego.
     * TODO: zmienić na ostateczną wersję.
     */
    public static void main(String[] args) throws Exception {
        //currentGame = new GameLayer();
        currentGame = new GameLayer();
        setCurrentGame(currentGame); // Ustawienie aktualnej gry
        gui = new GameUI();
//        fileHandlerMain();
//        try {netMain(args);} catch (IOException | InterruptedException _) {}
    }

    /*TODO: Do usunięcia na koniec*/
    private static void fileHandlerMain() throws Exception {
        boolean flag = false;

        if (flag) {
            Move move = new Move(currentGame.getBoard().getSquare(7, 1), currentGame.getBoard().getSquare(5, 0));
            currentGame.makeMove(move);
            move = new Move(currentGame.getBoard().getSquare(1, 1), currentGame.getBoard().getSquare(2, 1));
            currentGame.makeMove(move);
            move = new Move(currentGame.getBoard().getSquare(6, 1), currentGame.getBoard().getSquare(4, 1));
            currentGame.makeMove(move);
            move = new Move(currentGame.getBoard().getSquare(2, 1), currentGame.getBoard().getSquare(3, 1));
            currentGame.makeMove(move);

            FileHandler.saveCurrentGame("rsc\\game.ser");
        } else {
            System.out.println(currentGame.getBoard());

            FileHandler.loadGame("rsc\\game.ser");

            System.out.println(currentGame.getBoard());
            System.out.println(currentGame.getMoveList());
        }
    }


    /*TODO: Do usunięcia na koniec*/
    private static void botMain() throws Exception {
//        currentGame.getBoard().getSquare(5,3).putPiece(new Queen(currentGame.getBoard().getSquare(5,3), true));
//        currentGame.getBoard().getSquare(5,1).putPiece(new Queen(currentGame.getBoard().getSquare(5,1), false));
//        System.out.println(currentGame.getBoard().getSquare(6,2).getOccupyingPiece().getLegalMoves(currentGame.getBoard().getSquare(6,2)));
//        System.out.println(currentGame.getBoard());
//
//        System.exit(0);
//        currentGame.getBoard().getSquare(1, 4).removeOccupyingPiece();
//        System.out.println(currentGame.getBoard());
//
//
//        currentGame.getBoard().getSquare(1, 4).putPiece(new Bishop(currentGame.getBoard().getSquare(1, 4), false));
//        System.out.println(currentGame.getBoard().getSquare(1, 4).getOccupyingPiece());
//        System.out.println(currentGame.getBoard().getSquare(1, 4).getOccupyingPiece().getLegalMoves(currentGame.getBoard().getSquare(1, 4)));
//        System.out.println(Piece.ProperMovesHandler.getProperMoves(currentGame.getBoard().getSquare(1, 4).getOccupyingPiece(), currentGame.getBoard().getSquare(1, 4).getOccupyingPiece().getLegalMoves(currentGame.getBoard().getSquare(0, 4))));

//        currentGame.getBoard().getSquare(7, 5).removeOccupyingPiece();
//        currentGame.getBoard().getSquare(7, 6).removeOccupyingPiece();
//        System.out.println(currentGame.getBoard());
//        System.out.println(currentGame.getBoard().getSquare(7, 4).getOccupyingPiece());
//        System.out.println(currentGame.getBoard().getSquare(7, 4).getOccupyingPiece().getOccupyingSquare());
//        System.out.println(currentGame.getBoard().getSquare(7, 4).getOccupyingPiece().getLegalMoves(currentGame.getBoard().getSquare(7, 4).getOccupyingPiece().getOccupyingSquare()));
//        currentGame.makeMove(new Move(currentGame.getBoard().getSquare(7, 4), currentGame.getBoard().getSquare(7, 7)));
//        System.out.println(currentGame.getBoard());
//        System.out.println(currentGame.getMoveList());
//
//
//        System.out.println(currentGame.getBoard().getSquare(1, 0).getOccupyingPiece());
//        System.out.println(currentGame.getBoard().getSquare(1, 0).getOccupyingPiece().isWhite());
//        System.out.println(currentGame.getBoard().getSquare(1, 0).getOccupyingPiece().getOccupyingSquare());
//        System.out.println(currentGame.getBoard().getSquare(1, 0).getOccupyingPiece().getLegalMoves(currentGame.getBoard().getSquare(1, 0)));
//
//        System.out.println(currentGame.getBoard().getSquare(1, 0).getOccupyingPiece());
//        System.out.println(currentGame.getBoard().getSquare(1, 0).getOccupyingPiece().isWhite());
//        System.out.println(currentGame.getBoard().getSquare(1, 0).getOccupyingPiece().getOccupyingSquare());
//        System.out.println(currentGame.getBoard().getSquare(1, 0).getOccupyingPiece().getLegalMoves(currentGame.getBoard().getSquare(1, 0)));
//
//        System.out.println("Kto czarny (0-czlowiek)");
//        Scanner sc = new Scanner(System.in);
//        String kom = sc.next();
//        if (!kom.equals("0")) currentGame.getBot().setAsBlack();
//        else {
//            System.out.println(currentGame.getBoard());
//            int from1 = sc.nextInt();
//            int from2 = sc.nextInt();
//            int to1 = sc.nextInt();
//            int to2 = sc.nextInt();
//            Move move = new Move(currentGame.getBoard().getSquare(from1, from2), currentGame.getBoard().getSquare(to1, to2));
//            currentGame.makeMove(move);
//            System.out.println(currentGame.getBoard());
//        }

        int rounds = 8;
        while (rounds>0) {


            Move mv = currentGame.getBot().AImove(currentGame.getBoard());
            System.out.println(mv);
            System.out.println(mv.getPiece().getLegalMoves(mv.getPiece().getOccupyingSquare()));
            currentGame.makeMove(mv);
            System.out.println(currentGame.getBoard());
            currentGame.getBot().setAsBlack();


            mv = currentGame.getBot().AImove(currentGame.getBoard()); //w tym usuwany jest biskup

            System.out.println(mv.getPiece().getLegalMoves(mv.getPiece().getOccupyingSquare()));
            System.out.println(mv);
            currentGame.makeMove(mv);
            System.out.println(currentGame.getBoard());
            currentGame.getBot().setAsWhite();

            System.out.println(currentGame.getBoard().getBlackPieces());
            System.out.println(currentGame.getBoard().getWhitePieces());
//            rounds--;



//            int from1 = sc.nextInt();
//
//            if (from1 == 1800) {
//                currentGame.printMoveList();
//                System.exit(0);
//            }
//
//
//            int from2 = sc.nextInt();
//            int to1 = sc.nextInt();
//            int to2 = sc.nextInt();
//
//            Move move = new Move(currentGame.getBoard().getSquare(from1, from2), currentGame.getBoard().getSquare(to1, to2));
//            currentGame.makeMove(move);
//            System.out.println(currentGame.getBoard());
        }
        System.exit(130);
    }


    /*TODO: Do usunięcia na koniec*/
    private static void netMain(String[] args) throws Exception {
        if (Objects.equals(args[0], "h")) {
            try (final DatagramSocket socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                String hostname = socket.getLocalAddress().getHostAddress();
                System.out.println(hostname);

                netHandler = new NetHandler(19000);
                Thread recThread = new Thread(netHandler);
                recThread.start();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e + " Brak połączenia sieciowego");
            }
        } else {
            String hostname = "localhost";

            netHandler = new NetHandler(hostname, 19000, 19001);
            Thread senThread = new Thread(netHandler);
            senThread.start();
        }
        Boolean tempFlag = true;
        while (true) {
            Thread.sleep(500);
            if (sender != null) {
                if (tempFlag) {
                    netHandler.printNetInfo();
                    tempFlag = false;
                }
                Scanner reader = new Scanner(System.in);
                String text = reader.nextLine();

                /*Wybór podjętego działania*/
                if (text.equals("move")) {
                    /*Wykonanie ruchu*/
                    int from1 = reader.nextInt();
                    int from2 = reader.nextInt();
                    int to1 = reader.nextInt();
                    int to2 = reader.nextInt();
                    Move move = new Move(currentGame.getBoard().getSquare(from1, from2), currentGame.getBoard().getSquare(to1, to2));
                    currentGame.makeMove(move);
                    System.out.println(sender.sendCurrentGame());
                } else if (text.equals("disconnect")) {
                    /*Info o rozłączeniu*/
                    sender.sendDisconnectInfo();
                    System.exit(0);
                } else if (text.equals("board")) {
                    System.out.println(currentGame.getBoard());
                } else {
                    /*Wiadomość*/
                    sender.sendText(text);
                }
            }
        }
    }
}
