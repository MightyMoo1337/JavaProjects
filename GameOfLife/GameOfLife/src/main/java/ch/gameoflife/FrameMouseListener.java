package ch.gameoflife;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.Queue;

public class FrameMouseListener implements MouseListener, MouseMotionListener {
    private Queue<long[]> pixelsToFlip;
    private CurrentCameraState cameraState;
    private Runnable repaintTrigger;

    public FrameMouseListener(Queue<long[]> pixelsToFlip, CurrentCameraState cameraState) {
        this.pixelsToFlip = pixelsToFlip;
        this.cameraState = cameraState;
        this.repaintTrigger = null;
    }

    public void setRepaintTrigger(Runnable repaintTrigger){
        this.repaintTrigger = repaintTrigger;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            long[] coordinates = new long[2];
            int xCoord = (e.getX() + this.cameraState.getCameraX()) / cameraState.getPixelSize();
            int yCoord = (e.getY() + this.cameraState.getCameraY()) / cameraState.getPixelSize();
            coordinates[0] = xCoord;
            coordinates[1] = yCoord;
            System.out.println("MouseEvent: " + e + "coordinates: " + Arrays.toString(coordinates));
            this.pixelsToFlip.offer(coordinates);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3){
            System.out.println("Mouse pressed at: " + e.getX() + ", " + e.getY());
            this.cameraState.setLastMouseX(e.getX());
            this.cameraState.setLastMouseY(e.getY());
            System.out.println("gameState mouse pressed at: " + this.cameraState.getLastMouseX() +", " + this.cameraState.getLastMouseY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3){
            System.out.println("Mouse release detected");
            this.cameraState.adjustSubPixelCameraOffset();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        int maxSpeed = this.cameraState.getPixelSize(); // Limit the maximum movement speed per drag
        int dx = e.getX() - this.cameraState.getLastMouseX();
        int dy = e.getY() - this.cameraState.getLastMouseY();

//        if (Math.abs(dx) > maxSpeed) {
//            dx = dx > 0 ? maxSpeed : -maxSpeed;
//        }
//        if (Math.abs(dy) > maxSpeed) {
//            dy = dy > 0 ? maxSpeed : -maxSpeed;
//        }

        this.cameraState.adjustCameraX(dx);
        this.cameraState.adjustCameraY(dy);

        this.cameraState.setLastMouseX(e.getX());
        this.cameraState.setLastMouseY(e.getY());

        this.repaintTrigger.run();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
