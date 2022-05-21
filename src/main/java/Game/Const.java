package Game;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.awt.event.KeyEvent.*;

public final class Const {
//    public static final int BOARD_WIDTH = 16 * 33;
//    public static final int BOARD_HEIGHT = 16 * 28;
    public static final int BOARD_WIDTH = 16 * 27;
    public static final int BOARD_HEIGHT = 16 * 27;


    public static final int GAME_DELAY = 15;
    public static final int BLOCK_WIDTH = 16;
    public static final int BLOCK_HEIGHT = 16;
//    public static final int INIT_PLAYER1_X = 10 * 16;
//    public static final int INIT_PLAYER1_Y = 25 * 16;
    public static final int INIT_PLAYER1_X = 9 * 16;
    public static final int INIT_PLAYER1_Y = 26 * 16;
    public static final int bulletSpeed = 3;

    public static final int INIT_PLAYER2_X = 16 * 16;
    public static final int INIT_PLAYER2_Y = 26 * 16;

    public static final int INIT_X = 28; // score info position
    public static final int NUM_ENEMIES = 10;

    public static final int SHIELD_TIME_3 = 3000;
    public static final int SHIELD_TIME_10 = 10000;

    public static final int SPAWN_POWER_UP = 30000;
    public static final int REMOVE_POWER_UP = 10000;

    public static final int POWER_UP_WIDTH  = 32;
    public static final int POWER_UP_HEIGHT = 32;

    public static final int BASE_POS_X = 12*16;
    public static final int BASE_POS_Y = 25 * 16;

    public static final String ACTIVATE_CHEAT_MODE_WORD = "IDDQD";

    public static final long FROZEN_TIME = 5000;


    public static final boolean FRIENDLY_FIRE = false;

    public static Font loadFont() {
        Font font = null;
        try {
            font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
                    new File("prstart.ttf"));
            font = font.deriveFont(java.awt.Font.PLAIN, 15);
            GraphicsEnvironment ge
                    = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

        } catch (FontFormatException | IOException ex) {
            //Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return font;
    }

}
