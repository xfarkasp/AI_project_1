import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;

public class Main {
    static int getInvCount(int[] arr)
    {
        int inv_count = 0;
        for (int i = 0; i < 9; i++)
            for (int j = i + 1; j < 9; j++)

                // Value 0 is used for empty space
                if (arr[i] > 0 &&
                        arr[j] > 0 && arr[i] > arr[j])
                    inv_count++;
        return inv_count;
    }

    // This function returns true
// if given 8 puzzle is solvable.
    static boolean isSolvable(int[][] puzzle)
    {
        int linearPuzzle[];
        linearPuzzle = new int[9];
        int k = 0;

        // Converting 2-D puzzle to linear form
        for(int i=0; i<3; i++)
            for(int j=0; j<3; j++)
                linearPuzzle[k++] = puzzle[i][j];

        // Count inversions in given 8 puzzle
        int invCount = getInvCount(linearPuzzle);

        // return true if inversion count is even.
        return (invCount % 2 == 0);
    }
    public static void main(String[] args) {

        int[][] puzzle1 = {{1, 0, 2},{3, 4, 5},{8, 7, 6}};
        // in linear
        if(isSolvable(puzzle1))
            System.out.println("Solvable");
        else
            System.out.println("Not Solvable");

        int levels = 3;
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>(levels);
        ArrayList<ArrayList<Integer>> finalGraph = new ArrayList<>(levels);

        for(int i=0; i < levels; i++) {
            graph.add(new ArrayList());
            finalGraph.add(new ArrayList());
        }

        //start graph
        graph.get(0).add(1);
        graph.get(0).add(2);
        graph.get(0).add(3);

        graph.get(1).add(5);
        graph.get(1).add(6);
        graph.get(1).add(0);

        graph.get(2).add(7);
        graph.get(2).add(8);
        graph.get(2).add(4);

        //final graph
        finalGraph.get(0).add(1);
        finalGraph.get(0).add(2);
        finalGraph.get(0).add(3);

        finalGraph.get(1).add(5);
        finalGraph.get(1).add(8);
        finalGraph.get(1).add(6);

        finalGraph.get(2).add(0);
        finalGraph.get(2).add(7);
        finalGraph.get(2).add(4);

        //print status
        for(int i = 0; i < graph.size(); i++){
            for(int j = 0; j < graph.get(i).size(); j++)
                System.out.print(graph.get(i).get(j) + " ");
            System.out.println();
        }
        System.out.println();

       Puzzler puzzle = new Puzzler(graph, finalGraph, Puzzler.heuristics.H1);
        puzzle.solvable(graph);
        puzzle.puzzleSolver();
    }
}