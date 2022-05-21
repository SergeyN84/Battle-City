package Sprites;

public class TankSpawn extends SpriteAnimation{

    public TankSpawn(int x, int y) {
        super(x, y);
        loadImage("image/appear_1.png");
    }

    @Override
    public void updateAnimation() {

        if ((System.currentTimeMillis() - getInitialTime()) > 100) {
            loadImage("image/appear_2.png");
        }

        if ((System.currentTimeMillis() - getInitialTime()) > 200) {
            loadImage("image/appear_3.png");
        }

        if ((System.currentTimeMillis() - getInitialTime()) > 300) {
            loadImage("image/appear_4.png");
        }

        if ((System.currentTimeMillis() - getInitialTime()) > 400) {
            loadImage("image/appear_1.png");
        }

        if ((System.currentTimeMillis() - getInitialTime()) > 500) {
            loadImage("image/appear_2.png");
        }

        if ((System.currentTimeMillis() - getInitialTime()) > 600) {
            loadImage("image/appear_3.png");
        }

        if ((System.currentTimeMillis() - getInitialTime()) > 700) {
            loadImage("image/appear_4.png");
        }


        if ((System.currentTimeMillis() - getInitialTime()) > 800) {
            setVisible(false);
        }
    }
}
