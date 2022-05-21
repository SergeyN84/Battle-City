package Game;

import javax.swing.*;
import java.awt.*;

public class ImageUtil {

    private final Image background;
    private final Image tank_player1_right_c0_t1;
    private final Image tank_player1_right_c0_t2;
    private final Image tree2;
    private final Image lives;
    private final Image flagIcon;
    private final Image enemyIcon;
    private final Image arrow, tankBasic, tankFast, tankPower, tankArmor;

    private static ImageUtil instance;

    private ImageUtil(){
        enemyIcon = loadImage("image/enemy.png");
        lives = loadImage("image/lives.png");
        background = loadImage("image/battle_city.png");
        tank_player1_right_c0_t1 = loadImage("image/tank_player1_right_c0_t1.png");
        tank_player1_right_c0_t2 = loadImage("image/tank_player1_right_c0_t2.png");
        tree2 = loadImage("image/trees2.png");
        flagIcon = loadImage("image/flag.png");

        arrow = loadImage("image/arrow.png");
        tankBasic = loadImage("image/tank_basic.png");
        tankFast = loadImage("image/tank_fast.png");
        tankPower = loadImage("image/tank_power.png");
        tankArmor = loadImage("image/tank_armor.png");
    }

    public static ImageUtil getInstance(){

        if (instance == null) {
            instance =  new ImageUtil();
        };

        return instance;
    }

    public static Image loadImage(String src) {
        ImageIcon ia = new ImageIcon(src);
        return ia.getImage();
    }

    public Image getBackground() {
        return background;
    }

    public Image getTank_player1_right_c0_t1() {
        return tank_player1_right_c0_t1;
    }

    public Image getTank_player1_right_c0_t2() {
        return tank_player1_right_c0_t2;
    }

    public Image getTree2() {
        return tree2;
    }

    public Image getLives() {
        return lives;
    }
    public Image getFlagIcon() {
        return flagIcon;
    }

    public Image getEnemyIcon() {
        return enemyIcon;
    }

    public Image getArrow() {
        return arrow;
    }

    public Image getTankBasic() {
        return tankBasic;
    }

    public Image getTankFast() {
        return tankFast;
    }

    public Image getTankPower() {
        return tankPower;
    }

    public Image getTankArmor() {
        return tankArmor;
    }

}
