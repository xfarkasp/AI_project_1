import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int levels = 3;
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>(levels);

        for(int i=0; i < levels; i++) {
            graph.add(new ArrayList());
        }
        int element = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                element++;
                graph.get(i).add(element);
            }
        }
        (graph.get(2)).set(2,0);

        //force change 0 to row 1 last position
        graph.get(0).set(graph.size()-1, 0);
        graph.get(2).set(graph.size()-1, 8);


        System.out.println("Before oneUp: ");
        for(int i = 0; i < graph.size(); i++){
            for(int j = 0; j < graph.get(i).size(); j++)
                System.out.print(graph.get(i).get(j) + " ");
            System.out.println();
        }

        Puzzler puzzle = new Puzzler();
        puzzle.oneUp(graph);

        System.out.println("After oneUp at lvl not 0: ");
        for(int i = 0; i < graph.size(); i++){
            for(int j = 0; j < graph.get(i).size(); j++)
                System.out.print(graph.get(i).get(j) + " ");
            System.out.println();
        }

        System.out.println(graph.indexOf(0));
    }
}