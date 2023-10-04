import java.util.ArrayList;

public class Puzzler {

    private PuzzleNode root;

    public Puzzler() {
        this.root = new PuzzleNode();
    }

    boolean validator(ArrayList<Integer> startPos, ArrayList<Integer> endPosition){

        return false;
    }

    void puzzleSolver(ArrayList<Integer> startPos, ArrayList<Integer> endPosition){

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

    void oneUp(ArrayList<ArrayList<Integer>> puzzleStatus){
        ArrayList<Integer> positions = emptyPosition(puzzleStatus);

        //oneUp
        if(positions.get(0) == 0){
            int tmp = puzzleStatus.get(puzzleStatus.size()-1).get(positions.get(1)); //get the value at the same position in the last row as is zero at first row
            puzzleStatus.get(0).set(positions.get(1), tmp); //set the previous position of zero, to value of tmp
            puzzleStatus.get(puzzleStatus.size()-1).set(positions.get(1), 0);
        }
        else{
            int tmp = puzzleStatus.get(positions.get(0) - 1).get(positions.get(1));
            puzzleStatus.get(positions.get(0)).set(positions.get(1), tmp); //set the previous position of zero, to value of tmp
            puzzleStatus.get(positions.get(0) - 1).set(positions.get(1), 0);
        }
    }
    void oneDown(ArrayList<Integer> position){}
    void oneLeft(ArrayList<Integer> position){}
    void oneRight(ArrayList<Integer> position){}
}

class PuzzleNode {
    int errNum;
    protected ArrayList<Integer> currStateOfPuzzle;
    protected ArrayList<PuzzleNode> currLvl;
    protected ArrayList<PuzzleNode> nextLvl;

    protected PuzzleNode() {
        this.errNum = errNum;
        this.currStateOfPuzzle = new ArrayList<>();
        this.currLvl = new ArrayList<>();
        this.nextLvl = new ArrayList<>();
    }
}
