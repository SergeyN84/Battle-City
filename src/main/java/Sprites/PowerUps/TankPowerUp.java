package Sprites.PowerUps;
import Enum.PowerUpType;

public class TankPowerUp extends PowerUp {

    public TankPowerUp(int x, int y) {
        super(x, y, PowerUpType.TANK);
    }

    @Override
    public void updateAnimation() {
        loadImage("image/powerup_tank.png");
    }

}
