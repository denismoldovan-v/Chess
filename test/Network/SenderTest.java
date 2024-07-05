package Network;

import org.junit.Test;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

public class SenderTest {

    @Test
    public void illegalConstructorArgumentsTest() {
        NetHandler host = new NetHandler(1234);
        assertThrows(UnknownHostException.class, () ->{
            new Sender("badip", 12345, host);
        });
        assertThrows(IllegalArgumentException.class, () ->{
            new Sender("8.8.8.8", 1111111111, host);
        });
        assertThrows(AssertionError.class, () ->{
            new Sender("8.8.8.8", 10002, null);
        });
    }

    @Test
    public void legalConstructorArgumentsTest() {
        NetHandler host = new NetHandler(1234);
        try {
            Sender sender = new Sender("8.8.8.8", 10002, host);
            assertEquals("8.8.8.8",sender.getHostname());
            assertEquals(10002, sender.getPort());
        }catch(Exception _){}

    }
}