package ch.gameoflife;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FrameKeyListener implements KeyListener {

    private CurrentGameState gameState;
    private CurrentCameraState cameraState;

    public FrameKeyListener(CurrentGameState gameState, CurrentCameraState cameraState){
        this.gameState = gameState;
        this.cameraState = cameraState;
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            this.gameState.flipRunning();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP){
            this.cameraState.increasePixelSize(2);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            this.cameraState.decreasePixelSize(2);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
