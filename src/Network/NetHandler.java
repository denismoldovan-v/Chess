package Network;

import Chess.Game.Main;
import Chess.Game.Move;

import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Klasa obsługująca połączenie sieciowe (host/klient). Uruchamiana jest w osobnym wątku i non-stop czeka na pakiety.
 * Inicjalizacja dla hosta przebiega w następujący sposób:
 *      - zapisanie danych sieciowych hosta (IP i port),
 *      - oczekiwanie na przysłanie od klienta jego danych sieciowych,
 *      - zainicjalizowanie własnego sendera, który nie wysyła pakietu z informacjami sieciowymi.
 * Inicjalizacja dla klienta przebiega w następujący sposób:
 *      - zapisanie wszystkich danych sieciowych (IP docelowe, port docelowy, port własny),
 *      - inicjalizacja sendera, który przesyła dane sieciowe do podłączonego hosta,
 *      - NetHandler nie oczekuje na pakiet z danymi hosta, ponieważ go nie potrzebuje.
 * Po tej procedurze oba komputery są gotowe do wymiany ruchów.
 */
public class NetHandler implements Runnable{
    private String connectedHost;
    private Integer connectedHostPort;
    private String hostname;
    private Integer port;
    private final Boolean isHost;

    /**
     * Konstruktor dla klienta.
     * @param conHost Adres IP docelowy (hosta).
     * @param conPort Port docelowy (hosta).
     * @param port Port własny.
     */
    public NetHandler(String conHost, Integer conPort, Integer port) throws UnknownHostException, IllegalArgumentException {
        /*Testowanie argumentów*/
        InetAddress.getByName(conHost);
        if((conPort<=0 || conPort > 65535) || (port<=0 || port > 65535)) throw new IllegalArgumentException("Błędny port.");

        /*Zapis podanych danych sieciowych*/
        this.port = port;
        connectedHost = conHost;
        connectedHostPort = conPort;

        /*Pobranie informacji o własnym adresie IP*/
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            hostname = socket.getLocalAddress().getHostAddress();
        } catch (Exception e){
            System.err.println(e + "Brak połączenia sieciowego");
        }
        /*Klient*/
        isHost = false;

        /*Inicjalizacja sendera*/
        Main.sender = new Sender(connectedHost, connectedHostPort, this);
    }

    /**
     * Konstruktor dla hosta.
     * @param port Port, na którym "postawiony zostanie serwer".
     */
    public NetHandler(Integer port) throws IllegalArgumentException{
        /*Testowanie argumentów*/
        if(port<=0 || port > 65535) throw new IllegalArgumentException("Błędny port.");


        /*Zapis podanych danych sieciowych*/
        this.port = port;
        connectedHost = null;
        connectedHostPort = null;

        /*Pobranie informacji o własnym adresie IP*/
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10003);
            hostname = socket.getLocalAddress().getHostAddress();
        } catch (Exception e){
            System.err.println("Brak połączenia sieciowego");
        }

        /*Host*/
        isHost = true;
    }

    /**
     * Pusty konstruktor.
     */
    public NetHandler() {isHost = false;}

    /**
     * Określenie funkcji NetHandlera.
     * @return True - Host; False - Klient.
     */
    public Boolean isHost(){
        return isHost;
    }

    /**
     * @return Port, na którym "postawiony jest serwer"
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @return Własny adres IP.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Ustawienie adresu IP podłączonego komputera.
     * @param connectedHost Adres IP podłączonego komputera.
     */
    public void setConnectedHost(String connectedHost) throws UnknownHostException {
        /*Testowanie argumentów*/
        InetAddress.getByName(connectedHost);

        this.connectedHost = connectedHost;
    }

    /**
     * Ustawienie portu podłączonego komputera.
     * @param connectedHostPort Port podłączonego komputera.
     */
    public void setConnectedHostPort(Integer connectedHostPort) throws IllegalArgumentException{
        /*Testowanie argumentów*/
        if(connectedHostPort<=0 || connectedHostPort > 65535) throw new IllegalArgumentException("Błędny port.");

        this.connectedHostPort = connectedHostPort;
    }

    /**
     * Metoda wypisująca informacje sieciowe połączenia.
     */
    public void printNetInfo(){
        System.out.println("Me: " + hostname + ":" + port);
        System.out.println("Connected with: " + connectedHost + ":" + connectedHostPort);
    }

    /**
     * Pętla nasłuchująca na przysłane informacje.
     */
    @Override
    public void run() {
        /*Otwarcie kanału serwerowego*/
        try (ServerSocketChannel ssChannel = ServerSocketChannel.open()){
            ssChannel.configureBlocking(true);
            ssChannel.socket().bind(new InetSocketAddress(port));

            /*Pętla przyjmująca*/
            while (true) {
                DataWrapper inputWrapped;

                /*Określenie danych sieciowych klienta - ważne tylko dla trybu hosta*/
                if(connectedHost == null){
                    SocketChannel sChannel = ssChannel.accept();

                    ObjectInputStream ois = new ObjectInputStream(sChannel.socket().getInputStream());

                    /*Ustawienie IP i portu*/
                    inputWrapped = (DataWrapper) ois.readObject();
                    if(inputWrapped.getType()!= DataWrapper.DataTypes.NETINFO) continue;

                    connectedHost = inputWrapped.getNetInfo().getFirst();
                    connectedHostPort = inputWrapped.getNetInfo().getSecond();

                    System.out.println("Connected to client: " + connectedHost + ":" + connectedHostPort);

                    Main.sender = new Sender(connectedHost, connectedHostPort, this);

                    Main.sender.sendBoard();
                    continue;
                }

                /*Odczytanie ruchu*/
                SocketChannel sChannel = ssChannel.accept();
                ObjectInputStream ois = new ObjectInputStream(sChannel.socket().getInputStream());

                inputWrapped = (DataWrapper) ois.readObject();
                DataWrapper.DataTypes receivedType = inputWrapped.getType();

                /*Określenie działania, w zależności od typu otrzymanej wiadomości*/
                switch (receivedType){
                    case MOVE -> {
                        Move moveMaked = inputWrapped.getMove();
                        /*Zastosowanie odczytanego ruchu*/
                        Main.getCurrentGame().makeMove(moveMaked);
                    }
                    case TEXT -> {
                        String text = inputWrapped.getText();
                        /*Wypisanie wiadomości*/
                        System.out.println("Wiadomość od " + connectedHost + ": " + text);
                    }
                    case DISCONNECT -> {
                        /*Wyłączenie aplikacji*/
                        System.out.println(connectedHost + " rozłączył się.");
                        connectedHost = null;
                        connectedHostPort = null;
                    }
                    case BOARD -> {
                        /*Zmiana planszy*/
                        Main.getCurrentGame().setBoard(inputWrapped.getBoard());
                    }
                    case NETINFO -> {
                        Main.sender.sendCurrentGame();
                    }
                    case GIVEUP -> {
                        Main.getCurrentGame().setSurrendered();
                    }
                    case GAME -> {
                        Main.setCurrentGame(inputWrapped.getGame());
                        Main.gui.drawPieces();
                        System.out.println(Main.currentGame.getBoard());
                    }
                    default -> System.out.println("Nieznany przesłany typ danych.");
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}