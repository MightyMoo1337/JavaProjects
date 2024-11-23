package ch.gameoflife;

public class CurrentGameState {

    private boolean running;

    public CurrentGameState(boolean running){
        this.running = running;
    }

    public void flipRunning(){
        this.running = !this.running;
    }

    public boolean isRunning(){
        return this.running;
    }
}
