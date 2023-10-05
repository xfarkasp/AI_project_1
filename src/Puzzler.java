import org.w3c.dom.Node;

import java.util.ArrayList;

public class Puzzler {

    private PuzzleNode root;
    final int unsolvableLimit = 1000000;
    private int numberOfNodes;
    private ArrayList<ArrayList<Integer>> puzzleFinal;
    private ArrayList<PuzzleNode> uniqueNodes;


    public Puzzler(ArrayList<ArrayList<Integer>> puzzleStart, ArrayList<ArrayList<Integer>> puzzleFinal) {
        this.numberOfNodes = 0;
        this.puzzleFinal = puzzleFinal;
        this.root = new PuzzleNode(puzzleStart, getErrCount(puzzleStart, puzzleFinal));
    }

    public int getErrCount(ArrayList<ArrayList<Integer>> puzzleStatus, ArrayList<ArrayList<Integer>> puzzleFinal){
        int nokPosCount = 0;
        //compare the value on the same position from the current status to the same position at final status of the puzzle
        for(int i = 0; i < puzzleStatus.size() && i < puzzleFinal.size(); i++){
            for(int j = 0; j < puzzleStatus.get(i).size() && j < puzzleFinal.size(); j++){
                if(puzzleStatus.get(i).get(j) != puzzleFinal.get(i).get(j))
                    nokPosCount++;
            }
        }
        return nokPosCount;
    }

    boolean validator(ArrayList<ArrayList<Integer>> startPos, ArrayList<ArrayList<Integer>> endPosition){
        if(startPos == endPosition)
            return true;
        return false;
    }

    boolean isUnique(ArrayList<ArrayList<Integer>> puzzleStatus){
        for(PuzzleNode node: uniqueNodes){
            if(node.puzzleStatus == puzzleStatus)
                return true;
        }
        return false;
    }

    void puzzleSolver(ArrayList<ArrayList<Integer>> startPos, ArrayList<ArrayList<Integer>> endPosition){
        PuzzleNode currentNode = root;
        while (numberOfNodes != unsolvableLimit){
            int bigestGreed = -1;
            PuzzleNode nextNode = null;
            //variation 1
            ArrayList<ArrayList<Integer>> alteredPuzzle = oneUp(currentNode.puzzleStatus);
            if(alteredPuzzle != null){
                PuzzleNode variation1 = new PuzzleNode(alteredPuzzle, getErrCount(currentNode.puzzleStatus, this.puzzleFinal));
                if(isUnique(variation1.puzzleStatus)) {
                    uniqueNodes.add(variation1);
                    currentNode.child1 = variation1;
                    if(currentNode.child1.greedCount >= bigestGreed){
                        bigestGreed = currentNode.child1.greedCount;
                        nextNode = currentNode.child1;
                    }
                }
            }
            //variation 2
            alteredPuzzle = oneDown(currentNode.puzzleStatus);
            if(alteredPuzzle != null){
                PuzzleNode variation2 = new PuzzleNode(alteredPuzzle, getErrCount(currentNode.puzzleStatus, this.puzzleFinal));
                if(isUnique(variation2.puzzleStatus)) {
                    uniqueNodes.add(variation2);
                    currentNode.child2 = variation2;
                }
            }
            //variation 3
            alteredPuzzle = oneRight(currentNode.puzzleStatus);
            if(alteredPuzzle != null){
                PuzzleNode variation3 = new PuzzleNode(alteredPuzzle, getErrCount(currentNode.puzzleStatus, this.puzzleFinal));
                if(isUnique(variation3.puzzleStatus)) {
                    uniqueNodes.add(variation3);
                    currentNode.child3 = variation3;
                }
            }
            //variation 4
            alteredPuzzle = oneLeft(currentNode.puzzleStatus);
            if(alteredPuzzle != null){
                PuzzleNode variation4 = new PuzzleNode(alteredPuzzle, getErrCount(currentNode.puzzleStatus, this.puzzleFinal));
                if(isUnique(variation4.puzzleStatus)) {
                    uniqueNodes.add(variation4);
                    currentNode.child4 = variation4;
                }
            }

        }
    }
    //index 0 = row, index 1 = col
    ArrayList<Integer> emptyPosition(ArrayList<ArrayList<Integer>> puzzleStatus){
        //find index of empty
        ArrayList<Integer> positions = new ArrayList<>();
        System.out.println("Before oneUp: ");
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
            int tmp = puzzleStatus.get(positions.get(0)).get(positions.get(1));
            puzzleStatus.get(positions.get(0)).set(positions.get(1), tmp); //set the previous position of zero, to value of tmp
            puzzleStatus.get(positions.get(0)).set(positions.get(1) + 1, 0);
        }
        return puzzleStatus;
    }
    public ArrayList<ArrayList<Integer>> oneLeft(ArrayList<ArrayList<Integer>> puzzleStatus){
        //row and col position
        ArrayList<Integer> positions = emptyPosition(puzzleStatus);
        //if zero is in top row
        if(positions.get(1) == puzzleStatus.get(positions.get(0)).size() - 1){
            return null;
        }
        else{
            int tmp = puzzleStatus.get(positions.get(0)).get(positions.get(1));
            puzzleStatus.get(positions.get(0)).set(positions.get(1), tmp); //set the previous position of zero, to value of tmp
            puzzleStatus.get(positions.get(0)).set(positions.get(1) + 1, 0);
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
