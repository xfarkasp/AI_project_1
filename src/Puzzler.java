import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import static java.lang.Math.abs;
//class to solve the puzzle
public class Puzzler {
    //enumeration for heuristic type
    public enum heuristics {
        H1,
        H2,
        H3
    }
    private PuzzleNode root; //root node
    final int UNSOLVABLE_LIMIT = 1000000; //stop solving if node count is greater than 10M
    private int numberOfNodes;  //number of generated nodes

    private ArrayList<ArrayList<Integer>> puzzleFinal; //goal state
    private ArrayList<PuzzleNode> alreadyGenerated; // que of already generated nodes
    private ArrayList<PuzzleNode> alreadyTried; // array of already tried
    private heuristics usedH; // used heuristic of the class

    // constructor of PuzzleNode
    public Puzzler(ArrayList<ArrayList<Integer>> puzzleStart, ArrayList<ArrayList<Integer>> puzzleFinal, heuristics h) {
        this.numberOfNodes = 0;
        this.puzzleFinal = puzzleFinal;
        this.usedH = h;
        this.root = new PuzzleNode(puzzleStart, h(puzzleStart, puzzleFinal,usedH), "Root", 0);
        this.alreadyGenerated = new ArrayList<>();
        this.alreadyTried = new ArrayList<>();
    }
    // pick the heuristic based on the usedH member
    int h(ArrayList<ArrayList<Integer>> puzzleStatus, ArrayList<ArrayList<Integer>> puzzleFinal, heuristics h){
        switch (h){
            case H1:
                return h1(puzzleStatus, puzzleFinal);
            case H2:
                return h2(puzzleStatus, puzzleFinal);
            case H3:
                return h3(puzzleStatus, puzzleFinal);
        }
       return -1;
    }
    // calculate number of misplaced positions
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
    // sums up the number of horizontal and vertical moves needed to get each position to its final position
    public int h2(ArrayList<ArrayList<Integer>> puzzleStatus, ArrayList<ArrayList<Integer>> puzzleFinal){
        int distanceSum = 0;
        //go through every element in the matrice
        for(int i = 0; i < puzzleStatus.size(); i++){
            for(int j = 0; j < puzzleStatus.get(i).size(); j++) {
                //save current element and calculate the horizontal and vertical misplacement from destination state
                int element = puzzleStatus.get(i).get(j);
                if (element != 0){
                    for (int level = 0; level < puzzleFinal.size(); level++) {
                        //find the level of the current element
                        if (puzzleFinal.get(level).contains(element)) {
                            //calculate difference between current coordinates and final coordinates
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
    // h3 combines the return value from h1 and h2
    public int h3(ArrayList<ArrayList<Integer>> puzzleStatus, ArrayList<ArrayList<Integer>> puzzleFinal){
        return h2(puzzleStatus, puzzleFinal) + h1(puzzleStatus, puzzleFinal);
    }
    //get the path from the nodes based on traversing the parents
    //and pushing their operationUsed string member to path array list(must be reversed at the end)
    ArrayList<String> getPath(PuzzleNode node){
        ArrayList<String> path = new ArrayList<>();
        //parent is null when the current node is the root
        while (node.parent != null) {
            path.add(node.operationUsed);
            node = node.parent;
        }
        return path;
    }
    //calculate depth of the node based on the number of parents
    int calculateDepth(PuzzleNode node){
        int depth = 0;
        while (node.parent != null) {
            node = node.parent;
            depth++;
        }
        return depth;
    }
    //check if node is unique to prevent generation of duplicate nodes
    int isUnique(ArrayList<ArrayList<Integer>> puzzleStatus, ArrayList<PuzzleNode> uniqueList){
        //iterate through the array list from parameter
        for(PuzzleNode node: uniqueList){
            //if node is found, return its index
            if(node.puzzleStatus.equals(puzzleStatus))
                return uniqueList.indexOf(node);
        }
        return -1; //if node has not been found, return -1
    }
    //method for visualization (not used)
    void printPuzzleStatus(ArrayList<ArrayList<Integer>> puzzleStatus){
        //print status
        for(int i = 0; i < puzzleStatus.size(); i++){
            for(int j = 0; j < puzzleStatus.get(i).size(); j++)
                System.out.print(puzzleStatus.get(i).get(j) + " ");
            System.out.println();
        }
        System.out.println();
    }
    //creates a new list, based on the current matrices status in the node, so the original values are not altered in the node
    ArrayList<ArrayList<Integer>> listCloner(ArrayList<ArrayList<Integer>> original){
        ArrayList<ArrayList<Integer>> clone = new ArrayList<>();
        //goes through the original mat and copies its values to the new one
        for(int i = 0; i < original.size(); i++){
            clone.add(new ArrayList<>());
            for (int j = 0; j < original.get(i).size(); j++)
                clone.get(i).add(original.get(i).get(j));
        }
        return clone;
    }
    //puzzle solver method to solve puzzle
    void puzzleSolver(){
        long start = System.nanoTime(); // start timer
        PuzzleNode currentNode = root; //set current root to point to the root
        int depth = 0; //initialize depth
        // puzzle is unsolvable if node count reaches 10 M or the solving time is larger than 30 seconds
        while (numberOfNodes != UNSOLVABLE_LIMIT && ((System.nanoTime() - start) * Math.pow(10, -9) < 30)){
            // if the greed cost of the current node calculated form the used heuristic function is 0, the puzzle is solved
            if(currentNode.greedCount == 0) {
                System.out.println("Puzzle solved!");
                ArrayList<String> operations = getPath(currentNode);
                Collections.reverse(operations);
                System.out.println("Operations used: " + operations);
                System.out.println("Number of operations used: " + operations.size());

                long end = System.nanoTime();
                double timeSeconds = (end - start) * Math.pow(10, -9);
                System.out.println("Heuristic used: " + usedH);
                System.out.println("Execution time: " +timeSeconds + " seconds");
                System.out.println("Number of tested nodes: " + alreadyTried.size());
                System.out.println("Number of generated nodes: " + numberOfNodes);
                System.out.println("Depth: " + currentNode.depth);

                return;
            }
            depth++; //depth is incremented
            int bigestGreed = Integer.MAX_VALUE; // initial value to compare costs of newly generated nodes
            PuzzleNode nextNode = null;

            //variation 1 UP
            ArrayList<ArrayList<Integer>> alteredPuzzle = oneUp(listCloner(currentNode.puzzleStatus)); // clones the array from node and tries to perform operation requested
            //if operation can not be performed, null is returned and new node is not created
            if(alteredPuzzle != null){
                //create new altered puzzle node
                PuzzleNode variation1 = new PuzzleNode(alteredPuzzle, h(alteredPuzzle, this.puzzleFinal, this.usedH), "UP", depth);
                //trie if new node has already been tried
                if(isUnique(variation1.puzzleStatus, alreadyTried) == -1) {
                    //set new node as children of current node
                    currentNode.child1 = variation1;
                    currentNode.child1.parent = currentNode;
                    //add new node to que of generated nodes
                    alreadyGenerated.add(currentNode.child1);
                    numberOfNodes++;
                    //check if new nodes greed count is lower than the actual lowes greed count
                    if(currentNode.child1.greedCount < bigestGreed){
                        //find out if node has been already generated, return its index if yes
                        int indexOfGenerated = isUnique(variation1.puzzleStatus, alreadyGenerated);
                        if(indexOfGenerated > -1){
                            //if this node has already been generated, set current node to it from the que and recalculate its depth
                            nextNode = alreadyGenerated.get(indexOfGenerated);
                            nextNode.depth = calculateDepth(currentNode);
                            bigestGreed = nextNode.greedCount;
                        }
                        else{
                            // if the node was unique
                            bigestGreed = currentNode.child1.greedCount;
                            nextNode = currentNode.child1;
                        }
                    }
                }
            }
            //variation 2 DOWN
            alteredPuzzle = oneDown(listCloner(currentNode.puzzleStatus));
            if(alteredPuzzle != null){
                PuzzleNode variation2 = new PuzzleNode(alteredPuzzle, h(alteredPuzzle, this.puzzleFinal, this.usedH), "DOWN", depth);
                if(isUnique(variation2.puzzleStatus, alreadyTried) == -1) {
                    currentNode.child2 = variation2;
                    currentNode.child2.parent = currentNode;
                    alreadyGenerated.add(currentNode.child2);
                    numberOfNodes++;
                    if(currentNode.child2.greedCount < bigestGreed){
                        int indexOfGenerated = isUnique(variation2.puzzleStatus, alreadyGenerated);
                        if(indexOfGenerated > -1){
                            nextNode = alreadyGenerated.get(indexOfGenerated);
                            nextNode.depth = calculateDepth(currentNode);
                            bigestGreed = nextNode.greedCount;
                        }
                        else{
                            bigestGreed = currentNode.child2.greedCount;
                            nextNode = currentNode.child2;
                        }
                    }
                }
            }
            //variation 3 RIGHT
            alteredPuzzle = oneRight(listCloner(currentNode.puzzleStatus));
            if(alteredPuzzle != null) {
                PuzzleNode variation3 = new PuzzleNode(alteredPuzzle, h(alteredPuzzle, this.puzzleFinal, this.usedH), "RIGHT", depth);
                if (isUnique(variation3.puzzleStatus, alreadyTried) == -1) {
                    currentNode.child3 = variation3;
                    currentNode.child3.parent = currentNode;
                    alreadyGenerated.add(currentNode.child3);
                    numberOfNodes++;
                    if (currentNode.child3.greedCount < bigestGreed) {
                        int indexOfGenerated = isUnique(variation3.puzzleStatus, alreadyGenerated);
                        if (indexOfGenerated > -1) {
                            nextNode = alreadyGenerated.get(indexOfGenerated);
                            nextNode.depth = calculateDepth(currentNode);
                            bigestGreed = nextNode.greedCount;
                        } else {
                            bigestGreed = currentNode.child3.greedCount;
                            nextNode = currentNode.child3;
                        }
                    }
                }
            }
            //variation 4 LEFT
            alteredPuzzle = oneLeft(listCloner(currentNode.puzzleStatus));
            if(alteredPuzzle != null){
                PuzzleNode variation4 = new PuzzleNode(alteredPuzzle, h(alteredPuzzle, this.puzzleFinal, this.usedH), "LEFT", depth);
                if(isUnique(variation4.puzzleStatus, alreadyTried) == -1) {
                    currentNode.child4 = variation4;
                    currentNode.child4.parent = currentNode;
                    alreadyGenerated.add(currentNode.child4);
                    numberOfNodes++;
                    if (currentNode.child4.greedCount < bigestGreed) {
                        int indexOfGenerated = isUnique(variation4.puzzleStatus, alreadyGenerated);
                        if (indexOfGenerated > -1) {
                            nextNode = alreadyGenerated.get(indexOfGenerated);
                            nextNode.depth = calculateDepth(currentNode);
                            bigestGreed = nextNode.greedCount;
                        } else {
                            bigestGreed = currentNode.child4.greedCount;
                            nextNode = currentNode.child4;
                        }
                    }
                }
            }

            try{
                if(nextNode == null){
                    currentNode = alreadyGenerated.get(0);
                    alreadyGenerated.remove(currentNode);
                    depth = currentNode.depth;

                    continue;
                }

                alreadyTried.add(currentNode);
                alreadyGenerated.remove(currentNode);
                Collections.sort(alreadyGenerated, greedValue);
                currentNode = nextNode;
            }
            catch (Exception e){
                System.out.println(e);
                return;
            }

        }
        System.out.println("Unsolvable, number of nodes is larger than 10M");
    }

    // Comparator for sorting the list by best path
    public  Comparator<PuzzleNode> greedValue = new Comparator<PuzzleNode>() {
        public int compare(PuzzleNode node1, PuzzleNode node2) {
            int depth1 = node1.depth;
            int depth2 = node2.depth;

            if(node1.puzzleStatus.size() > 3 && Puzzler.this.usedH == heuristics.H1){
                depth1 = 0;
                depth2 = 0;
            }

            int greed1 = node1.greedCount + depth1;
            int greed2 = node2.greedCount + depth2;
            //return descending order
            return greed1 - greed2;
        }
    };

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
}

class PuzzleNode {
    int greedCount;
    int depth;
    String operationUsed;
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
