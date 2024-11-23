package ch.gameoflife;

public class CurrentCameraState {


    private int pixelSize;

    private int cameraX;
    private int cameraY;
    private int maxCameraOffsetX;
    private int maxCameraOffsetY;

    private int lastMouseX;
    private int lastMouseY;

    private final int panelLength;
    private int gridSize;
    private int previousGridSize;


    static final int MIN_PIXEL_SIZE = 1;
    static final int MAX_PIXEL_SIZE = 64;


    public CurrentCameraState(int pixelSize, int gridSize, int panelLength){
        this.pixelSize = pixelSize;
        this.panelLength = panelLength;
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        this.gridSize = gridSize;
        this.previousGridSize = this.gridSize;
        this.maxCameraOffsetX = this.gridSize * this.pixelSize - panelLength;
        this.maxCameraOffsetY = this.gridSize * this.pixelSize - panelLength;
    }

    public int getGridSize(){
        return this.gridSize;
    }

    public void setGridSize(int size){
        this.gridSize = size;
    }

    public void offsetCameraIfGridSizeIncreased(){
        if (this.gridSize != this.previousGridSize){
            int newCameraOffset = ((this.gridSize - this.previousGridSize) / 2) * this.pixelSize;
            //*-1 to apply the camera adjustment in the right direction
            adjustCameraX(newCameraOffset * -1);
            adjustCameraY(newCameraOffset * -1);
            this.previousGridSize = this.gridSize;
        }
    }

    public void adjustCameraX(int dx){
        this.maxCameraOffsetX = this.gridSize * this.pixelSize - panelLength;

        if (this.cameraX - dx < 0) {
            this.cameraX = 0;
        } else if (this.cameraX - dx > this.maxCameraOffsetX) {
            this.cameraX = maxCameraOffsetX;
        } else {
            this.cameraX -= dx;
        }
    }

    public void adjustCameraY(int dy){
        this.maxCameraOffsetY = this.gridSize * this.pixelSize - panelLength;

        if (this.cameraY - dy < 0) {
            this.cameraY = 0;
        } else if (this.cameraY - dy > this.maxCameraOffsetY) {
            this.cameraY = maxCameraOffsetY;
        } else {
            this.cameraY -= dy;
        }
    }

    public void adjustSubPixelCameraOffset(){
        int offsetX = this.cameraX % this.pixelSize;

        if (offsetX != 0){
            if (offsetX > this.pixelSize / 2){
                offsetX -= this.pixelSize;
            }
            this.cameraX -= offsetX;
        }
        int offsetY = this.cameraY % this.pixelSize;

        if (offsetY != 0){
            if (offsetY > this.pixelSize / 2){
                offsetY -= this.pixelSize;
            }
            this.cameraY -= offsetY;
        }
    }

    public int getCameraX(){
        return this.cameraX;
    }

    public int getCameraY(){
        return this.cameraY;
    }

    public int getLastMouseX(){
        return this.lastMouseX;
    }

    public int getLastMouseY(){
        return this.lastMouseY;
    }

    public void setLastMouseX(int x){
        this.lastMouseX = x;
    }

    public void setLastMouseY(int y){
        this.lastMouseY = y;
    }

    public void setPixelSize(int pixelSize){
        if (pixelSize < MIN_PIXEL_SIZE) {
            this.pixelSize = MIN_PIXEL_SIZE;
        } else if (pixelSize > MAX_PIXEL_SIZE) {
            this.pixelSize = MAX_PIXEL_SIZE;
        } else {
            this.pixelSize = pixelSize;
        }
    }

    public int getPixelSize(){
        return this.pixelSize;
    }

    public void increasePixelSize(int pixelSizeFactor) {

        if (this.pixelSize * pixelSizeFactor > MAX_PIXEL_SIZE) {
            this.pixelSize = MAX_PIXEL_SIZE;
        } else {
            this.pixelSize *= pixelSizeFactor;
            this.cameraX = Math.min(this.cameraX * pixelSizeFactor, this.maxCameraOffsetX);
            this.cameraY = Math.min(this.cameraY * pixelSizeFactor, this.maxCameraOffsetY);
            adjustSubPixelCameraOffset();
        }
    }
    public void decreasePixelSize(int pixelSizeFactor) {
        if (this.pixelSize / pixelSizeFactor < MIN_PIXEL_SIZE) {
            this.pixelSize = MIN_PIXEL_SIZE;
        } else {
            this.pixelSize /= pixelSizeFactor;
            this.cameraX = (this.cameraX / pixelSizeFactor);
            this.cameraY = (this.cameraY / pixelSizeFactor);
            adjustSubPixelCameraOffset();
        }
    }

    public int getPanelLength(){
        return this.panelLength;
    }
}
