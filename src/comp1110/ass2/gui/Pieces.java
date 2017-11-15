package comp1110.ass2.gui;

import comp1110.ass2.Help;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by will on 2017/8/22
 *
 * Information taken and reordered from <https://gitlab.cecs.anu.edu.au/comp1110/comp1110-ass2>
 *
 * The game comprises 8 playing shapes, each of which is made of
 * plastic and consists of five or six flat rings arranged in two
 * layers (see the photo above).
 * Each layer has an arrangement of two or three rings.
 * The thickness of the rings is the same as the height of the board pegs,
 * so once a piece is played, the upper layer of rings
 * always sits neatly above the top of the pegs.
 *     - The two layers of rings are fused together.
 *     - The two layers are offset from each other by half a peg space
 *       (rings are never directly on top another).
 *
 * The center-most ring is defined to be the home of the piece
 * (the placement of the piece is defined in terms of the placement of the center ring).
 * Shapes may be whetherFlipped and rotated, so the home ring may
 * be on the top or bottom layer at the time it is played.
 *     - For orientations A-D, the home ring is on the bottom layer.
 *     - For orientations E-H, the home ring is on the top layer
 *
 *
 *                             --=Piece Placement Strings=--
 *
 * A piece placement string consists of three characters describing the location
 * and orientation of one particular piece on the board:
 *     - The first character identifies which of the eight shapes is being placed ('A' to 'H').
 *     - The second character identifies which orientation the piece is in
 *       ('A' to 'E' for four rotations, and then 'F' to 'H' for four whetherFlipped rotations).
 *     - The third character identifies which location on the board the home of the piece is to be placed on
 *       ('A' to 'Y' represent the first 25 locations and 'a' to 'y' represent the next 25 locations)
 *
 */
public enum Pieces {
    A(0, 4, 6, 10, 12, 14), B(4, 8, 10, 14, 16), C(4, 6, 10, 14, 16), D(4, 8, 10, 12, 16),
    E(4, 6, 10, 12, 16), F(2, 4, 6, 14, 16), G(2, 4, 6, 10, 14, 16), H(2, 4, 8, 10, 12, 16);
    private String piecesPlacement;        // The set of shapes that must be left piecesPlacement
    private String pieceConstraint;   // A constraint on the rotation of mask 'W', if any
    private String position;       // The actual position which one piece on the board
    private int[] index;

    Pieces(int n0, int n1, int n2, int n3, int n4, int n5){
        index = new int[6];
        index[0]=n0;
        index[1]=n1;
        index[2]=n2;
        index[3]=n3;
        index[4]=n4;
        index[5]=n5;
    }

    Pieces(int n0, int n1, int n2, int n3, int n4){
        index = new int[5];
        index[0]=n0;
        index[1]=n1;
        index[2]=n2;
        index[3]=n3;
        index[4]=n4;
    }

    /**
     * A string describing the set of shapes
     */
    Pieces(String piecesPlacement) {
        this.piecesPlacement = piecesPlacement;
    }

    /**
     * @param piecesPlacement A string describing the set of shapes that must be left piecesPlacement
     * @param pieceConstraint (A' to 'E' for four rotations, and then 'F' to 'H' for four whetherFlipped rotations)
     */
    Pieces(String piecesPlacement, String pieceConstraint) {
        this.piecesPlacement = piecesPlacement;
        this.pieceConstraint = pieceConstraint;
    }

    /**
     * get the piece index
     * @param placement A char to describe to direction of a piece.
     * @return an int array that contains a series integers describing the piece.
     *
     * @author Yu Yang(u6412985)
     */
    public int[] getIndex(char placement){
        boolean flip = false;
        int[] tmp = this.index.clone();
        if (placement<'A'|| placement>'H'){
            return null;
        }

        if ((placement-'A')/4==1){
            flip = true;
        }else{
            flip = false;
        }

        if (flip){
            tmp = flip(tmp);
        }

        int rotated_time = (placement-'A')%4;

        for (int i=0; i<rotated_time;i++){
            tmp = rotatePiece(tmp);
        }
        Arrays.sort(tmp);
        return tmp;
    }

    /**
     *
     * flip the piece.
     * @param index the index of piece
     * @return the whetherFlipped piece.
     *
     * @author Yu Yang(u6412985)
     */
    public int[] flip(int[] index){
        for (int i=0; i<index.length;i++){
            if (index[i]%3 == 0){
                index[i] +=2;
            }else if(index[i]%3==2){
                index[i] -= 2;
            }
            index[i] = (index[i] + 9) % 18;
        }
        return index;
    }

