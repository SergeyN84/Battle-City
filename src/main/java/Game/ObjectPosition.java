package Game;

public class ObjectPosition {

    private int x;
    private int y;

    public ObjectPosition(int xPos, int yPos) {
        this.x = xPos;
        this.y = yPos;
    }

    public int x() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int y() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
