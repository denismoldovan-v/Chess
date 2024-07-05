package Network;

import Chess.Game.Main;
import Chess.Game.Move;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

/**
 * Klasa odpowiadająca za wysyłanie danych do drugiego użytkownika.
 */
public class Sender {
    private final NetHandler netHandler;
    private final String hostname;
    private final Integer port;

    /**
     * Klasa implementująca mechanizm określenia utrzymania połączenia. Działa jak tykająca bomba.
     * Miota wyjątkiem w przypadku 7,5 sekundowego braku odpowiedzi na żądanie połączenia.
     * Odliczanie przerywane jest przeszkodzeniem wątkowi.
     */
    private static class ConnectionMaintainer implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                return;
            }
            throw new RuntimeException("Desynchronizacja");
        }
    }


    /**
     * Konstruktor tworzący obiekt i, jeśli podany NetHandler jest klientem, wywołujący metodę, która wysyła dane sieciowe komputera do hosta.
     * @param hostname Docelowy adres IP.
     * @param port Docelowy port.
     * @param netHandler NetHandler aplikacji.
     */
    public Sender(String hostname, Integer port, NetHandler netHandler) throws UnknownHostException, IllegalArgumentException {
        /*Testowanie argumentów*/
        assert netHandler != null;
        InetAddress.getByName(hostname);
        if(port<=0 || port > 65535) throw new IllegalArgumentException("Błędny port.");

        this.netHandler = netHandler;
        this.hostname = hostname;
        this.port = port;

        /*Określenie, czy należy wysłać informacje adresowe*/
        if(!netHandler.isHost()) {
            try {
                sendInfo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Prywatna metoda wysyłąjąca informacje adresowe (własny IP i port) do hosta.
     * @return True - pakiet dotarł.
     * @throws IOException Błąd zwracany w przypadku problemu z połączeniem.
     */
    private Boolean sendInfo() throws IOException {
        /*Ustawienie docelowych informacji adresowych*/
        netHandler.setConnectedHost(hostname);
        netHandler.setConnectedHostPort(port);

        DataWrapper dWrapper = new DataWrapper();
        /*Zawinięcie własnych informacji adresowych*/
        dWrapper.wrapNetInfo(new Pair<>(netHandler.getHostname(), netHandler.getPort()));
        send(dWrapper);

        return true;
    }

    /**
     * Publiczna metoda wysyłąjąca wykonany ruch do przeciwnika.
     * @param move Wysyłany, wykonany ruch.
     * @return True - pakiet dotarł.
     * @throws IOException Błąd zwracany w przypadku problemu z połączeniem.
     */
    public Boolean sendMove(Move move) throws IOException {
        /*Testowanie argumentów*/
        assert move != null;

        DataWrapper dWrapper = new DataWrapper();
        dWrapper.wrapMove(move);
        send(dWrapper);

        return true;
    }

    /**
     * Publiczna metoda wysyłąjąca aktualną planszę do przeciwnika.
     * @return True - pakiet dotarł.
     * @throws IOException Błąd zwracany w przypadku problemu z połączeniem.
     */
    public Boolean sendBoard() throws IOException {
        /*Testowanie argumentów*/
        assert Main.getCurrentGame().getBoard() != null;

        DataWrapper dWrapper = new DataWrapper();
        dWrapper.wrapBoard(Main.getCurrentGame().getBoard());
        send(dWrapper);

        return true;
    }

    /**
     * Publiczna metoda wysyłąjąca aktualną grę do przeciwnika.
     * @return True - pakiet dotarł.
     * @throws IOException Błąd zwracany w przypadku problemu z połączeniem.
     */
    public Boolean sendCurrentGame() throws IOException {
        /*Testowanie argumentów*/
        assert Main.getCurrentGame() != null;

        DataWrapper dWrapper = new DataWrapper();
        dWrapper.wrapGame(Main.getCurrentGame());
        send(dWrapper);

        return true;
    }

    /**
     * Publiczna metoda odpowiadająca za przesłanie informacji o rozłączeniu się do drugiego komputera.
     * @return True - pakiet dotarł.
     * @throws IOException Błąd zwracany w przypadku problemu z połączeniem.
     */
    public Boolean sendDisconnectInfo() throws IOException {
        DataWrapper dWrapper = new DataWrapper();
        dWrapper.wrapDisconnectionInfo();
        send(dWrapper);

        return true;
    }

    /**
     * Publiczna metoda odpowiadająca za przesłanie informacji o poddaniu się przeciwnika.
     * @return True - pakiet dotarł.
     * @throws IOException Błąd zwracany w przypadku problemu z połączeniem.
     */
    public Boolean sendGiveUpInfo() throws IOException {
        DataWrapper dWrapper = new DataWrapper();
        dWrapper.wrapGiveUpInfo();
        send(dWrapper);

        return true;
    }

    /**
     * Publiczna metoda odpowiadająca za przesłanie wiadomości.
     * @return True - pakiet dotarł.
     * @throws IOException Błąd zwracany w przypadku problemu z połączeniem.
     */
    public Boolean sendText(String text) throws IOException {
        DataWrapper dWrapper = new DataWrapper();
        dWrapper.wrapText(text);

        send(dWrapper);
        return true;
    }

    /**
     * Prywatna metoda wysyłająca podane opakowane dane.
     * @param dWrapper Obiekt zawierający opakowane dane.
     * @throws IOException Błąd zwracany w przypadku problemu z połączeniem.
     */
    private void send(DataWrapper dWrapper) throws IOException {
        SocketChannel sChannel = SocketChannel.open();
        sChannel.configureBlocking(true);

        /*Konfiguracja mechanizmu utrzymania połączenia*/
        ConnectionMaintainer conMntnr = new ConnectionMaintainer();
        Thread cM = new Thread(conMntnr);
        cM.start();

        if (sChannel.connect(new InetSocketAddress(hostname, port))) {
            ObjectOutputStream oos = new ObjectOutputStream(sChannel.socket().getOutputStream());
            oos.writeObject(dWrapper);
            oos.close();

            /*Wyłączenie odliczania mechanizmu utrzymania połączenia*/
            cM.interrupt();
        }
    }

    /**
     * @return Docelowy adres IP.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @return Docelowy port.
     */
    public Integer getPort() {
        return port;
    }
}