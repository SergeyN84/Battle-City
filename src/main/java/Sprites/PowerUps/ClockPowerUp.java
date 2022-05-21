package Sprites.PowerUps;
import Enum.PowerUpType;

public class ClockPowerUp extends PowerUp{
    public ClockPowerUp(int x, int y) {
        super(x, y, PowerUpType.CLOCK);
    }

    @Override
    public void updateAnimation() {
        loadImage("image/powerup_timer.png");
    }

}
