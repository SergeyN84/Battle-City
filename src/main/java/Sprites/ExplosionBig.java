package Sprites;

public class ExplosionBig extends SpriteAnimation{

    public ExplosionBig(int x, int y) {
        super(x-16, y-16);
        loadImage("image/big_explosion_1.png");
    }

    @Override
    public void updateAnimation() {
        if ((System.currentTimeMillis() - getInitialTime()) > 125) {
            loadImage("image/big_explosion_2.png");
        }

        if ((System.currentTimeMillis() - getInitialTime()) > 500) {
            loadImage("image/big_explosion_3.png");
        }

        if ((System.currentTimeMillis() - getInitialTime()) > 700) {
            loadImage("image/big_explosion_4.png");
        }

        if ((System.currentTimeMillis() - getInitialTime()) > 900) {
            loadImage("image/big_explosion_5.png");
        }

        if ((System.currentTimeMillis() - getInitialTime()) > 1100) {
            setVisible(false);
        }
    }


}
