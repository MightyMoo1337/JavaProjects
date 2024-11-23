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
    private CurrentGameState gameState;
    private FrameMouseListener mouseAdapter;
    private FrameKeyListener keyAdapter;


    public GUI(CurrentCameraState cameraState, CurrentGameState gameState, FrameMouseListener mouseAdapter, FrameKeyListener keyAdapter){
        this.cameraState = cameraState;
        this.gameState = gameState;
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

        if (!this.gameState.getAllAlive().isEmpty()) {
            for (long[] coord : this.gameState.getAllAlive()) {
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