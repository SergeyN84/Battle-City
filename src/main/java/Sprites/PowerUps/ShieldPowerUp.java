package Sprites.PowerUps;
import Enum.PowerUpType;

public class ShieldPowerUp extends PowerUp{

    public ShieldPowerUp(int x, int y) {
        super(x, y, PowerUpType.SHIELD);
    }

    @Override
    public void updateAnimation() {
        loadImage("image/powerup_helmet.png");
    }
}
