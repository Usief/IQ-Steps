package comp1110.ass2.gui;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class FlipTest {
    @Test
    public void flip() throws Exception {
        int[] index = {0, 4,6,10,12,14};
        int[] result = Pieces.A.flip(index);
        int[] target = {1,3,5, 11,13,17};
        assertTrue(Arrays.equals(result, target));

        int[] index1 = {4, 8,10,14,16};
        int[] result1 = Pieces.B.flip(index1);
        int[] target1 = {1,3,7,13,15};
        assertTrue(Arrays.equals(result1, target1));

        int[] index2 = {0,4,12,14,16};
        int[] result2 = Pieces.C.flip(index2);
        int[] target2 = {3,5,7,11,13};
        assertTrue(Arrays.equals(result2, target2));

        int[] index3 = {1,3,7,13,17};
        int[] result3 = Pieces.D.flip(index3);
        int[] target3 = {4,6,10,14,16};
        assertTrue(Arrays.equals(result3, target3));

        int[] index4 = {4,6,10,12,16};
        int[] result4 = Pieces.E.flip(index4);
        int[] target4 = {1,5,7,13,17};
        assertTrue(Arrays.equals(result4, target4));

        int[] index6 = {2,4,6,10,14,16};
        int[] result6 = Pieces.G.flip(index6);
        int[] target6 = {1,3,7,9,13,17};
        assertTrue(Arrays.equals(result6, target6));

        int[] index7 = {2,4,8,10,12,16};
        int[] result7 = Pieces.H.flip(index7);
        int[] target7 = {1,5,7,9,13,15};
        assertTrue(Arrays.equals(result7, target7));


    }

}