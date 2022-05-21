package Sprites;

import java.awt.*;

public class Block extends SpriteAnimation{

    private int health = 1;
    private boolean ignoreCollision = false;
    private boolean blowUpBullet = true;
    private int starLevel = 0;

    public Block(int x, int y) {
        super(x, y);
    }

    public void lowerHealth() {
        health -= 1;
        if (health <= 0) {
            setVisible(false);
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public void updateAnimation() {
        loadImage(getImagePath());
    }

    public boolean isIgnoreCollision() {
        return ignoreCollision;
    }

    public boolean isIgnoreBulletCollision(int starLevel) {
        return (starLevel < this.starLevel);
    }

    public void setIgnoreCollision(boolean ignoreCollision) {
        this.ignoreCollision = ignoreCollision;
    }

    public void setIgnoreBulletCollision(int starLevel) {
        this.starLevel = starLevel;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public boolean isBlowUpBullet() {
        return blowUpBullet;
    }

    public void setBlowUpBullet(boolean blowUpBullet) {
        this.blowUpBullet = blowUpBullet;
    }

}
