import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        int levels = 3;
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>(levels);
        ArrayList<ArrayList<Integer>> finalGraph = new ArrayList<>(levels);

        for(int i=0; i < levels; i++) {
            graph.add(new ArrayList());
            finalGraph.add(new ArrayList());
        }

        //start graph
        graph.get(0).add(2);
        graph.get(0).add(8);
        graph.get(0).add(3);

        graph.get(1).add(1);
        graph.get(1).add(6);
        graph.get(1).add(4);

        graph.get(2).add(7);
        graph.get(2).add(0);
        graph.get(2).add(5);

        //final graph
        finalGraph.get(0).add(1);
        finalGraph.get(0).add(2);
        finalGraph.get(0).add(3);

        finalGraph.get(1).add(8);
        finalGraph.get(1).add(0);
        finalGraph.get(1).add(4);

        finalGraph.get(2).add(7);
        finalGraph.get(2).add(6);
        finalGraph.get(2).add(5);

        //print status
        for(int i = 0; i < graph.size(); i++){
            for(int j = 0; j < graph.get(i).size(); j++)
                System.out.print(graph.get(i).get(j) + " ");
            System.out.println();
        }
        System.out.println();

        Puzzler puzzle = new Puzzler(graph, finalGraph);
        puzzle.puzzleSolver();


    }
}