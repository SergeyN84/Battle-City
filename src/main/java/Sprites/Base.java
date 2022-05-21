package Sprites;

import Game.Board;
import Game.SoundUtility;

import java.util.LinkedList;

public class Base extends Block {

    private boolean showBaseDestroyed = false;
    private boolean BaseDestroyed = false;
    private LinkedList<SpriteAnimation> allAnimations;

    public Base(int x, int y, LinkedList<SpriteAnimation> allAnimations) {
        super(x, y);
        this.allAnimations = allAnimations;
    }

    @Override
    public void updateAnimation() {
        if (!BaseDestroyed) loadImage("image/base.png");
        else {
            loadImage("image/base_destroyed.png");
            if (!showBaseDestroyed) {
                allAnimations.add(new ExplosionBig(getX(), getY()));
                showBaseDestroyed = true;
                SoundUtility.explosion2();
                Board.setGameOver(true);
            }
        }
    }

    public void setBaseDestroyed(boolean baseDestroyed) {
        BaseDestroyed = baseDestroyed;
    }

    @Override
    public void setVisible(Boolean visible) {
        setBaseDestroyed(!visible);
    }

    public boolean isBaseDestroyed() {
        return BaseDestroyed;
    }
}
