package comp1110.ass2;

import java.util.*;
import java.util.List;

import comp1110.ass2.gui.Pieces;

/**
 * This class provides the text interface for the Steps Game
 *
 * The game is based directly on Smart Games' IQ-Steps game
 * (http://www.smartgames.eu/en/smartgames/iq-steps)
 */
public class StepsGame {

    private int[][][] board = new int[2][5][10];


    /**
     * Determine whether a piece placement is well-formed according to the following:
     * - it consists of exactly three characters
     * - the first character is in the range A .. H (shapes)
     * - the second character is in the range A .. H (orientations)
     * - the third character is in the range A .. Y and a .. y (locations)
     *
     * @param piecePlacement A string describing a piece placement
     * @return True if the piece placement is well-formed
     *
     * @author Hengrui Wang(u6202156)
     */
    // FIXME Task 2: determine whether a piece placement is well-formed
    static boolean isPiecePlacementWellFormed(String piecePlacement) {
        if (piecePlacement.charAt(0) >= 'A' && piecePlacement.charAt(0) <= 'H') {
            if (piecePlacement.charAt(1) >= 'A' && piecePlacement.charAt(1) <= 'H') {
                if ((piecePlacement.charAt(2) >= 'A' && piecePlacement.charAt(2) <= 'Y')
                        || (piecePlacement.charAt(2) >= 'a' && piecePlacement.charAt(2) <= 'y')) {
                    return true;
                } else return false;
            } else return false;
        } else return false;
    }

    /**
     * Determine whether a placement string is well-formed:
     *  - it consists of exactly N three-character piece placements (where N = 1 .. 8);
     *  - each piece placement is well-formed
     *  - no shape appears more than once in the placement
     *
     * @param placement A string describing a placement of one or more pieces
     * @return True if the placement is well-formed
     *
     * @author Hengrui Wang(u6202156)
     */
    static boolean testRepeat(String[] array){
        Set<String> set = new HashSet<String>();
        for(String s : array){
            set.add(s);
        }
        if(set.size() != array.length){
            return false;
        }else{
            return true;
        }
    }
    // FIXME Task 3: determine whether a placement is well-formed
    static boolean isPlacementWellFormed(String placement) {
        List<String> pieces = new ArrayList();
        boolean x = true;
        if (placement == null) return false;
        if (placement.equals("")) return false;
        if (placement.length()<25 && placement.length()%3==0) {
            for (int i =0; i<placement.length(); i++) {
                if (i==0 || i%3==0) {
                    String a = placement.substring(i,i+3);
                    x = x && isPiecePlacementWellFormed(a);
                    char piece = placement.charAt(i);
                    pieces.add(String.valueOf(piece));
                    if (!x) {
                        return false;}
                }
            }
            int size = pieces.size();
            String[] values = pieces.toArray(new String[size]);
            return testRepeat(values);
        } else return false;
    }

    /**
     * Determine whether a placement sequence is valid.  To be valid, the placement
     * sequence must be well-formed and each piece placement must be a valid placement
     * (with the pieces ordered according to the order in which they are played).
     *
     * @param placement A placement sequence string
     * @return True if the placement sequence is valid
     * @author: Weizhangxian WANG (u6033260)
     */

    static public boolean isPlacementSequenceValid(String placement) {
        // FIXME Task 5: determine whether a placement sequence is valid
        boolean isValid = true;
        int[][][] board = new int[2][5][10];

        // add pieces string on the board
        if(!isPlacementWellFormed(placement))
            return false;

        for(int i=0; i<placement.length();i=i+3){
            char pos = placement.charAt(i+2);
            int posX;
            int posY;
            int posZ;
            if (pos >='A' && pos <='Y'){
                posX = (pos - 'A') % 10;
                posY = (pos - 'A') / 10;
            }else{
                posX = (pos - 'a' + 25) % 10;
                posY = (pos - 'a' + 25) / 10;
            }
            if(placement.charAt(i+1)<'E'){
                posZ = 0;
            }else{
                posZ = 1;
            }
            if((posX+posY+posZ)%2==1){
                return false;
            }
            switch (placement.charAt(i)){
                case 'A': isValid = puttingPiece(Pieces.A.getIndex(placement.charAt(i+1)), placement.charAt(i+2), board); break;
                case 'B': isValid = puttingPiece(Pieces.B.getIndex(placement.charAt(i+1)), placement.charAt(i+2), board); break;
                case 'C': isValid = puttingPiece(Pieces.C.getIndex(placement.charAt(i+1)), placement.charAt(i+2), board); break;
                case 'D': isValid = puttingPiece(Pieces.D.getIndex(placement.charAt(i+1)), placement.charAt(i+2), board); break;
                case 'E': isValid = puttingPiece(Pieces.E.getIndex(placement.charAt(i+1)), placement.charAt(i+2), board); break;
                case 'F': isValid = puttingPiece(Pieces.F.getIndex(placement.charAt(i+1)), placement.charAt(i+2), board); break;
                case 'G': isValid = puttingPiece(Pieces.G.getIndex(placement.charAt(i+1)), placement.charAt(i+2), board); break;
                case 'H': isValid = puttingPiece(Pieces.H.getIndex(placement.charAt(i+1)), placement.charAt(i+2), board); break;

            }
            if (!isValid){
                return false;
            }
        }
        return true;
    }

