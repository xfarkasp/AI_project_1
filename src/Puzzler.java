import org.w3c.dom.Node;

import java.util.*;

import static java.lang.Math.abs;

public class Puzzler {
    public enum heuristics {
        H1,
        H2
    }
    private PuzzleNode root;
    final int UNSOLVABLE_LIMIT = 1000000;
    private int numberOfNodes;
    private ArrayList<ArrayList<Integer>> puzzleStart;
    private ArrayList<ArrayList<Integer>> puzzleFinal;
    private ArrayList<PuzzleNode> alreadyGenerated;
    private ArrayList<PuzzleNode> alreadyTried;
    private heuristics usedH;


    public Puzzler(ArrayList<ArrayList<Integer>> puzzleStart, ArrayList<ArrayList<Integer>> puzzleFinal, heuristics h) {
        this.numberOfNodes = 0;
        this.puzzleStart = puzzleStart;
        this.puzzleFinal = puzzleFinal;
        this.usedH = h;
        this.root = new PuzzleNode(puzzleStart, h(puzzleStart, puzzleFinal,usedH), "Root", 0);
        this.alreadyGenerated = new ArrayList<>();
        this.alreadyTried = new ArrayList<>();
    }

    int h(ArrayList<ArrayList<Integer>> puzzleStatus, ArrayList<ArrayList<Integer>> puzzleFinal, heuristics h){
        switch (h){
            case H1:
                return h1(puzzleStatus, puzzleFinal);
            case H2:
                return h2(puzzleStatus, puzzleFinal);
        }
       return -1;
    }

    public int h1(ArrayList<ArrayList<Integer>> puzzleStatus, ArrayList<ArrayList<Integer>> puzzleFinal){
        int nokPositions = 0;
        //compare the value on the same position from the current status to the same position at final status of the puzzle
        for(int i = 0; i < puzzleStatus.size() && i < puzzleFinal.size(); i++){
            for(int j = 0; j < puzzleStatus.get(i).size() && j < puzzleFinal.get(i).size(); j++){
                if(puzzleFinal.get(i).get(j) != 0 && puzzleStatus.get(i).get(j) != puzzleFinal.get(i).get(j))
                    nokPositions++;
            }
        }
        return nokPositions;
    }

    public int h2(ArrayList<ArrayList<Integer>> puzzleStatus, ArrayList<ArrayList<Integer>> puzzleFinal){
        int distanceSum = 0;
        for(int i = 0; i < puzzleStatus.size(); i++){
            for(int j = 0; j < puzzleStatus.get(i).size(); j++) {
                int element = puzzleStatus.get(i).get(j);
                if (element != 0){
                    for (int level = 0; level < puzzleFinal.size(); level++) {
                        if (puzzleFinal.get(level).contains(element)) {
                            int indexOfScnd = puzzleFinal.get(level).indexOf(element);
                            int distance = (abs(i - level) + abs(j - indexOfScnd));
                            distanceSum += distance;
                        }
                    }
                }
            }
        }

        return distanceSum;
    }

    ArrayList<String> getPath(PuzzleNode node){
        ArrayList<String> path = new ArrayList<>();
        while (node.parent != null) {
            path.add(node.operationUsed);
            node = node.parent;
        }
        return path;
    }

    int isUnique(ArrayList<ArrayList<Integer>> puzzleStatus, ArrayList<PuzzleNode> uniqueList){
        for(PuzzleNode node: uniqueList){
            if(node.puzzleStatus.equals(puzzleStatus))
                return uniqueList.indexOf(node);
        }
        return -1;
    }

    void printPuzzleStatus(ArrayList<ArrayList<Integer>> puzzleStatus){
        //print status
        for(int i = 0; i < puzzleStatus.size(); i++){
            for(int j = 0; j < puzzleStatus.get(i).size(); j++)
                System.out.print(puzzleStatus.get(i).get(j) + " ");
            System.out.println();
        }
        System.out.println();
    }
    ArrayList<ArrayList<Integer>> listCloner(ArrayList<ArrayList<Integer>> original){
        ArrayList<ArrayList<Integer>> clone = new ArrayList<>();
        for(int i = 0; i < original.size(); i++){
            clone.add(new ArrayList<>());
            for (int j = 0; j < original.get(i).size(); j++)
                clone.get(i).add(original.get(i).get(j));
        }
        return clone;
    }