    /**
     * rotate the piece
     * @param indics the piece index
     * @return a new index representing the rotated piece.
     *
     * @author Yu Yang(u6412985)
     * based on rotate() in class Mask of assigment1.
     * URL: https://gitlab.cecs.anu.edu.au/u6412985/comp1110-ass1/blob/master/src/comp1110/ass1/Mask.java
     */
    public static int[] rotatePiece(int[] indics){
        int[][][] metrix = new int[2][3][3];
        for (int i=0;i<indics.length;i++) {
            metrix[indics[i]/9][(indics[i])%9/3][(indics[i])%9%3] = 1;
        }
        int[][][] rotated_metrix = new int[2][3][3];
        for(int k=0;k<2;k++) {
            for (int i = 0; i < 3; i++) {                                  // using a metrix to represent the Mask.
                for (int j = 2; j >= 0; j--) {
                    rotated_metrix[k][j][2 - i] = metrix[k][i][j];
                }
            }
        }
        int[] tmp = new int[indics.length];
        int m=0;
        for (int k=0;k<2;k++) {
            for (int i = 0; i < 3; i++) {                                 // rotate the metrics
                for (int j = 0; j < 3; j++) {
                    if (rotated_metrix[k][i][j] != 0) {
                        tmp[m] = k*9+ i * 3 + j;
                        m++;
                    }
                }
            }
        }
        return tmp;
    }

    static public int encode(char c){
        if( c>='A' && c<='Y') return ((int)c - 64);
        else return ((int)c -71);
    }
    static public char decode(int i) {
        if (i >= 1 && i <= 25) return (char) (i + 64);
        else return (char) (i + 71);
    }

    static public boolean isPieceValid(String piece) {
        Set<Integer> noPensLocation  = new HashSet<>(Arrays.asList(2,4,6,8,10,11,13,15,17,19,22,24,26,28,30,31,33,35,37,39,42,44,46,48,50));
        Set<Character> upper  = new HashSet<>(Arrays.asList('A','B','C','D','E','F','G','H','I','J'));
        Set<Character> lower  = new HashSet<>(Arrays.asList('p','q','r','s','t','u','v','w','x','y'));
        Set<Character> left  = new HashSet<>(Arrays.asList('A','K','U','f','p'));
        Set<Character> right  = new HashSet<>(Arrays.asList('J','T','e','o','y'));
        ArrayList valid = new ArrayList();
        char shape = piece.charAt(0);
        char orientation = piece.charAt(1);
        char location = piece.charAt(2);
        if (shape!='E' && shape!='B') {
            valid.add(!upper.contains(location) && !lower.contains(location) && !left.contains(location) && !right.contains(location));
            if (orientation >= 'A' && orientation <= 'D' && noPensLocation.contains(Pieces.encode(location))) valid.add(false);
            if (orientation >= 'E' && orientation <= 'H' && !noPensLocation.contains(Pieces.encode(location))) valid.add(false);
        }
        switch (shape) {
            case ('B'):
                if (orientation == 'A' || orientation == 'G') {
                    if (left.contains(location) && location != 'A' && location != 'p') valid.add(true);
                }
                if (orientation == 'B' || orientation == 'H') {
                    if (upper.contains(location) && location != 'J' && location != 'A') valid.add(true);
                }
                if (orientation == 'C' || orientation == 'E') {
                    if (right.contains(location) && location != 'y' && location != 'J') valid.add(true);
                }
                if (orientation == 'D' || orientation == 'F') {
                    if (lower.contains(location) && location != 'p' && location != 'y') valid.add(true);
                } break;

            case ('E') :
                if (orientation == 'A' || orientation == 'G') {
                    if (right.contains(location) && location != 'y' && location != 'J') valid.add(true);
                }
                if (orientation == 'B' || orientation == 'H') {
                    if (lower.contains(location) && location != 'p' && location != 'y') valid.add(true);
                }
                if (orientation == 'C' || orientation == 'E') {
                    if (left.contains(location) && location != 'A' && location != 'p') valid.add(true);
                }
                if (orientation == 'D' || orientation == 'F') {
                    if (upper.contains(location) && location != 'J' && location != 'A') valid.add(true);
                } break;
        }

        return !valid.contains(false);
    }