    /**
     * trying to put the piece on the board. If the elements of the board is 1, the position of the board is not available.
     * @param index an int array, representing the piece
     * @param pos the peg.
     * @param board a 3-D int array
     * @return true if the piece has been putted on the sucessfully.
     *
     *
     */

    /* add pieces on the board and determine the failed situations */
    static boolean puttingPiece(int[] index, char pos, int[][][] board){
        int posX=0;
        int posY=0;

        if (pos >='A' && pos <='Y'){
            posX = (pos - 'A') % 10;
            posY = (pos - 'A') / 10;
        }

        if (pos >='a' && pos<='y'){
            posX = (pos - 'a' + 25) % 10;
            posY = (pos - 'a' + 25) / 10;
        }

        for(int i=0; i<index.length;i++){
            int x = posX+index[i]%9%3-1;
            int y = posY+index[i]%9/3-1;
            int z = index[i]/9;
            if (x<0 || x>=10 || y<0 || y>=5){
                return false;
            }
            if (board[z][y][x]== 1){
                return false;  //overlapping
            }

            if ((y + 1 < 5 && board[z][y + 1][x] == 1) || (y - 1 >= 0 && board[z][y - 1][x] == 1) || (x + 1 < 10 && board[z][y][x + 1] == 1) || (x - 1 >= 0 && board[z][y][x - 1] == 1)) {
                return false; //overlapping on the y direction
            }

            if (z == 0 && ((y + 1 < 5 && board[1][y + 1][x] == 1) || (y - 1 >= 0 && board[1][y - 1][x] == 1) || (x + 1 < 10 && board[1][y][x + 1] == 1) || (x - 1 >= 0 && board[1][y][x - 1] == 1))) {
                return false; //overlapping on the x direction
            }
            board[z][y][x] = 1;
        }
        return true;
    }


    /**
     * Given a string describing a placement of pieces and a string describing
     * an (unordered) objective, return a set of all possible next viable
     * piece placements.   A viable piece placement must be a piece that is
     * not already placed (ie not in the placement string), and which will not
     * obstruct any other unplaced piece.
     *
     * @param placement A valid sequence of piece placements where each piece placement is drawn from the objective
     * @param objective A valid game objective, but not necessarily a valid placement string
     * @return An set of viable piece placements
     *
     */
    public static Set<String> getViablePiecePlacements(String placement, String objective) {
        // FIXME Task 6: determine the correct order of piece placements
        Set<String> possiblePlacement = new HashSet<>();
        String unUsedPieces = objective;
        int[][][] board = new int[2][5][10];

        if (!placement.isEmpty())
            board = puttingPiece(placement, board);

        for (int i = 0; i<placement.length();i+=3){
            unUsedPieces = unUsedPieces.replace(placement.substring(i,i+3), "");
        }

        for (int i = 0; i<unUsedPieces.length();i+=3){
            boolean canPut = true;
            String piece = unUsedPieces.substring(i, i+3);
            int [][][] board_tmp = puttingPiece(piece, board);
            if (board_tmp==null){
                continue;
            }
            for (int j=0; j<unUsedPieces.length();j+=3){
                if(j==i){
                    continue;
                }
                String nextPiece = unUsedPieces.substring(j, j+3);
                if(puttingPiece(nextPiece, board_tmp)== null){
                    canPut = false;
                    break;
                }
            }

            if(canPut)
                possiblePlacement.add(piece);
        }

        return possiblePlacement;
    }

    /**
     * trying to putting the piece on the board. If the elements on the board is 1, the position is not available.
     * @param placement A piece placement
     * @param board 3-D array.
     * @return a board with pieces.
     *
     * @author Yu Yang(u6412985)
     */

