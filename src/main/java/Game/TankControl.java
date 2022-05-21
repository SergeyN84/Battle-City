package Game;

import java.util.HashMap;

import static java.awt.event.KeyEvent.*;

public class TankControl {
    private HashMap<Integer, Integer> keyControl = new HashMap<Integer, Integer>();

    public TankControl(int LEFT, int RIGHT, int UP, int DOWN, int SPACE ) {
        keyControl.put(LEFT, VK_LEFT);
        keyControl.put(RIGHT, VK_RIGHT);
        keyControl.put(UP, VK_UP);
        keyControl.put(DOWN, VK_DOWN);
        keyControl.put(SPACE, VK_SPACE);
    }

    public HashMap<Integer, Integer> getKeyControl() {
        return keyControl;
    }
}
