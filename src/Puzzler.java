import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class Puzzler {

    private PuzzleNode root;
    final int unsolvableLimit = 1000000;
    private int numberOfNodes;
    private ArrayList<ArrayList<Integer>> puzzleStart;
    private ArrayList<ArrayList<Integer>> puzzleFinal;
    private ArrayList<PuzzleNode> uniqueNodes;
    private ArrayList<PuzzleNode> alreadyTried;


    public Puzzler(ArrayList<ArrayList<Integer>> puzzleStart, ArrayList<ArrayList<Integer>> puzzleFinal) {
        this.numberOfNodes = 0;
        this.puzzleStart = puzzleStart;
        this.puzzleFinal = puzzleFinal;
        this.root = new PuzzleNode(puzzleStart, getErrCount(puzzleStart, puzzleFinal));
        this.uniqueNodes = new ArrayList<>();
        this.alreadyTried = new ArrayList<>();
    }

    public int getErrCount(ArrayList<ArrayList<Integer>> puzzleStatus, ArrayList<ArrayList<Integer>> puzzleFinal){
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

    boolean isUnique(ArrayList<ArrayList<Integer>> puzzleStatus){
        for(PuzzleNode node: alreadyTried){
            if(node.puzzleStatus.equals(puzzleStatus))
                return false;
        }
        return true;
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
        uniqueNodes.add(root);
        alreadyTried.add(root);
        while (true){
            int bigestGreed = Integer.MAX_VALUE;
            PuzzleNode nextNode = null;
            //variation 1
            ArrayList<ArrayList<Integer>> alteredPuzzle = oneUp(listCloner(currentNode.puzzleStatus));
            if(alteredPuzzle != null){

                PuzzleNode variation1 = new PuzzleNode(alteredPuzzle, getErrCount(alteredPuzzle, this.puzzleFinal));
                if(isUnique(variation1.puzzleStatus)) {
                    currentNode.child1 = variation1;
                    if(currentNode.child1.greedCount < bigestGreed){
                        bigestGreed = currentNode.child1.greedCount;
                        nextNode = currentNode.child1;
                        numberOfNodes++;
                    }
                }
            }
            //variation 2
            alteredPuzzle = oneDown(listCloner(currentNode.puzzleStatus));
            if(alteredPuzzle != null){
                PuzzleNode variation2 = new PuzzleNode(alteredPuzzle, getErrCount(alteredPuzzle, this.puzzleFinal));
                if(isUnique(variation2.puzzleStatus)) {
                    currentNode.child2 = variation2;
                    if(currentNode.child2.greedCount < bigestGreed){
                        bigestGreed = currentNode.child2.greedCount;
                        nextNode = currentNode.child2;
                        numberOfNodes++;
                    }
                }
            }
            //variation 3
            alteredPuzzle = oneRight(listCloner(currentNode.puzzleStatus));
            if(alteredPuzzle != null){
                PuzzleNode variation3 = new PuzzleNode(alteredPuzzle, getErrCount(alteredPuzzle, this.puzzleFinal));
                if(isUnique(variation3.puzzleStatus)) {
                    currentNode.child3 = variation3;
                    if(currentNode.child3.greedCount < bigestGreed){
                        bigestGreed = currentNode.child3.greedCount;
                        nextNode = currentNode.child3;
                        numberOfNodes++;
                    }
                }
            }
            //variation 4
            alteredPuzzle = oneLeft(listCloner(currentNode.puzzleStatus));
            if(alteredPuzzle != null){
                PuzzleNode variation4 = new PuzzleNode(alteredPuzzle, getErrCount(alteredPuzzle, this.puzzleFinal));
                if(isUnique(variation4.puzzleStatus)) {
                    uniqueNodes.add(variation4);
                    currentNode.child4 = variation4;
                    if(currentNode.child4.greedCount < bigestGreed){
                        bigestGreed = currentNode.child4.greedCount;
                        nextNode = currentNode.child4;
                        numberOfNodes++;
                    }
                }
            }

            try{
                printPuzzleStatus(nextNode.puzzleStatus);
                if(currentNode.greedCount == 0)
                    System.out.println("Puzzle solved!");

                alreadyTried.add(currentNode);
                currentNode = nextNode;
            }
            catch (Exception e){
                System.out.println(e);
            }

        }
        //System.out.println("unsolved");
    }
    //index 0 = row, index 1 = col
    ArrayList<Integer> emptyPosition(ArrayList<ArrayList<Integer>> puzzleStatus){
        //find index of empty
        ArrayList<Integer> positions = new ArrayList<>();
        for(int i = 0; i < puzzleStatus.size(); i++){
            if(puzzleStatus.get(i).indexOf(0) != -1) {
                //System.out.println("Level: " + i + " position: " + graph.get(i).indexOf(0));
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
}

class PuzzleNode {
    int greedCount;

    PuzzleNode child1;
    PuzzleNode child2;
    PuzzleNode child3;
    PuzzleNode child4;
    protected ArrayList<ArrayList<Integer>> puzzleStatus;

    protected PuzzleNode(ArrayList<ArrayList<Integer>> puzzleStatus, int greedCount) {
        this.greedCount = greedCount;
        this.puzzleStatus = puzzleStatus;
    }
}