    static int[][][] puttingPiece(String placement, int[][][] board){
        int posX=0;
        int posY=0;

        int[][][] board_tmp= new int[2][5][10];
        int[] index = new int[1];

        for(int z=0;z<2;z++){
            for(int y=0;y<5;y++){
                for(int x=0;x<10;x++){
                    board_tmp[z][y][x]=board[z][y][x];
                }
            }
        }
        for (int i=0; i<placement.length();i+=3) {
            switch (placement.charAt(i)) {
                case 'A':
                    index = Pieces.A.getIndex(placement.charAt(i+1));
                    break;
                case 'B':
                    index = Pieces.B.getIndex(placement.charAt(i+1));
                    break;
                case 'C':
                    index = Pieces.C.getIndex(placement.charAt(i+1));
                    break;
                case 'D':
                    index = Pieces.D.getIndex(placement.charAt(i+1));
                    break;
                case 'E':
                    index = Pieces.E.getIndex(placement.charAt(i+1));
                    break;
                case 'F':
                    index = Pieces.F.getIndex(placement.charAt(i+1));
                    break;
                case 'G':
                    index = Pieces.G.getIndex(placement.charAt(i+1));
                    break;
                case 'H':
                    index = Pieces.H.getIndex(placement.charAt(i+1));
                    break;
            }
            char pos = placement.charAt(i+2);
            if (pos >= 'A' && pos <= 'Y') {
                posX = (pos - 'A') % 10;
                posY = (pos - 'A') / 10;
            }

            if (pos >= 'a' && pos <= 'y') {
                posX = (pos - 'a' + 25) % 10;
                posY = (pos - 'a' + 25) / 10;
            }

            for (int j = 0; j < index.length; j++) {
                int x = posX + index[j] % 9 % 3 - 1;
                int y = posY + index[j] % 9 / 3 - 1;
                int z = index[j] / 9;
                if (x < 0 || x >= 10 || y < 0 || y >= 5) {
                    return null;
                }
                if (board_tmp[z][y][x] == 1) {
                    return null;
                }
                if ((y + 1 < 5 && board_tmp[z][y + 1][x] == 1) || (y - 1 >= 0 && board_tmp[z][y - 1][x] == 1) || (x + 1 < 10 && board_tmp[z][y][x + 1] == 1) || (x - 1 >= 0 && board_tmp[z][y][x - 1] == 1)) {
                    return null;
                }

                if (z == 0 && ((y + 1 < 5 && board_tmp[1][y + 1][x] == 1) || (y - 1 >= 0 && board_tmp[1][y - 1][x] == 1) || (x + 1 < 10 && board_tmp[1][y][x + 1] == 1) || (x - 1 >= 0 && board_tmp[1][y][x - 1] == 1))) {
                    return null;
                }
                board_tmp[z][y][x] = 1;
            }
        }
        return board_tmp;
    }

    /**
     * Return an array of all unique (unordered) solutions to the game, given a
     * starting placement.   A given unique solution may have more than one than
     * one placement sequence, however, only a single (unordered) solution should
     * be returned for each such case.
     *
     * @param placement  A valid piece placement string.
     * @return An array of strings, each describing a unique unordered solution to
     * the game given the starting point provided by placement.
     */
    static String[] getSolutions(String placement) {
        // FIXME ;Task 9: determine all solutions to the game, given a particular starting placement
        Set<Integer> boundary = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,21,31,41,20,30,40,50,41,42,43,44,45,46,47,48,49));
        Set<Integer> board = Help.rearrange(Help.canUse(placement));
        //board.addAll(boundary);
        ArrayList<Character> unplacedShapes = new ArrayList<>(Help.getUnusedShapes(placement));
        //int[][][] board1 = new int[2][5][10];

        Set<String> solutions = new HashSet<>(Help.getSols(unplacedShapes,board));
        Set<String> list1 = new HashSet<>();
        for (String i:solutions) {
            //if(puttingPiece((placement+i),board1)!=null)  {
            if(Help.CanBePlaced((placement+i),placement))  {
                list1.add(i);
            }
        }
        String[] output = new String[list1.size()];
        int index = 0;
        for(String i:list1){
            output[index] = placement+i;
            index++;
        }
        return output;
    }

    // for task10
    public static String[] getHints(String placement) {
        Set<String> list1 = new HashSet<>(Arrays.asList(getSolutions(placement)));
        Set<String> list2 = new HashSet<>();
        for (String s:list1) {
            list2.addAll(getViablePiecePlacements(placement,s));
        }

        String[] hints = new String[list2.size()];
        int index =0;
        for(String i:list2){
            hints[index] = i.substring(0,3);
            index++;
        }
        //If you want to see the exact placement
        /*for(String i:list2){
            hints[index] = i;
            index++;
        }*/
        return hints;

    }
}


