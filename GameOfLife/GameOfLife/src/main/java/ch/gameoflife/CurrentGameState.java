package ch.gameoflife;

import java.util.ArrayList;
import java.util.List;

public class CurrentGameState {

    private boolean running;
    private List<long[]> allAlive;

    public CurrentGameState(boolean running, List<long[]> allAlive){
        this.running = running;
        this.allAlive = allAlive;
    }

    public void setAllAlive(List<long[]> newAllAlive){
        this.allAlive = newAllAlive;
    }

    public List<long[]> getAllAlive(){
        return this.allAlive;
    }

    public void flipRunning(){
        this.running = !this.running;
    }

    public boolean isRunning(){
        return this.running;
    }
}
