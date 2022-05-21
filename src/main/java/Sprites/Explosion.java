package Sprites;

public class Explosion extends SpriteAnimation{
    public Explosion(int x, int y) {
        super(x, y);
        loadImage("image/bullet_explosion_1.png");
    }

    @Override
    public void updateAnimation() {
        if ((System.currentTimeMillis() - getInitialTime()) > 125) {
            loadImage("image/bullet_explosion_2.png");
        }

        if ((System.currentTimeMillis() - getInitialTime()) > 250) {
            loadImage("image/bullet_explosion_3.png");
        }

        if ((System.currentTimeMillis() - getInitialTime()) > 300) {
           setVisible(false);
        }

    }

}
