package Sprites.PowerUps;
import Enum.PowerUpType;

public class StarPowerUp extends PowerUp {

    public StarPowerUp(int x, int y) {
        super(x, y, PowerUpType.STAR);
    }

    @Override
    public void updateAnimation() {
        loadImage("image/powerup_star.png");
    }
}