    //return a list of int to represent the piece on board in 3*3 grid.
    static public int[] encodemap(String placement){
        char shape = placement.charAt(0);
        char orientation = placement.charAt(1);
        int location = (int) placement.charAt(2);
        int [] map = new int[9];

        if( location>=65 && location<=89 ) location = location-64+10;//easy for rotation.
        if( location>=97 && location<=121) location = location-71+10;
        switch (shape) {
            case 'A' :
                if (!((orientation-'A')/4==1))
                    map = new int[]{ location-11, location -10, 0,
                            location-1, location, location+1,
                            location+9, 0, 0};
                else
                    map = new int[]{0, location-10, location-9,
                            location-1, location, location+1,
                            0, 0, location+11};
                break;

            case 'B' :
                if (!((orientation-'A')/4==1))
                    map = new int[]{0, location-10, 0,
                            0, location, location+1,
                            0, location+10, location+11};
                else
                    map = new int[]{0, location-10, 0,
                            location-1, location, 0,
                            location+9, location+10, 0};
                break;

            case 'C' :
                if (!((orientation-'A')/4==1))
                    map = new int[]{0, location-10, 0,
                            0, location, location+1,
                            location+9, location+10, 0};
                else
                    map = new int[]{0, location-10, 0,
                            location-1, location, 0,
                            0, location+10, location+11};
                break;

            case 'D':
                if (!((orientation-'A')/4==1))
                    map = new int[]{0, location-10, 0,
                            location-1, location, 0,
                            0, location+10, location+11};
                else
                    map = new int[]{0, location-10, 0,
                            0, location, location+1,
                            location+9, location+10, 0};
                break;

            case 'E' :
                if (!((orientation-'A')/4==1))
                    map = new int[]{0, location-10, 0,
                            location-1, location, 0,
                            location+9, location+10, 0};
                else
                    map = new int[]{0, location-10, 0,
                            0, location, location+1,
                            0, location+10, location+11};
                break;

            case 'F' :
                if (!((orientation-'A')/4==1))
                    map = new int[]{0, 0, location-9,
                            0, location, location+1,
                            location+9, location+10, 0};
                else
                    map = new int[]{location-11, 0, 0,
                            location-1, location, 0,
                            0, location+10, location+11};
                break;

            case 'G' :
                if (!((orientation-'A')/4==1))
                    map = new int[]{0, location-10, location-9,
                            0, location, location+1,
                            location+9, location+10, 0};
                else
                    map = new int[]{location-11, location-10, 0,
                            location-1, location, 0, 0,
                            location+10, location+11};
                break;

            case 'H' :
                if (!((orientation-'A')/4==1))
                    map = new int[]{0, location-10, location-9,
                            location-1, location, 0,
                            0, location+10, location+11};
                else
                    map = new int[]{location-11, location-10, 0,
                            0, location, location+1,
                            location+9, location+10, 0};
                break;
        }
        return map;
    }
    //  Rotate the piece
    static public int[] rotate(int[] map, int rotationTimes ) {
        //int[] map = encodemap(piece);
        int[] output = new int[9];
        for (int i=0; i<rotationTimes; i++) {
            output[4] = map[4];
            //map[0]
            if (map[0] == 0) output[2] = 0;
            else output[2] = map[0]+2;
            //map[1]
            if (map[1] == 0) output[5] = 0;
            else output[5] = map[1]+11;
            //map[2]
            if (map[2] == 0) output[8] = 0;
            else output[8] = map[2]+20;
            //map[3]
            if (map[3] == 0) output[1] = 0;
            else output[1] = map[3]-9;
            //map[5]
            if (map[5] == 0) output[7] = 0;
            else output[7] = map[5]+ 9;
            //map[6]
            if (map[6] == 0) output[0] = 0;
            else output[0] = map[6]-20;
            //map[7]
            if (map[7] == 0) output[3] = 0;
            else output[3] = map[7]-11;
            //map[8]
            if (map[8] == 0) output[6] = 0;
            else output[6] = map[8]-2;
        }

        return output;
    }

    //  Return an array of int which stores all placements occupied by the piece on the board
    public static Set<Integer> returnArray(String piece){
        int[] map = encodemap(piece);
        Set piece1 = new HashSet<>();
        for (int i=0;i<(piece.charAt(1) - 'A') % 4;i++) {
            map = rotate(map,(piece.charAt(1) - 'A') % 4);
        }
        for (int i:map){
            piece1.add(i-10);
        }
        piece1.remove(-10);
        return piece1;
    }

}
