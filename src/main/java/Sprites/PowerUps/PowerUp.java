package Sprites.PowerUps;
import Enum.PowerUpType;
import Sprites.SpriteAnimation;

public abstract class PowerUp extends SpriteAnimation {

    private final PowerUpType powerUpType;

    public PowerUp(int x, int y, PowerUpType powerUpType) {
        super(x, y);
        this.powerUpType = powerUpType;

    }

    public PowerUpType powerUpType() {
        return powerUpType;
    }
}
