package Sprites.PowerUps;
import Enum.PowerUpType;

public class BombPowerUp extends PowerUp{
    public BombPowerUp(int x, int y) {
        super(x, y, PowerUpType.BOMB);
    }

    @Override
    public void updateAnimation() {
        loadImage("image/powerup_grenade.png");
    }

}

