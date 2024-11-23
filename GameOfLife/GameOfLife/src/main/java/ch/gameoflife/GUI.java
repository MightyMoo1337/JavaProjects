package ch.gameoflife;

import javax.swing.*;
import java.util.Queue;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;



public class GUI extends JPanel{

    private CurrentCameraState cameraState;
    private FrameMouseListener mouseAdapter;
    private FrameKeyListener keyAdapter;
    private List<long[]> allAlive;




    public GUI(CurrentCameraState cameraState, List<long[]> allAlive, FrameMouseListener mouseAdapter, FrameKeyListener keyAdapter){
        this.cameraState = cameraState;
        this.allAlive = allAlive;
        this.mouseAdapter = mouseAdapter;
        this.mouseAdapter.setRepaintTrigger(this::repaint);
        this.keyAdapter = keyAdapter;
        setupJPanel();
    }

    private void setupJPanel() {
        JFrame jFrame = new JFrame();
        jFrame.add(this);
        jFrame.pack();
        jFrame.setSize(this.cameraState.getPanelLength(), this.cameraState.getPanelLength());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        addMouseListener(this.mouseAdapter);
        addMouseMotionListener(this.mouseAdapter);
        addKeyListener(this.keyAdapter);

        requestFocus();
    }

    public void start(){
//        while (true){
//            while (!this.nodesToFlip.isEmpty()){
//                int[] coordinate = this.nodesToFlip.poll();
//                this.board.flipIsAlive(coordinate[0], coordinate[1]);
//                this.allAlive = this.board.getAllAlive();
//                this.cameraState.setGridSize((int) Math.sqrt(board.getSize()));
//
//            }
//
//
//            while (this.gameState.isRunning()){
//                long start = System.nanoTime();
//                this.board.runStep();
//                long end = System.nanoTime();
//                double loopMilliseconds = (double) (end - start) / 1000000;
//                double loopsPerSec = (1000 / loopMilliseconds);
//                System.out.println("time for loop: " + loopMilliseconds + "milliseconds");
//                System.out.println("loops per second: " + loopsPerSec);
//
//                this.cameraState.setGridSize((int) Math.sqrt(this.board.getSize()));
//                this.cameraState.offsetCameraIfGridSizeIncreased();
//                this.allAlive = this.board.getAllAlive();
//                SwingUtilities.invokeLater(this::repaint);
//            }
//
//
//
////            this.gameState.setPixelSize(1024 / this.gridSize);
//            SwingUtilities.invokeLater(this::repaint);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//
//            }
//
//
//        }
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        var pos = getMousePosition();
        Color veryLightGray = new Color(230, 230, 230);
        g.setColor(veryLightGray);
        if (pos != null) {
            g.fillRect(pos.x / cameraState.getPixelSize() * cameraState.getPixelSize(), 0, cameraState.getPixelSize(), this.cameraState.getGridSize() * cameraState.getPixelSize());
            g.fillRect(0, pos.y / cameraState.getPixelSize() * cameraState.getPixelSize(), this.cameraState.getGridSize() * cameraState.getPixelSize(), cameraState.getPixelSize());
        }

        Color lightGray = new Color(200, 200, 200);
        g.setColor(lightGray);
        if (pos != null) {
            g.fillRect(pos.x / cameraState.getPixelSize() * cameraState.getPixelSize(), pos.y / cameraState.getPixelSize() * cameraState.getPixelSize(), cameraState.getPixelSize(), cameraState.getPixelSize());
        }

        g.setColor(Color.BLACK);


        // Define the number of nodes to draw in the visible window
        int nodesToDrawX = this.cameraState.getPanelLength() / this.cameraState.getPixelSize();
        int nodesToDrawY = this.cameraState.getPanelLength() / this.cameraState.getPixelSize();

        // Define the starting grid coordinates based on camera offsets
        int startCol = this.cameraState.getCameraX() / this.cameraState.getPixelSize();
        int startRow = this.cameraState.getCameraY() / this.cameraState.getPixelSize();

        if (!this.allAlive.isEmpty()) {
            for (long[] coord : this.allAlive) {
                if (coord[0] >= startCol && coord[0] <= (startCol + nodesToDrawX)) {
                    if (coord[1] >= startRow && coord[1] <= (startRow + nodesToDrawY)) {
                        int x = (int) coord[0] * this.cameraState.getPixelSize() - this.cameraState.getCameraX();
                        int y = (int) coord[1] * this.cameraState.getPixelSize() - this.cameraState.getCameraY();

                        g.fillRect(x, y, this.cameraState.getPixelSize(), this.cameraState.getPixelSize());
                    }
                }
            }
        }
    }
}