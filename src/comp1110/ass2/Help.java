package comp1110.ass2;

import comp1110.ass2.gui.Pieces;

import java.util.*;

/**
 * Created by will on 2017/10/1.
 * Four ways to prune hypothesis space:
   1. flipped pieces could only placed on noPensLocation
   2. unflipped pieces could only placed on pens
   3. if there is a ring on the noPensLocation, no pieces can be placed around it
   4. piece B and E are special, they can be placed at the boundary of the board if you put it properly

   Four steps to get solutions:
   1. find unplaced shapes
   2. find the places which is vacant
   3. calculate each combination (shape, orientation, vacant position)
   4. try each combination

 */
public class Help {
    // exclude used shapes.
    static public Set<Character> getUnusedShapes(String placement){
        Set<Character> shapes = new HashSet<>(Arrays.asList('A','B','C','D','E','F','G','H'));
        for(int i=0;i<placement.length();i+=3){
            shapes.remove(placement.charAt(i));
        }
        return shapes;
    }

    //Depending on the placements have been occupied, generates usable locations
    static public Set<Integer> canUse(String placement) {
        Set<Integer> board  = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50));
        Set<Integer> noPensLocation  = new HashSet<>(Arrays.asList(2,4,6,8,10,11,13,15,17,19,22,24,26,28,30,31,33,35,37,39,42,44,46,48,50));
        Set<Integer> output = new HashSet<>();
        Set<Integer> overlapped = new HashSet<>();
        for (int i = 0; i < placement.length(); i+=3) {
            output.addAll(Pieces.returnArray(placement.substring(i, i + 3)));
        }

        for (int i : output) {
            if (noPensLocation.contains(i))
                overlapped.addAll(prune(i));
        }
        board.removeAll(overlapped);
        return board;
    }

    // put the position with low posibility at the end of the test list
    static Set<Integer> rearrange (Set<Integer> canUse) {
        Set<Integer> boundary = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,21,31,41,20,30,40,50,41,42,43,44,45,46,47,48,49));
        for (Integer i:boundary) {
            if (canUse.contains(i)) {
                canUse.remove(i);
                canUse.add(i);
            }
        }
        return canUse;
    }

    //Return the placements under the input placement
    //pruning the hypothesis space
    static public Set<Integer> prune(int coord){
        Set<Integer> occupied;
        if(0<coord && coord<9) occupied=new HashSet<>(Arrays.asList(coord-1,coord+1,coord+10,coord));
        else if(coord==10 ) occupied=new HashSet<>(Arrays.asList(coord-1,coord+10,coord));
        else if(coord==11)  occupied=new HashSet<>(Arrays.asList(coord-10,coord+1,coord+10,coord));
        else if(coord==20 ) occupied=new HashSet<>(Arrays.asList(coord-1,coord+10,coord,coord-10));
        else if(coord==21)  occupied=new HashSet<>(Arrays.asList(coord-10,coord+1,coord+10,coord));
        else if(coord==30 ) occupied=new HashSet<>(Arrays.asList(coord-1,coord-10,coord+10,coord));
        else if(coord==31)  occupied=new HashSet<>(Arrays.asList(coord-10,coord+1,coord+10,coord));
        else if(coord>40 && coord<49) occupied=new HashSet<>(Arrays.asList(coord-1,coord+1,coord-10,coord));
        else if(coord==50 ) occupied=new HashSet<>(Arrays.asList(coord-1,coord-10,coord));
        else                occupied=new HashSet<>(Arrays.asList(coord-1,coord-10,coord+10,coord+1,coord));
        return occupied;
    }

    static String generate(Set<Character> shapes, Set<Character> orientation, Set<Integer> board) {
        String test1 = new String();
        for (char shape : shapes) {
            for (char o : orientation) {
                for (int i : board) {
                    test1 = String.valueOf(shape) + String.valueOf(o) + String.valueOf(Pieces.decode(i));
                    if (Pieces.isPieceValid(test1)) return test1;

                }
            }
        }
        return test1;
    }



    static public Set<String> getNextPiece(char shape, String piece,Set<Integer> canUse){
        Set<Character> orientation = new HashSet<>(Arrays.asList('A','B','C','D','E','F','G','H'));
        Set<Integer> boundary = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,21,31,41,20,30,40,50,41,42,43,44,45,46,47,48,49));
        Set output = new HashSet<>();
        Set<Integer> test = new HashSet<>(canUse);
        Set<Integer> occupied = new HashSet<>();
        // return a set of integer represent the coordinate which have been occupied.
            for (int i=0;i<piece.length();i+=3) {
            occupied.addAll(Pieces.returnArray(piece.substring(i,i+3)));
            }
            test.removeAll(occupied);
            test = rearrange(test);
            //test.addAll(boundary);
            //if (shape=='B'||shape=='E') {
            for (char o : orientation) {
                for (int i : test) {
                    String test1 = String.valueOf(shape) + String.valueOf(o) + String.valueOf(Pieces.decode(i));
                    if (/*StepsGame.isPlacementSequenceValid(test1)) {*/Pieces.isPieceValid(test1)) {
                        if (test.containsAll(Pieces.returnArray(test1))) {
                            output.add(piece + test1);
                        }
                    }
                }
            }
            /*} else {
                test.removeAll(boundary);
                for (char o : orientation) {
                    for (int i : test) {
                        String test1 = String.valueOf(shape) + String.valueOf(o) + String.valueOf(Pieces.decode(i));
                        //if (/*StepsGame.isPlacementSequenceValid(test1)) {*Pieces.isPieceValid(test1)) {
                            //if (test.containsAll(Pieces.returnArray(test1))) {
                                output.add(piece + test1);
                            //}
                        //}
                    }
                }
            }*/
        return output;
    }
    // get all usable placements
    static public Set<String> getPieces(char shape, Set<String> input,Set<Integer> canUse){
        Set<String> output = new HashSet<>();
        for (String s:input) {
            output.addAll(getNextPiece(shape,s,canUse));
        }
        return output;
    }

    static public Set<String> getSols(ArrayList<Character> unplacedShapes ,Set<Integer> canUse){
        Set<String> input = new HashSet<>(Arrays.asList(""));
        int size =unplacedShapes.size();
        for (int i=0;i<size; i++) {
                input = getPieces(unplacedShapes.get(0), input, canUse);
                unplacedShapes.remove(0);
        }
        return input;
    }

    static public boolean CanBePlaced(String objective,String placement){
        if(placement.length()==objective.length()){
            return true;
        }else{
            ArrayList<String> test = new ArrayList<>(StepsGame.getViablePiecePlacements(placement,objective));
            if(!test.isEmpty()) return CanBePlaced(objective,placement+test.get(0));
            else return false;
        }
    }


    public static void main(String[] args) {
        Set<Character> orientation = new HashSet<>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'));
        Set<Integer> board1 = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50));
        Set<Integer> boundary = new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,21,31,41,20,30,40,50,41,42,43,44,45,46,47,48,49));
        Set<Character> shapes = new HashSet<>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'));
        List<Set<Character>> list0 = new ArrayList<>();
        Set list00 = new HashSet();
        list0.add(shapes);
        list0.add(orientation);
        String placement1 = "CEQ";
        String placement4 = "CEQEHu";
        String placement = "DFOGGQEDI";
        String placement3 = "FCLEBx";
        Set<Integer> board = rearrange(Help.canUse(placement));
        //board.addAll(boundary);
        ArrayList<Character> unplacedShapes = new ArrayList<>(getUnusedShapes(placement));
        System.out.println(generate(shapes,orientation,board1));

        Set<String> list = new HashSet<>(getSols(unplacedShapes,board));
        Set<String> list1 = new HashSet<>();
        for (String i:list) {
            if(CanBePlaced((placement+i),placement))  {
                list1.add(i);
            }
        }
        for(String i:list1)
            System.out.println(placement+i);

    }


}


