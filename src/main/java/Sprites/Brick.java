package Sprites;

public class Brick extends Block{

    public Brick(int x, int y) {
        super(x, y);
        loadImage("image/wall_brick.png");
        setHealth(1);
    }

}
