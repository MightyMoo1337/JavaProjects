package ch.gameoflife;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class QuadtreeNodeTest {


    private static final Logger LOG = LoggerFactory.getLogger(QuadtreeNodeTest.class);

    @Test
    void createSingleCell(){
        QuadtreeNode node = QuadtreeNode.create();
        System.out.println(node.toString());
    }

    @Test
    void traverseInOrderTest1(){
        QuadtreeNode node = QuadtreeNode.create();
        List<Integer> allCells = new ArrayList<>();
        node.traverseInOrder(allCells);
        System.out.println(allCells.toString());
    }

    @Test
    void setIsALiveTest1(){
        QuadtreeNode node = QuadtreeNode.create();
        node = node.setIsAlive(0,0, true);
        node = node.setIsAlive(1,1, true);
        System.out.println(node.toString());
    }

    @Test
    void isAliveTest1(){
        QuadtreeNode node = QuadtreeNode.create();
        node = node.setIsAlive(2, 2, true);
        assertEquals(1, node.isAlive(2, 2));
        for (int x = 0; x < 16; x++){
            for (int y = 0; y < 16; y++){
                if (x != 2 && y != 2){
                    assertEquals(0, node.isAlive(x, y));
                }
            }
        }
    }

    @Test
    void nextGenTest1(){
        QuadtreeNode node = QuadtreeNode.create();
        node = node.setIsAlive(3, 3, true);
        node = node.setIsAlive(4,3, true);
        node = node.setIsAlive(2, 3, true);

        System.out.println(nodeToString(node));

        node = node.lookUpNextGen();
        node = node.expandBoard();
        System.out.println(nodeToString(node));

        node = node.lookUpNextGen();
        node = node.expandBoard();
        System.out.println(nodeToString(node));

    }

    @Test
    void getAllAliveTest1(){
        QuadtreeNode node = QuadtreeNode.create();
        node = node.setIsAlive(40, 40, true);
        node = node.setIsAlive(70, 70, true);
        List<long[]> allAlive = new ArrayList<>();
        node.getAllAlive(0, 0, allAlive);
        for (long[] coord : allAlive){
            System.out.println(Arrays.toString(coord));
        }
    }

    String nodeToString(QuadtreeNode node){
        List<Integer> allCells = new ArrayList<>();
        node.traverseRowMajor(allCells);
        String row = "";
        int rowLength = (int) Math.sqrt(allCells.size());
        for (int i = 1; i < allCells.size() + 1; i++){
            row += allCells.get(i - 1);
            if (i % rowLength == 0 && i != 0){
                row += "\n";
            }
        }
        return row;
    }

}