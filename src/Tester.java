import java.util.ArrayList;

import static java.util.Collections.shuffle;
// Tester class used to generate random puzzle scenarios
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
        ArrayList<ArrayList<Integer>> start = createPuzzle(rows, cols);
        ArrayList<ArrayList<Integer>> goal = createPuzzle(rows, cols);

        System.out.println("Start: " + start);
        System.out.println("Goal: " + goal);

        Puzzler puzzleH1 = new Puzzler(start , goal, Puzzler.heuristics.H1);
        puzzleH1.puzzleSolver();

        Puzzler puzzleH2 = new Puzzler(start , goal, Puzzler.heuristics.H2);
        puzzleH2.puzzleSolver();
    }

    public static void main(String[] args){
        for(int i = 0; i < 1000; i++)
            test(3, 3);
    }
}
