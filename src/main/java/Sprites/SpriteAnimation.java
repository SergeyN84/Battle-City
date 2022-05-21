package Sprites;

import java.util.LinkedList;

public abstract class SpriteAnimation extends Sprite{

    private long initialTime = System.currentTimeMillis();

    public SpriteAnimation(int x, int y) {
        super(x, y);
    }

    public abstract void updateAnimation();

    public long getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }

    @Override
    public void loadImage(String src) {
        super.loadImage(src);
    }
}
