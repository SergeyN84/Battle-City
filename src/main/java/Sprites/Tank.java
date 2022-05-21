package Sprites;

import Game.Collisions;
import Game.Const;
import Game.SoundUtility;
import Enum.TankDirection;

import java.awt.*;
import java.util.LinkedList;

public abstract class Tank extends SpriteAnimation {

    private String caption = "";
    private long lastFired = 0;
    private int teamNumber;
    private int lives;
    private int dx;
    private int dy;
    private LinkedList<Bullet> bullets;
    private int starLevel = 0;
    private boolean shield = false;
    private int reset_x;
    private int reset_y;
    private TankDirection reset_tankDirection;
    private boolean ai = false;
    private boolean frozen = false;
    private long frozenStartTime = 0;


    private TankDirection tankDirection;

    public Tank(int x, int y, int lives, TankDirection tankDirection, int teamNumber) {
        super(x, y);
        this.lives = lives;
        this.tankDirection = tankDirection;
        this.teamNumber = teamNumber;
        bullets = new LinkedList<>();
        reset_tankDirection = tankDirection;
        reset_x = x;
        reset_y = y;
    }

    public static double calculateDistanceBetweenTank(Tank tank1, Tank tank2) {
        return Math.sqrt((tank1.getY() - tank2.getY()) * (tank1.getY() - tank2.getY()) + (tank1.getX() - tank2.getX()) * (tank1.getX() - tank2.getX()));
    }

    private void fireAndCheckTime() {
        Bullet bullet;
        switch (tankDirection) {
            case UP:
                bullet = new Bullet(x + getWidth()/3, getY(), getTeamNumber(), this);
                break;

            case DOWN:
                bullet = new Bullet(x + getWidth()/3, (getY() + getHeight())-3, getTeamNumber(), this);
                break;

            case RIGHT:
                bullet = new Bullet(x + getWidth()-3, getY() + getHeight()/3, getTeamNumber(), this);
                break;

            default:
                bullet = new Bullet(x, getY() + getHeight()/3, getTeamNumber(), this);
        }

        bullets.add(bullet);

        SoundUtility.fire();
    }

    public void fire() {

        if (!isVisible() || isFrozen()) {
            return;
        }

        int time;

        if (getStarLevel() == 0) {
            time = 700;
        } else {
            time = 250;
        }

        if ((System.currentTimeMillis() - lastFired) > time){
            fireAndCheckTime();
            lastFired = System.currentTimeMillis();
        }
    }

    @Override
    public abstract void updateAnimation();

    public void move() {

        if (!isVisible()) {
            return;
        }

        Rectangle theTank = new Rectangle(x + dx, y + dy, getWidth(), getHeight());

        if (!Collisions.checkCollisionTankAndBlock(theTank) && !Collisions.checkCollisionTanks(theTank, this)) {
            x += dx;
            y += dy;
        }

        if (x > (Const.BOARD_WIDTH - getWidth())) {
            x = Const.BOARD_WIDTH - getWidth();
        }

        if (y > (Const.BOARD_HEIGHT - getHeight())) {
            y = Const.BOARD_HEIGHT - getHeight();
        }

        if (x < 1) {
            x = 1;
        }

        if (y < 1) {
            y = 1;
        }

    }

    public void upLives() {
        this.lives += 1;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void upStarLevel() {
        if (starLevel < 3) {
            starLevel += 1;
            SoundUtility.powerUpPick();
        }
    }

    public int getLives() {
        return this.lives;
    }

    public void downLives() {
        if (shield == false) {
            this.lives -= 1;
        }
    }

    public int getStarLevel() {
        return starLevel;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public TankDirection getTankDirection() {
        return tankDirection;
    }

    public void setTankDirection(TankDirection tankDirection) {
        this.tankDirection = tankDirection;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void reset(int X, int Y, boolean resetStarLevel) {
        setDx(0);
        setDy(0);
        setX(X);
        setY(Y);
        reset_x = X;
        reset_y = Y;
        setTankDirection(reset_tankDirection);
        setVisible(getLives()>0);
        starLevel = (resetStarLevel)?0:getStarLevel();
    }

    public void resetDefault(boolean resetStarLevel) {
        reset(reset_x, reset_y, resetStarLevel);
    }

    @Override
    public void loadImage(String src) {
        super.loadImage(src);
        setWidth(getWidth()-6);
        setHeight(getHeight()-6);
    }

    public LinkedList<Bullet> getBullets() {
        return bullets;
    }

    public boolean isShield() {
        return shield;
    }

    public void setShield(boolean shield) {
        this.shield = shield;
    }

    public boolean isAI() {
        return ai;
    }

    public void setAI(boolean ai) {
        this.ai = ai;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
        frozenStartTime = System.currentTimeMillis();
    }

    public long getFrozenStartTime() {
        return frozenStartTime;
    }

}
