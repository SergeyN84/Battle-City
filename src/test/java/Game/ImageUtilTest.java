package Game;

import Game.ImageUtil;
import org.junit.jupiter.api.Test;


import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ImageUtilTest {

    private static ImageUtil imageUtil = ImageUtil.getInstance();


    @Test
    void getBackground() {
        assertEquals(true, imageUtil.getBackground().getWidth(null) != -1);
    }

    @Test
    void getTank_player1_right_c0_t1() {
        assertEquals(true, imageUtil.getTank_player1_right_c0_t1().getWidth(null) != -1);
    }

    @Test
    void getTank_player1_right_c0_t2() {
        assertEquals(true, imageUtil.getTank_player1_right_c0_t2().getWidth(null) != -1);
    }

    @Test
    void getTree2() {
        assertEquals(true, imageUtil.getTree2().getWidth(null) != -1);
    }

    @Test
    void getLives() {
        assertEquals(true, imageUtil.getLives().getWidth(null) != -1);
    }

    @Test
    void getFlagIcon() {
        assertEquals(true, imageUtil.getFlagIcon().getWidth(null) != -1);
    }

    @Test
    void getEnemyIcon() {
        assertEquals(true, imageUtil.getEnemyIcon().getWidth(null) != -1);
    }

    @Test
    void getArrow() {
        assertEquals(true, imageUtil.getArrow().getWidth(null) != -1);
    }

    @Test
    void getTankBasic() {
        assertEquals(true, imageUtil.getTankBasic().getWidth(null) != -1);
    }

    @Test
    void getTankFast() {
        assertEquals(true, imageUtil.getTankFast().getWidth(null) != -1);
    }

    @Test
    void getTankPower() {
        assertEquals(true, imageUtil.getTankPower().getWidth(null) != -1);
    }

    @Test
    void getTankArmor() {
        assertEquals(true, imageUtil.getTankArmor().getWidth(null) != -1);
    }
}