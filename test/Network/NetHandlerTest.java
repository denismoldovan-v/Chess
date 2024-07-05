package Network;

import org.junit.Test;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

public class NetHandlerTest {

    @Test
    public void illegalArgumentConstructorsTest() {
        assertThrows(UnknownHostException.class,()->
                new NetHandler("badIP", 322, 3223));

        assertThrows(IllegalArgumentException.class,()->
                new NetHandler("localhost", -1219, 3223));

        assertThrows(IllegalArgumentException.class,()->
                new NetHandler("localhost", 322, 932090));

        assertThrows(IllegalArgumentException.class,()->
                new NetHandler(932090));

        assertThrows(IllegalArgumentException.class,()->
                new NetHandler(-32090));
    }

    @Test
    public void illegalArgumentMethodsTest() {
        NetHandler netH = new NetHandler();

        assertThrows(UnknownHostException.class,()->
                netH.setConnectedHost("badIP"));

        assertThrows(IllegalArgumentException.class,()->
                netH.setConnectedHostPort(-2211));

        assertThrows(IllegalArgumentException.class,()->
                netH.setConnectedHostPort(222211));
    }

    @Test
    public void gettersAndSettersTest() {
        NetHandler netH = new NetHandler(2121);

        assertEquals(2121, netH.getPort());

        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);

            assertEquals(socket.getLocalAddress().getHostAddress(), netH.getHostname());

        } catch (Exception _){}
    }
}