    void puzzleSolver(){
        PuzzleNode currentNode = root;
        //live que
        PriorityQueue<PuzzleNode> liveQue = new PriorityQueue<>(new comp());
        liveQue.add(root);
        alreadyGenerated.add(root);

        int depth = 1;
        int tryAgainIndex = 0;



        while (numberOfNodes != UNSOLVABLE_LIMIT){
            currentNode = liveQue.peek();
            liveQue.poll();

            if(currentNode.greedCount == 0) {
                System.out.println("Puzzle solved!");
                ArrayList<String> operations = getPath(currentNode);
                Collections.reverse(operations);
                System.out.println(operations);
                System.out.println(operations.size());
                return;
            }

            //variation 1 UP
            ArrayList<ArrayList<Integer>> alteredPuzzle = oneUp(listCloner(currentNode.puzzleStatus));

            if(alteredPuzzle != null){

                PuzzleNode variation1 = new PuzzleNode(alteredPuzzle, h(alteredPuzzle, this.puzzleFinal, this.usedH), "UP", depth);

                currentNode.child1 = variation1;
                currentNode.child1.parent = currentNode;
                liveQue.add(currentNode.child1);
                numberOfNodes++;

            }
            //variation 2 DOWN
            alteredPuzzle = oneDown(listCloner(currentNode.puzzleStatus));
            if(alteredPuzzle != null){
                PuzzleNode variation2 = new PuzzleNode(alteredPuzzle, h(alteredPuzzle, this.puzzleFinal, this.usedH), "DOWN", depth);

                currentNode.child2 = variation2;
                currentNode.child2.parent = currentNode;
                liveQue.add(currentNode.child2);
                numberOfNodes++;

            }
            //variation 3 RIGHT
            alteredPuzzle = oneRight(listCloner(currentNode.puzzleStatus));
            if(alteredPuzzle != null) {
                PuzzleNode variation3 = new PuzzleNode(alteredPuzzle, h(alteredPuzzle, this.puzzleFinal, this.usedH), "RIGHT", depth);

                currentNode.child3 = variation3;
                currentNode.child3.parent = currentNode;
                liveQue.add(currentNode.child3);
                numberOfNodes++;

            }
            //variation 4 LEFT
            alteredPuzzle = oneLeft(listCloner(currentNode.puzzleStatus));
            if(alteredPuzzle != null){
                PuzzleNode variation4 = new PuzzleNode(alteredPuzzle, h(alteredPuzzle, this.puzzleFinal, this.usedH), "LEFT", depth);

                currentNode.child4 = variation4;
                currentNode.child4.parent = currentNode;
                liveQue.add(currentNode.child4);
                numberOfNodes++;

            }

        }
        System.out.println("Unsolvable, number of nodes is larger than 10M");
    }
    //index 0 = row, index 1 = col
    ArrayList<Integer> emptyPosition(ArrayList<ArrayList<Integer>> puzzleStatus){
        //find index of empty
        ArrayList<Integer> positions = new ArrayList<>();
        for(int i = 0; i < puzzleStatus.size(); i++){
            if(puzzleStatus.get(i).indexOf(0) != -1) {
                positions.add(i);
                positions.add(puzzleStatus.get(i).indexOf(0));
            }
        }
        return positions;
    }

    public ArrayList<ArrayList<Integer>> oneUp(ArrayList<ArrayList<Integer>> puzzleStatus){
        //row and col position
        ArrayList<Integer> positions = emptyPosition(puzzleStatus);
        //if zero is in top row
        if(positions.get(0) == 0){
            return null;
        }
        else{
            int tmp = puzzleStatus.get(positions.get(0) - 1).get(positions.get(1));
            puzzleStatus.get(positions.get(0)).set(positions.get(1), tmp); //set the previous position of zero, to value of tmp
            puzzleStatus.get(positions.get(0) - 1).set(positions.get(1), 0);
        }
        return puzzleStatus;
    }
    public ArrayList<ArrayList<Integer>> oneDown(ArrayList<ArrayList<Integer>> puzzleStatus){
        //row and col position
        ArrayList<Integer> positions = emptyPosition(puzzleStatus);
        //if zero is in top row
        if(positions.get(0) == puzzleStatus.size()-1){
            return null;
        }
        else{
            int tmp = puzzleStatus.get(positions.get(0) + 1).get(positions.get(1));
            puzzleStatus.get(positions.get(0)).set(positions.get(1), tmp); //set the previous position of zero, to value of tmp
            puzzleStatus.get(positions.get(0) + 1).set(positions.get(1), 0);
        }
        return puzzleStatus;
    }
    public ArrayList<ArrayList<Integer>> oneRight(ArrayList<ArrayList<Integer>> puzzleStatus){
        //row and col position
        ArrayList<Integer> positions = emptyPosition(puzzleStatus);
        //if zero is in top row
        if(positions.get(1) == puzzleStatus.get(positions.get(0)).size() - 1){
            return null;
        }
        else{
            int tmp = puzzleStatus.get(positions.get(0)).get(positions.get(1) + 1);
            puzzleStatus.get(positions.get(0)).set(positions.get(1), tmp); //set the previous position of zero, to value of tmp
            puzzleStatus.get(positions.get(0)).set(positions.get(1) + 1, 0);
        }
        return puzzleStatus;
    }
    public ArrayList<ArrayList<Integer>> oneLeft(ArrayList<ArrayList<Integer>> puzzleStatus){
        //row and col position
        ArrayList<Integer> positions = emptyPosition(puzzleStatus);
        //if zero is in top row
        if(positions.get(1) == 0){
            return null;
        }
        else{
            int tmp = puzzleStatus.get(positions.get(0)).get(positions.get(1) - 1);
            puzzleStatus.get(positions.get(0)).set(positions.get(1), tmp); //set the previous position of zero, to value of tmp
            puzzleStatus.get(positions.get(0)).set(positions.get(1) - 1, 0);
        }
        return puzzleStatus;
    }

    // Comparison object to be used to order the heap
    public static class comp implements Comparator<PuzzleNode> {
        @Override
        public int compare(PuzzleNode lhs, PuzzleNode rhs){
            return (lhs.greedCount + lhs.depth) > (rhs.greedCount+rhs.depth)?1:-1;
        }
    }
}

class PuzzleNode {
    int greedCount;
    String operationUsed;
    int depth;
    PuzzleNode child1;
    PuzzleNode child2;
    PuzzleNode child3;
    PuzzleNode child4;
    PuzzleNode parent;
    protected ArrayList<ArrayList<Integer>> puzzleStatus;

    protected PuzzleNode(ArrayList<ArrayList<Integer>> puzzleStatus, int greedCount, String operationUsed, int depth) {
        this.greedCount = greedCount;
        this.puzzleStatus = puzzleStatus;
        this.operationUsed = operationUsed;
        this.depth = depth;
    }


}
