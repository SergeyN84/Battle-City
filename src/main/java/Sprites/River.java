package Sprites;

public class River extends Block{

    private boolean loadWater2 = true;

    public River(int x, int y) {
        super(x, y);
        loadImage("image/water_1.png");
        setIgnoreBulletCollision(99);
        setBlowUpBullet(false);
        setHealth(1);
    }

    @Override
    public void updateAnimation() {

        if ( (System.currentTimeMillis() - getInitialTime()) > 500) {
            if (getImagePath() == "image/water_1.png") {
                loadImage("image/water_2.png");
            }
            else
            {
                loadImage("image/water_1.png");
            }
            setInitialTime(System.currentTimeMillis());
        }

    }

}
