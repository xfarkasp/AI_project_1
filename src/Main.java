import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static java.lang.Math.abs;

public class Main {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("Enter row count: ");
        int levels = input.nextInt();
        input.nextLine();

        System.out.println("Enter col count: ");
        int cols = input.nextInt();
        input.nextLine();

        ArrayList<ArrayList<Integer>> graph = new ArrayList<>(levels);
        ArrayList<ArrayList<Integer>> finalGraph = new ArrayList<>(levels);

        for(int i=0; i < levels; i++) {
            graph.add(new ArrayList());
            finalGraph.add(new ArrayList());
        }
        System.out.println("Start pos: ");
        String startPos = input.nextLine();
        System.out.println("End pos: ");
        String endPos = input.nextLine();

        input = new Scanner(startPos);
        Scanner inputFinal = new Scanner(endPos);

        for(int i = 0; i < levels; i++){
            for (int j = 0; j < cols; j++){
                graph.get(i).add(input.nextInt());
                finalGraph.get(i).add(inputFinal.nextInt());
            }
        }

        //print status
        for(int i = 0; i < graph.size(); i++){
            for(int j = 0; j < graph.get(i).size(); j++)
                System.out.print(graph.get(i).get(j) + " ");
            System.out.println();
        }
        System.out.println();

        Puzzler h3 = new Puzzler(graph, finalGraph, Puzzler.heuristics.H3);
        Puzzler h2 = new Puzzler(graph, finalGraph, Puzzler.heuristics.H2);
        Puzzler h1 = new Puzzler(graph, finalGraph, Puzzler.heuristics.H1);

        h2.puzzleSolver();
        h3.puzzleSolver();

        h1.puzzleSolver();
    }
}