package Sprites;

import Enum.TankDirection;
import Enum.Difficulty;
import Enum.AIType;
import Game.Collisions;
import Game.Const;
import Game.GameActionUtil;

import java.awt.*;
import java.util.Random;


public class TankAI extends Tank {

    private final Difficulty difficulty;
    private final AIType type;
    private int health;
    private int speedConst;
    private int fireUpdateInterval;
    private int dirUpdateInterval;
    private int dirTimer = 0;
    private int fireTimer = 0;

    public TankAI(int x, int y, TankDirection tankDirection, int teamNumber, Difficulty tankDifficulty, AIType aiType) {
        super(x, y, 1, tankDirection, teamNumber);
        setAI(true);
        difficulty = tankDifficulty;
        type = aiType;
        setUp();
        setWidth(32);
        setHeight(32);
    }

    @Override
    public void updateAnimation() {
        GameActionUtil.updateTankAIImage(this);
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public AIType getType() {
        return type;
    }

    private void setUp() {

        if (getType() == AIType.BASIC) {
            health = 1;
            speedConst = 1;
            if (getDifficulty() == Difficulty.EASY) {
                dirUpdateInterval = 30;
                fireUpdateInterval = 80;
            } else if (getDifficulty() == Difficulty.NORMAL) {
                dirUpdateInterval = 30;
                fireUpdateInterval = 75;
            } else if (getDifficulty() == Difficulty.HARD) {
                dirUpdateInterval = 30;
                fireUpdateInterval = 70;
            }
        } else if (getType() == AIType.ARMOR) {
            health = 3;
            speedConst = 1;
            if (getDifficulty() == Difficulty.EASY) {
                dirUpdateInterval = 30;
                fireUpdateInterval = 80;
            } else if (getDifficulty() == Difficulty.NORMAL) {
                dirUpdateInterval = 30;
                fireUpdateInterval = 75;
            } else if (getDifficulty() == Difficulty.HARD) {
                dirUpdateInterval = 30;
                fireUpdateInterval = 70;
            }
        } else if (getType() == AIType.POWER) {
            health = 1;
            speedConst = 1;
            if (getDifficulty() == Difficulty.EASY) {
                dirUpdateInterval = 30;
                fireUpdateInterval = 40;
            } else if (getDifficulty() == Difficulty.NORMAL) {
                dirUpdateInterval = 30;
                fireUpdateInterval = 35;
            } else if (getDifficulty() == Difficulty.HARD) {
                dirUpdateInterval = 30;
                fireUpdateInterval = 30;
            }
        } else if (getType() == AIType.FAST) {
            health = 1;
            speedConst = 2;
            if (getDifficulty() == Difficulty.EASY) {
                dirUpdateInterval = 30;
                fireUpdateInterval = 80;
            } else if (getDifficulty() == Difficulty.NORMAL) {
                dirUpdateInterval = 30;
                fireUpdateInterval = 75;
            } else if (getDifficulty() == Difficulty.HARD) {
                dirUpdateInterval = 30;
                fireUpdateInterval = 70;
            }
        }
    }

    public void decreaseHP() {
        this.health -= 1;
    }

    public int getHealth() {
        return health;
    }

    public void randomDir() {
        switch ((new Random()).nextInt(4)) {
            case 0 : setTankDirection(TankDirection.UP); break;
            case 1 : setTankDirection(TankDirection.RIGHT);break;
            case 2 : setTankDirection(TankDirection.DOWN);break;
            case 3 : setTankDirection(TankDirection.LEFT);break;
//            case 0 -> setTankDirection(TankDirection.UP);
//            case 1 -> setTankDirection(TankDirection.RIGHT);
//            case 2 -> setTankDirection(TankDirection.DOWN);
//            case 3 -> setTankDirection(TankDirection.LEFT);
        }
        setDirectionSpeed();
    }

    public void toTankDirection() {

        Tank nearestTank = Collisions.getNearestTank(this, false);

        if (nearestTank != null) {

            int tankX = nearestTank.getX();
            int tankY = nearestTank.getY();
            Random randomDir = new Random();
            if (randomDir.nextBoolean()) {
                if (this.getY() > tankY) {
                    setTankDirection(TankDirection.UP);
                } else {
                    setTankDirection(TankDirection.DOWN);
                }
            } else if (this.getX() > tankX) {
                setTankDirection(TankDirection.LEFT);
            } else if (this.getX() < tankX) {
                setTankDirection(TankDirection.RIGHT);
            } else {
                setTankDirection(TankDirection.LEFT);
            }

            setDirectionSpeed();

        }

    }

    public void toEagleDir() {

        if (getX() > 14 * 16) {
            setTankDirection(TankDirection.LEFT);
        } else if (getX() < 14 * 16) {
            setTankDirection(TankDirection.RIGHT);
        } else {
            setTankDirection(TankDirection.DOWN);
        }
        setDirectionSpeed();
    }

    private void setDirectionSpeed() {

        switch (getTankDirection()) {
            case UP :
                setDx(0);
                setDy(-1 * speedConst);
                break;

            case RIGHT :
                setDx(1 * speedConst);
                setDy(0);
                break;
            case DOWN :
                setDx(0);
                setDy(1 * speedConst);
                break;
            case LEFT :
                setDx(-1 * speedConst);
                setDy(0);
                break;
        }

        updateAnimation();

    }

    public void difficultyLevelEasy() {

        if (dirTimer >= this.dirUpdateInterval) {
            randomDir();
            dirTimer = 0;
        } else {
            dirTimer++;
        }

        super.move();

        if (fireTimer >= fireUpdateInterval) {
            fire();
            fireTimer = 0;
        } else {
            fireTimer++;
        }

    }

    public void difficultyLevelNormal() {

        Random randomDir = new Random();
        if (dirTimer >= dirUpdateInterval) {
            int random = randomDir.nextInt(20);
            if (random % 8 == 1) {
                toEagleDir();
            } else if (random % 4 == 0) {
                randomDir();
            } else {
                toTankDirection();
            }
            this.dirTimer = 0;
        } else {
            this.dirTimer++;
        }
        super.move();
        Rectangle theTank = new Rectangle(getX() + getDx(), getY() + getDy(), getWidth(), getHeight());

        if (Collisions.checkCollisionTankAndBlock(theTank)) {
            if (randomDir.nextBoolean() && fireTimer < 3) {
                fire();
                fireTimer++;
            }
        }
        if (fireTimer >= fireUpdateInterval) {
            fire();
            fireTimer = 0;
        } else {
            fireTimer++;
        }
    }

    public void difficultyLevelHard() {

        Random randomDir = new Random();
        if (dirTimer >= dirUpdateInterval) {
            int random = randomDir.nextInt(7);
            if (random % 5 == 0) {
                toEagleDir();
            } else if (random % 6 == 1) {
                randomDir();
            } else {
                toTankDirection();
            }
            this.dirTimer = 0;
        } else {
            this.dirTimer++;
        }
        super.move();
        Rectangle theTank = new Rectangle(getX() + getDx(), getY() + getDy(), getWidth(), getHeight());

        if (Collisions.checkCollisionTankAndBlock(theTank)) {
            if (randomDir.nextBoolean() && fireTimer < 3) {
                fire();
                fireTimer++;
            }
        }
        if (fireTimer >= fireUpdateInterval) {
            fire();
            fireTimer = 0;
        } else {
            fireTimer++;
        }
    }

    private boolean checkFrozenState() {
        if (isFrozen() && System.currentTimeMillis() - getFrozenStartTime() > Const.FROZEN_TIME) {
            setFrozen(false);
        }
        return isFrozen();
    }

    @Override
    public void move() {

        if (checkFrozenState()) return;

        switch (getDifficulty()) {
            case EASY   : difficultyLevelEasy(); break;
            case NORMAL : difficultyLevelNormal(); break;
            case HARD   : difficultyLevelHard(); break;
        }
    }
}
