package comp1110.ass2;

import comp1110.ass2.gui.Pieces;
import org.junit.Test;

import static org.junit.Assert.*;

public class puttingPieceTest {
    int[][][] board = new int[2][5][10];
    Pieces[] pieces = {Pieces.A, Pieces.B, Pieces.C, Pieces.D, Pieces.E, Pieces.F, Pieces.G, Pieces.H};

    @Test
    public void testGood() throws Exception {
        char[] pos = {'L', 'O', 'R', 'j', 'g', 'm', 'P', 'S'};
        for(int i=0; i<8;i++) {
            int[][][] board = new int[2][5][10];
            int[] index = pieces[i].getIndex((char)('A'+i));
            assertTrue(StepsGame.puttingPiece(index, pos[i], board));
        }
    }

    @Test
    public void testOverlaps() throws Exception{
        char[] pos = {'L', 'M', 'V', 'c', 'R', 'm', 'b', 'l'};

        StepsGame.puttingPiece(pieces[6].getIndex('G'), 'L', board);
        StepsGame.puttingPiece(pieces[7].getIndex('B'), 'c', board);
        for(int i=0;i<6;i++){
            int[] index = pieces[i].getIndex((char)('A'+1));
            assertFalse(StepsGame.puttingPiece(index, pos[i], board));
        }
    }

}