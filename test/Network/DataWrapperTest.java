package Network;

import Chess.Game.Board;
import Chess.Game.Move;
import Chess.Game.Square;
import com.sun.jdi.InvalidTypeException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataWrapperTest {

    @Test
    public void textTest() {
        DataWrapper dataWrapper = new DataWrapper();
        dataWrapper.wrapText("test");
        try {
            assertEquals("test", dataWrapper.getText());
        } catch (InvalidTypeException _){}

        assertThrows(InvalidTypeException.class, dataWrapper::getBoard);
        assertThrows(InvalidTypeException.class, dataWrapper::getNetInfo);
        assertThrows(InvalidTypeException.class, dataWrapper::getMove);
        assertThrows(InvalidTypeException.class, dataWrapper::getDisconnectionInfo);
        assertThrows(InvalidTypeException.class, dataWrapper::getGiveUpInfo);

        Assert.assertEquals(DataWrapper.DataTypes.TEXT, dataWrapper.getType());
    }

    @Test
    public void moveTest() {
        DataWrapper dataWrapper = new DataWrapper();
        Move move = new Move();
        dataWrapper.wrapMove(move);
        try {
            assertEquals(move, dataWrapper.getMove());
        } catch (InvalidTypeException _){}

        assertThrows(InvalidTypeException.class, dataWrapper::getBoard);
        assertThrows(InvalidTypeException.class, dataWrapper::getNetInfo);
        assertThrows(InvalidTypeException.class, dataWrapper::getText);
        assertThrows(InvalidTypeException.class, dataWrapper::getDisconnectionInfo);
        assertThrows(InvalidTypeException.class, dataWrapper::getGiveUpInfo);

        Assert.assertEquals(DataWrapper.DataTypes.MOVE, dataWrapper.getType());
    }

    @Test
    public void netInfoTest() {
        DataWrapper dataWrapper = new DataWrapper();
        Pair<String, Integer> testInf = new Pair<>("t1",2);
        dataWrapper.wrapNetInfo(testInf);
        try {
            assertEquals(testInf, dataWrapper.getNetInfo());
        } catch (InvalidTypeException _){}

        assertThrows(InvalidTypeException.class, dataWrapper::getBoard);
        assertThrows(InvalidTypeException.class, dataWrapper::getText);
        assertThrows(InvalidTypeException.class, dataWrapper::getMove);
        assertThrows(InvalidTypeException.class, dataWrapper::getDisconnectionInfo);
        assertThrows(InvalidTypeException.class, dataWrapper::getGiveUpInfo);

        Assert.assertEquals(DataWrapper.DataTypes.NETINFO, dataWrapper.getType());
    }

    @Test
    public void boardTest() {
        DataWrapper dataWrapper = new DataWrapper();
        Board board = new Board();
        dataWrapper.wrapBoard(board);
        try {
            assertEquals(board, dataWrapper.getBoard());
        } catch (InvalidTypeException _){}

        assertThrows(InvalidTypeException.class, dataWrapper::getText);
        assertThrows(InvalidTypeException.class, dataWrapper::getNetInfo);
        assertThrows(InvalidTypeException.class, dataWrapper::getMove);
        assertThrows(InvalidTypeException.class, dataWrapper::getDisconnectionInfo);
        assertThrows(InvalidTypeException.class, dataWrapper::getGiveUpInfo);

        Assert.assertEquals(DataWrapper.DataTypes.BOARD, dataWrapper.getType());
    }

    @Test
    public void disconnectTest() {
        DataWrapper dataWrapper = new DataWrapper();
        dataWrapper.wrapDisconnectionInfo();
        try {
            assertTrue(dataWrapper.getDisconnectionInfo());
        } catch (InvalidTypeException _){}

        assertThrows(InvalidTypeException.class, dataWrapper::getBoard);
        assertThrows(InvalidTypeException.class, dataWrapper::getNetInfo);
        assertThrows(InvalidTypeException.class, dataWrapper::getMove);
        assertThrows(InvalidTypeException.class, dataWrapper::getText);
        assertThrows(InvalidTypeException.class, dataWrapper::getGiveUpInfo);

        Assert.assertEquals(DataWrapper.DataTypes.DISCONNECT, dataWrapper.getType());
    }

    @Test
    public void giveUpTest() {
        DataWrapper dataWrapper = new DataWrapper();
        dataWrapper.wrapGiveUpInfo();
        try {
            assertTrue(dataWrapper.getGiveUpInfo());
        } catch (InvalidTypeException _){}

        assertThrows(InvalidTypeException.class, dataWrapper::getBoard);
        assertThrows(InvalidTypeException.class, dataWrapper::getNetInfo);
        assertThrows(InvalidTypeException.class, dataWrapper::getMove);
        assertThrows(InvalidTypeException.class, dataWrapper::getDisconnectionInfo);
        assertThrows(InvalidTypeException.class, dataWrapper::getText);

        Assert.assertEquals(DataWrapper.DataTypes.GIVEUP, dataWrapper.getType());
    }

}