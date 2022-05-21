package Sprites;

public class Steel extends Block{

    public Steel(int x, int y) {
        super(x, y);
        loadImage("image/wall_steel.png");
        setIgnoreBulletCollision(3);
        setHealth(2);
    }

}
