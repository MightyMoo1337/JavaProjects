package ch.gameoflife;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
class BoardTest {

    private static final Logger LOG = LoggerFactory.getLogger(BoardTest.class);

    @Test
    void to2DStringTest1() {
        Board board = new Board();
        System.out.println(board.to2DString());
    }

    @Test
    void getSizeTest(){
        Board board = new Board();
        assertEquals(64, board.getSize());
    }

    @Test
    void setIsAliveTest1(){
        Board board = new Board();
        board.setIsAlive(0,0, true);
        board.setIsAlive(1, 1, true);
        board.setIsAlive(0,1, true);
        board.setIsAlive(1, 0, true);
        board.setIsAlive(2, 1, true);
        for (int i = 2; i < 8; i++) {
            board.setIsAlive(3, i, true);
        }
        assertEquals("11000000\n" +
                "11100000\n" +
                "00010000\n" +
                "00010000\n" +
                "00010000\n" +
                "00010000\n" +
                "00010000\n" +
                "00010000\n", board.to2DString());
    }
    @Test
    void setIsAliveTest2() {
        Board board = new Board();
        board.setIsAlive(0, 0, true);
        board.setIsAlive(1, 1, true);
        board.setIsAlive(0, 1, true);
        board.setIsAlive(1, 0, true);
        board.setIsAlive(2, 1, true);
        for (int i = 2; i < 8; i = i + 2) {
            board.setIsAlive(3, i, true);
        }
        assertEquals("11000000\n" +
                "11100000\n" +
                "00010000\n" +
                "00000000\n" +
                "00010000\n" +
                "00000000\n" +
                "00010000\n" +
                "00000000\n", board.to2DString());
    }

    @Test
    void runStepTest1(){
        Board board = new Board();
        board.setIsAlive(0,0, true);
        System.out.println(board.to2DString());
        board.runStep();
        System.out.println(board.to2DString());
    }

    @Test
    void runStepTest2(){
        Board board = new Board();
        board.setIsAlive(3,3, true);
        String boardBeforeRunStep = board.to2DString();
        board.runStep();
        assertEquals(boardBeforeRunStep, board.to2DString());
    }

    @Test
    void runStepTest3(){
        Board board = new Board();
        board.setIsAlive(3, 3, true);
        board.setIsAlive(4, 3, true);
        board.setIsAlive(3, 2, true);
        System.out.println(board.to2DString());
        board.runStep();
        System.out.println(board.to2DString());
        board.runStep();
        System.out.println(board.to2DString());
    }

    @Test
    void runStepTest4(){
        Board board = new Board();
        System.out.println(board.to2DString());
        board.setIsAlive(1, 1, true);
        board.setIsAlive(2, 1, true);
        board.setIsAlive(1, 2, true);
        System.out.println(board.to2DString());
        board.runStep();
        System.out.println(board.to2DString());
        board.runStep();
        System.out.println(board.to2DString());
    }

    @Test
    void runStepTest5(){
        Board board = new Board();
        board.setIsAlive(3, 5, true);
        board.setIsAlive(3, 3, true);
        board.setIsAlive(3, 4, true);
        board.runStep();
        assertEquals("0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000001110000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n", board.to2DString());
        board.runStep();
        assertEquals("0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000100000000\n" +
                "0000000100000000\n" +
                "0000000100000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n" +
                "0000000000000000\n", board.to2DString());
    }
  
}