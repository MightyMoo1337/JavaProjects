package ch.gameoflife;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuadtreeNode {

    private final QuadtreeNode nw;
    private final QuadtreeNode ne;
    private final QuadtreeNode sw;
    private final QuadtreeNode se;
    private QuadtreeNode result;
    private final int level;
    private final boolean isAlive;
    private final double population;
    private static HashMap<QuadtreeNode, QuadtreeNode> hashMap = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(QuadtreeNode.class);

    private QuadtreeNode(boolean isAlive){
        this.nw = this.ne = this.sw = this.se = null;
        this.result = null;
        this.level = 0;
        this.isAlive = isAlive;
        this.population = this.isAlive ? 1.0 : 0.0;
    }

    private QuadtreeNode(QuadtreeNode nw, QuadtreeNode ne, QuadtreeNode sw, QuadtreeNode se){
        this.nw = nw;
        this.ne = ne;
        this.sw = sw;
        this.se = se;
        this.level = nw.level + 1;
        this.population = this.nw.population + this.ne.population + this.sw.population + this.se.population;
        this.isAlive = this.population > 0.0;
        this.result = null;
    }

    public final QuadtreeNode create(boolean isAlive){
        return new QuadtreeNode(isAlive).checkCache();
    }

    public final QuadtreeNode create(QuadtreeNode nw, QuadtreeNode ne, QuadtreeNode sw, QuadtreeNode se){
        return new QuadtreeNode(nw, ne, sw, se).checkCache();
    }

    static QuadtreeNode create() {
        return (new QuadtreeNode(false)).emptyTree(10);
    }

    public final double getSize(){
        return Math.pow(Math.pow(2, this.level), 2);
    }

    public final int getLevel(){
        return this.level;
    }

    public final QuadtreeNode getNw(){
        return this.nw;
    }

    public final QuadtreeNode getNe(){
        return this.ne;
    }
    public final QuadtreeNode getSw(){
        return this.sw;
    }
    public final QuadtreeNode getSe(){
        return this.se;
    }

    public final double getPopulation(){
        return this.population;
    }

    public final QuadtreeNode lookUpNextGen(){
        if (this.result == null){
            this.result = nextGen();
        }
        return result;
    }

    private final QuadtreeNode simulate(){
        List<Integer> allCells = new ArrayList<>();
        for (int row = 0; row < 4; row++){
            for (int col = 0; col < 4; col++){
                allCells.add(isAlive(col, row));
            }
        }
        //checking central 4 cells for neighbours and returning newly created central next generation
        return create(checkNeighbours(5, allCells),
                checkNeighbours(6, allCells),
                checkNeighbours(9, allCells),
                checkNeighbours(10, allCells));

    }

    private final QuadtreeNode checkNeighbours(int indexTarget, List<Integer> allCells){
        int aliveNeighbours = 0;
        // 4 x 4 region, index -5 is topLeft, -3 is topRight, -1/+1 are left/right, -3 is bottomLeft and -5 is bottomRight
        //excluding 0 (cell itself) and -2/+2 because those are not direct neighbours
        for (int index = -5; index < 6; index++){
            if (index != 2 && index != 0 && index != -2){
                if (allCells.get(indexTarget + index) == 1){
                    aliveNeighbours += 1;
                }
            }
        }
        if (aliveNeighbours == 3 || (allCells.get(indexTarget) == 1 && aliveNeighbours == 2)){
            return create(true);
        }else{
            return create(false);
        }
    }

    private final QuadtreeNode nextGen(){

        if (this.population == 0.0){
            return this.nw;
        }
        if (this.level == 2){
            return simulate();
        }

        QuadtreeNode topLeft = this.nw.centralSubnode();
        QuadtreeNode topMiddle = centralHorizontal(this.nw, this.ne);
        QuadtreeNode topRight = this.ne.centralSubnode();

        QuadtreeNode middleLeft = centralVertical(this.nw, this.sw);
        QuadtreeNode center = centralSubSubnode();
        QuadtreeNode middleRight = centralVertical(this.ne, this.se);

        QuadtreeNode bottomLeft = this.sw.centralSubnode();
        QuadtreeNode bottomMiddle = centralHorizontal(this.sw, this.se);
        QuadtreeNode bottomRight = this.se.centralSubnode();

        return create(create(topLeft, topMiddle, middleLeft, center).lookUpNextGen(),
                create(topMiddle, topRight, center, middleRight).lookUpNextGen(),
                create(middleLeft, center, bottomLeft, bottomMiddle).lookUpNextGen(),
                create(center, middleRight, bottomMiddle, bottomRight).lookUpNextGen());

    }

    private final QuadtreeNode centralSubnode(){
        return create(this.nw.se, this.ne.sw, this.sw.ne, this.se.nw);
    }

    private final QuadtreeNode centralSubSubnode(){
        return create(this.nw.se.se, this.ne.sw.sw, this.sw.ne.ne, this.se.nw.nw);
    }

    private final QuadtreeNode centralVertical(QuadtreeNode top, QuadtreeNode bottom){
        return create(top.sw.se, top.se.sw, bottom.nw.ne, bottom.ne.nw);
    }

    private final QuadtreeNode centralHorizontal(QuadtreeNode left, QuadtreeNode right){
        return create(left.ne.se, right.nw.sw, left.se.ne, right.sw.nw);
    }

    public final QuadtreeNode setIsAlive(long x, long y, boolean isAlive){
        if (this.level == 0){
            return new QuadtreeNode(isAlive);
        }
        double halfSize = Math.pow(2, this.level) / 2;

        if (x < halfSize && y < halfSize){
            return create(this.nw.setIsAlive(x, y, isAlive), this.ne, this.sw, this.se);
        }
        if (x >= halfSize && y < halfSize){
            return create(this.nw, this.ne.setIsAlive(x - (int) halfSize, y, isAlive), this.sw, this.se);
        }
        if (x < halfSize && y >= halfSize){
            return create(this.nw, this.ne, this.sw.setIsAlive(x, y - (int) halfSize, isAlive), this.se);
        }
        if (x >= halfSize && y >= halfSize){
            return create(this.nw, this.ne, this.sw, this.se.setIsAlive(x - (int) halfSize, y - (int) halfSize, isAlive));
        }
        return null;
    }

    public final QuadtreeNode flipIsAlive(long x, long y){
        if (this.level == 0){
            if (this.isAlive) {
                return new QuadtreeNode(false);
            }else{
                return new QuadtreeNode(true);
            }
        }
        double halfSize = Math.pow(2, this.level) / 2;

        if (x < halfSize && y < halfSize){
            return create(this.nw.flipIsAlive(x, y), this.ne, this.sw, this.se);
        }
        if (x >= halfSize && y < halfSize){
            return create(this.nw, this.ne.flipIsAlive(x - (int) halfSize, y), this.sw, this.se);
        }
        if (x < halfSize && y >= halfSize){
            return create(this.nw, this.ne, this.sw.flipIsAlive(x, y - (int) halfSize), this.se);
        }
        if (x >= halfSize && y >= halfSize){
            return create(this.nw, this.ne, this.sw, this.se.flipIsAlive(x - (int) halfSize, y - (int) halfSize));
        }
        return null;
    }


    public final int isAlive(long x, long y){
        if (this.level == 0){
            return this.isAlive ? 1 : 0;
        }

        double halfSize = Math.pow(2, this.level) / 2;

        if (x < halfSize && y < halfSize){
            return this.nw.isAlive(x, y);
        }
        if (x >= halfSize && y < halfSize){
            return this.ne.isAlive(x - (int) halfSize, y);
        }
        if (x < halfSize && y >= halfSize){
            return this.sw.isAlive(x, y - (int) halfSize);
        }
        if (x >= halfSize && y >= halfSize){
            return this.se.isAlive(x - (int) halfSize, y - (int) halfSize);
        }
        LOG.info("error occured while checking if cell isAlive; fallen through: x: {} y: {} halfSize: {}", x, y, halfSize);
        return -2;
    }

    public void getAllAlive(long x, long y, List<long[]> allAlive){
        if (this.population == 0.0){
            return;
        }
        if (this.level == 0){
            long[] coords = new long[2];
            coords[0] = x;
            coords[1] = y;
            allAlive.add(coords);
            return;
        }

        long halfSize = (long) Math.pow(2, this.level) / 2;

        if (this.nw.population > 0.0){
            this.nw.getAllAlive(x, y, allAlive);
        }
        if (this.ne.population > 0.0){
            this.ne.getAllAlive(x + halfSize, y, allAlive);
        }
        if (this.sw.population > 0.0){
            this.sw.getAllAlive(x, y + halfSize, allAlive);
        }
        if (this.se.population > 0.0){
            this.se.getAllAlive(x + halfSize, y + halfSize, allAlive);
        }

    }

    public final QuadtreeNode expandBoard() {
        QuadtreeNode emptyBorder = emptyTree(this.level - 1);
        return create(create(emptyBorder, emptyBorder, emptyBorder, this.nw),
                create(emptyBorder, emptyBorder, this.ne, emptyBorder),
                create(emptyBorder, this.sw, emptyBorder, emptyBorder),
                create(this.se, emptyBorder, emptyBorder, emptyBorder));
    }

    private final QuadtreeNode emptyTree(int level){
        if (level == 0){
            return this.create(false);
        }else{
            QuadtreeNode oneLevelUp = this.emptyTree(level - 1);
            return this.create(oneLevelUp, oneLevelUp, oneLevelUp, oneLevelUp);
        }
    }

    private QuadtreeNode checkCache(){
        QuadtreeNode nodeInCache = hashMap.get(this);
        if (nodeInCache != null){
            return nodeInCache;
        }
        hashMap.put(this, this);
        return this;
    }

    public void traverseInOrder(List<Integer> allCells){

        if (this.level == 0){
            allCells.add(isAlive ? 1 : 0);
            return;
        }

        this.nw.traverseInOrder(allCells);
        this.ne.traverseInOrder(allCells);
        this.sw.traverseInOrder(allCells);
        this.se.traverseInOrder(allCells);
    }

    public void traverseRowMajor(List<Integer> allCells) {
        int size = (int) Math.pow(2, this.level);  // The size of the grid represented by this node
        for (int row = 0; row < size; row++) {
            traverseRow(allCells, row, 0, size);
        }
    }

    // Helper method to traverse a specific row
    private void traverseRow(List<Integer> allCells, int targetRow, int startCol, int size) {
        if (this.level == 0) {
            // Base case: if at a leaf node, add its state to the list
            allCells.add(isAlive ? 1 : 0);
            return;
        }

        int halfSize = size / 2;

        // Determine which quadrants intersect with the target row
        if (targetRow < halfSize) {
            // Top half: traverse NW and NE quadrants
            this.nw.traverseRow(allCells, targetRow, startCol, halfSize);
            this.ne.traverseRow(allCells, targetRow, startCol + halfSize, halfSize);
        } else {
            // Bottom half: traverse SW and SE quadrants, adjusting row index for lower half
            this.sw.traverseRow(allCells, targetRow - halfSize, startCol, halfSize);
            this.se.traverseRow(allCells, targetRow - halfSize, startCol + halfSize, halfSize);
        }
    }


    @Override
    public String toString() {
        return toStringHelper(0);  // Start at level 0 (root)
    }

    // Helper method with indentation based on the level in the tree
    private String toStringHelper(int level) {
        StringBuilder sb = new StringBuilder();
        String indent = "  ".repeat(level);  // Indentation for each level

        if (this.nw == null && this.ne == null && this.sw == null && this.se == null) {
            sb.append(indent)
                    .append("Leaf: ")
                    .append(this.isAlive ? "Alive" : "Dead")
                    .append("\n");
        } else {
            sb.append(indent)
                    .append("Node:\n");
            if (this.nw != null) {
                sb.append(indent).append("  Quadrant ").append("nw").append(":\n");
                sb.append(this.nw.toStringHelper(level + 1));  // Recur for children
            }
            if (this.ne != null) {
                sb.append(indent).append("  Quadrant ").append("ne").append(":\n");
                sb.append(this.ne.toStringHelper(level + 1));  // Recur for children
            }
            if (this.sw != null) {
                sb.append(indent).append("  Quadrant ").append("sw").append(":\n");
                sb.append(this.sw.toStringHelper(level + 1));  // Recur for children
            }
            if (this.se != null) {
                sb.append(indent).append("  Quadrant ").append("se").append(":\n");
                sb.append(this.se.toStringHelper(level + 1));  // Recur for children
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        QuadtreeNode t = (QuadtreeNode) o;
        if (this.level != t.level){
            return false;
        }
        if (level == 0){
            return this.isAlive == t.isAlive;
        }
        return this.nw == t.nw && this.ne == t.ne && this.sw == t.sw && this.se == t.se;
    }

    @Override
    public int hashCode() {
        if (this.level==0){
            return (int) this.population;
        }
        return System.identityHashCode(this.nw) +
                11 * System.identityHashCode(this.ne) +
                101 * System.identityHashCode(this.sw) +
                1007 * System.identityHashCode(this.se);
    }
}
