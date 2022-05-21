package Sprites;

public class Tree extends  Block{
    public Tree(int x, int y) {
        super(x, y);
        loadImage("image/trees.png");
        setIgnoreCollision(true);
        setHealth(1);
    }
}
