package ch.gameoflife;


import javax.swing.*;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class GameOfLife {

    private GUI gui;
    private CurrentGameState gameState;
    private CurrentCameraState cameraState;
    private Board board;
    private FrameMouseListener mouseAdapter;
    private FrameKeyListener keyAdapter;
    private Queue<long[]> nodesToFlip;
    private List<long[]> allAlive;

    public GameOfLife(){

        this.board = new Board();
        this.nodesToFlip = new LinkedBlockingQueue<>();
        int gridSize = (int) Math.sqrt(board.getSize());

//        addRandomStartPattern();

        this.allAlive = board.getAllAlive();
        this.gameState = new CurrentGameState(false);
        int panelLength = 1024;
        this.cameraState = new CurrentCameraState(panelLength / gridSize, gridSize, panelLength);
        this.mouseAdapter = new FrameMouseListener(this.nodesToFlip, this.cameraState);
        this.keyAdapter = new FrameKeyListener(this.gameState, this.cameraState);
        this.cameraState.setPixelSize(this.cameraState.getPanelLength() / this.cameraState.getGridSize());
        this.gui = new GUI(this.cameraState, this.allAlive, this.mouseAdapter, this.keyAdapter);
        start();
    }

    public void addRandomStartPattern(){
        Random random = new Random( 69420 );
        for ( int i = 1; i < 1000; i++ )
        {
            for ( int j = 1; j < 1000; j++ )
            {
                if ( random.nextDouble() > 0.5 )
                {
                    this.board.flipIsAlive(i, j);
                }
            }
        }
    }

    public void start(){
        while (true){
            while (!this.nodesToFlip.isEmpty()){
                long[] coordinate = this.nodesToFlip.poll();
                this.board.flipIsAlive(coordinate[0], coordinate[1]);
                this.allAlive.clear();
                this.allAlive.addAll(this.board.getAllAlive());
                this.cameraState.setGridSize((int) Math.sqrt(board.getSize()));
                SwingUtilities.invokeLater(this.gui::repaint);
            }


            while (this.gameState.isRunning()){
                long start = System.nanoTime();
                this.board.runStep();
                long end = System.nanoTime();
                double loopMilliseconds = (double) (end - start) / 1000000;
                double loopsPerSec = (1000 / loopMilliseconds);
                System.out.println("time for loop: " + loopMilliseconds + "milliseconds");
                System.out.println("loops per second: " + loopsPerSec);

                this.cameraState.setGridSize((int) Math.sqrt(this.board.getSize()));
                this.cameraState.offsetCameraIfGridSizeIncreased();
                this.allAlive.clear();
                this.allAlive.addAll(this.board.getAllAlive());
                SwingUtilities.invokeLater(this.gui::repaint);
            }



//            this.gameState.setPixelSize(1024 / this.gridSize);
            SwingUtilities.invokeLater(this.gui::repaint);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }


        }
    }


}
