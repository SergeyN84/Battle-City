package Game;

import Sprites.Tank;
import Sprites.SpriteAnimation;

public class TankShield extends SpriteAnimation {

    private int shieldTime;
    private Tank tank;

    public TankShield(Tank aTank, int shieldTime) {
        super(aTank.getX(), aTank.getY());
        loadImage("image/shield_1.png");
        setShieldTime(shieldTime);
        tank = aTank;
        tank.setShield(true);
    }

    public int getShieldTime() {
        return shieldTime;
    }

    public void setShieldTime(int shieldTime) {
        this.shieldTime = shieldTime;
    }

    @Override
    public void updateAnimation() {

        setX(tank.getX());
        setY(tank.getY());

        long timeDifference = (System.currentTimeMillis() - getInitialTime());

        if (timeDifference % 10 == 0) {
            if (getImagePath().equals("image/shield_1.png")) {
                loadImage("image/shield_2.png");
            } else {
                loadImage("image/shield_1.png");
            }
        }

        if ((System.currentTimeMillis() - getInitialTime() > shieldTime)) {
            tank.setShield(false);
            setVisible(false);
        }

    }
}
