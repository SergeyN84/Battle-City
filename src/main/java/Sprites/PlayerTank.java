package Sprites;

import Game.GameActionUtil;
import Game.TankControl;
import Enum.TankDirection;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.event.KeyEvent.*;

public class PlayerTank extends Tank implements KeyListener {
    private TankControl tankControl;

    public PlayerTank(int x, int y, int lives, TankDirection tankDirection, int teamNumber, TankControl tankControl) {
        super(x, y, lives, tankDirection, teamNumber);
        this.tankControl = tankControl;
    }

    @Override
    public void move() {
        super.move();
        updateAnimation();
    }

    @Override
    public void updateAnimation() {
        GameActionUtil.updateTankImage(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        keyReleased(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (tankControl.getKeyControl().get(e.getKeyCode()) == null || !isVisible()) {
            return;
        }

        int key = tankControl.getKeyControl().get(e.getKeyCode());

        switch (key) {
            case VK_LEFT:
                setDx((getStarLevel() > 2) ? -2 : -1);
                setDy(0);
                setTankDirection(TankDirection.LEFT);
                updateAnimation();
                break;

            case VK_RIGHT:
                setDx((getStarLevel() > 2) ? 2 : 1);
                setDy(0);
                setTankDirection(TankDirection.RIGHT);
                updateAnimation();
                break;

            case VK_UP:
                setDx(0);
                setDy((getStarLevel() > 2) ? -2 : -1);
                setTankDirection(TankDirection.UP);
                updateAnimation();
                break;

            case VK_DOWN:
                setDx(0);
                setDy((getStarLevel() > 2) ? 2 : 1);
                setTankDirection(TankDirection.DOWN);
                updateAnimation();
                break;

            case VK_SPACE:
                fire();
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (tankControl.getKeyControl().get(e.getKeyCode()) == null) {
            return;
        }

        int key = tankControl.getKeyControl().get(e.getKeyCode());

        if (key == VK_LEFT||key == VK_RIGHT) {
            setDx(0);
        }
        if (key == VK_UP||key == VK_DOWN) {
            setDy(0);
        }

    }

}
