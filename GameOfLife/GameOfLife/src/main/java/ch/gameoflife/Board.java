package ch.gameoflife;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Board {

    private QuadtreeNode board;
    private static final Logger LOG = LoggerFactory.getLogger(Board.class);

    public Board(){
        this.board = QuadtreeNode.create();
    }

    public void flipIsAlive(long x, long y){
        this.board = this.board.flipIsAlive(x, y);
    }

    public void setIsAlive(long x, long y, boolean isALive){
        this.board = this.board.setIsAlive(x, y, isALive);
    }

    public int getIsAlive(long x, long y){
        return this.board.isAlive(x, y);
    }

    public long[][] getAllCells(){
        int gridLength = (int) Math.sqrt(getSize());
        long[][] allCells = new long[gridLength][gridLength];
        for (int row = 0; row < gridLength; row++){
            for (int col = 0; col < gridLength; col++){
                allCells[col][row] = this.board.isAlive(col, row);
            }
        }
        return allCells;
    }

    public List<long[]> getAllAlive(){
        List<long[]> allAlive = new ArrayList<>();
        this.board.getAllAlive(0L, 0L, allAlive);
        return allAlive;
    }

    public double getSize(){
        return this.board.getSize();
    }

    public final void runStep(){
        if (this.board.getLevel() < 3 ||
                this.board.getNw().getPopulation() != this.board.getNw().getSe().getSe().getPopulation() ||
                this.board.getNe().getPopulation() != this.board.getNe().getSw().getSw().getPopulation() ||
                this.board.getSw().getPopulation() != this.board.getSw().getNe().getNe().getPopulation() ||
                this.board.getSe().getPopulation() != this.board.getSe().getNw().getNw().getPopulation()) {

            this.board = this.board.expandBoard();
        }

        this.board = this.board.lookUpNextGen();
        this.board = this.board.expandBoard();

    }

    public String to2DString(){
        StringBuilder sb = new StringBuilder();
        List<Integer> allCells = new ArrayList<>();
        this.board.traverseRowMajor(allCells);
        double gridSize = Math.sqrt(allCells.size());
        LOG.info("Number of cells: {} and gridSize: {}", allCells.size(), gridSize);
        for (int row = 0; row < gridSize; row++){
            for (int col = 0; col < gridSize; col++){
                sb.append(allCells.getFirst());
                allCells.removeFirst();
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
