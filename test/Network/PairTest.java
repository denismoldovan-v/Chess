package Network;

import org.junit.Test;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

public class PairTest {

    @Test
    public void nullConstructorTest() {
        Pair<?,?> pair = new Pair<>();
        assertNull(pair.getFirst());
        assertNull(pair.getSecond());
    }

    @Test
    public void properConstructorTest() {
        Pair<Integer, Integer> pair = new Pair<>(1232, 1212);
        assertEquals(1232, pair.getFirst());
        assertEquals(1212, pair.getSecond());
        pair.setSecond(11);
        pair.setFirst(123);

        assertEquals(11, pair.getSecond());
        assertEquals(123, pair.getFirst());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Pair<Integer, Integer> pair = new Pair<>(1232, 1212);
        int hashFirst = pair.hashCode();
        pair.setSecond(11);
        assertNotEquals(hashFirst, pair.getSecond());

        Pair<Integer, Integer> pair2 = new Pair<>(1232, 11);
        assertEquals(pair.hashCode(), pair2.hashCode());
        assertEquals(pair, pair2);
    }
}