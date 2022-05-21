package Sprites;

import Game.Const;
import Enum.TankDirection;

public class Bullet extends SpriteAnimation{
    private int teamNumber;
    private TankDirection direction;
    private Tank parent;

    public Bullet(int x, int y, int teamNumber, Tank parent) {
        super(x, y);
        this.teamNumber = teamNumber;
        direction = parent.getTankDirection();
        updateAnimation();
        this.parent = parent;
    }

    @Override
    public void updateAnimation() {
        String dirString = direction.getDirectionString();
        loadImage("image/bullet_" + dirString + ".png");
    }

    public void move() {

        if (isVisible()) {
            switch (direction) {
                case UP :
                    y -= Const.bulletSpeed;
                break;

                case DOWN :
                    y += Const.bulletSpeed;
                    break;

                case LEFT :
                    x -= Const.bulletSpeed;
                    break;

                case RIGHT :
                    x += Const.bulletSpeed;
                    break;
            }
        }

        if (getX() > Const.BOARD_WIDTH + getWidth()) {
            setVisible(false);
        }
        if (getX() < -getWidth()) {
            setVisible(false);
        }
        if (getY() > Const.BOARD_HEIGHT + getHeight()) {
            setVisible(false);
        }
        if (getY() < -getHeight()) {
            setVisible(false);
        }
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public Tank getParent() {
        return parent;
    }
}
