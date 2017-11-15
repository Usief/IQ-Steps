package comp1110.ass2.gui;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class getIndexTest {

    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);

    @Test
    public void getIndex() throws Exception {
        int[] index = Pieces.A.getIndex('A');
        assertTrue("Test the position 'A' of the piece A. Should be [0, 4, 6, 10, 12, 14], but" + index.toString(), index.equals(new int[]{0, 4, 6, 10, 12, 14}));
    }

}