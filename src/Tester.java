import java.util.ArrayList;

import static java.util.Collections.shuffle;

public class Tester {
    public static ArrayList<ArrayList<Integer>> createPuzzle(int rows, int cols){
        ArrayList<ArrayList<Integer>> puzzle = new ArrayList<>();
        //initialize rows
        for(int i=0; i < rows; i++)
            puzzle.add(new ArrayList());

        //push values
        int element = 0;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                puzzle.get(i).add(element);
                element++;
            }
            shuffle(puzzle.get(i));
        }
        shuffle(puzzle);

        return puzzle;
    }

    public static void test(int rows, int cols){
        Puzzler puzzle = new Puzzler(createPuzzle(rows, cols), createPuzzle(rows, cols), Puzzler.heuristics.H2);
        puzzle.puzzleSolver();
    }

    public static void main(String[] args){
        test(4, 4);
    }
}
