package Chess;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerTest {
    @Test
    public void fHTest(){
        assertThrows(AssertionError.class, ()->{
            FileHandler.loadGame(null);
        });
        assertThrows(AssertionError.class, ()->{
            FileHandler.loadGame("");
        });
        assertThrows(AssertionError.class, ()->{
            FileHandler.saveCurrentGame(null);
        });
        assertThrows(AssertionError.class, ()->{
            FileHandler.saveCurrentGame("");
        });
    }
}