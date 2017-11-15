package comp1110.ass2;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by will on 2017/9/27.
 */
public class testRepeatTest {
    @Test
    public void testGood() {
        String[] aaa = {"AB","BC","CD","DE","EF","FG"};
        Random rand = new Random();
        for (int i=0; i<3; i++) {
            int a = rand.nextInt(aaa.length);
            String[] s = {aaa[a]};
            assertTrue(StepsGame.testRepeat(s));
        }
    }
    @Test
    public void testBad() {
        String[] bbb = {"AAA","AAB","CCE","ASDFD","AA","UUUU"};
        Random rand = new Random();
        for (int i=0; i<3; i++) {
            int a = rand.nextInt(bbb.length);
            String[] s = {bbb[a]};
            assertFalse("There is a repeat, but hasn't been detected",
                    !(StepsGame.testRepeat(s)));
        }
    }
    @Test
    public void testEmpty() {
        String[] empty = {};
        assertTrue(StepsGame.testRepeat(empty));
    }
}
