package Game;

import Game.Collisions;
import Game.GameActionUtil;
import Game.TankControl;
import Sprites.*;
import Sprites.PowerUps.PowerUp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Enum.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import static java.awt.event.KeyEvent.*;
import static org.junit.jupiter.api.Assertions.*;

class CollisionsTest {

    private static LinkedList<Block> blocks = new LinkedList<>();
    private static LinkedList<Tank> tanks = new LinkedList<>();
    private static LinkedList<SpriteAnimation> allAnimations = new LinkedList<>();
    private static LinkedList<PowerUp> powerUps = new LinkedList<>();

    @BeforeAll
    static void beforeAll() {
        blocks.add(new Brick(10, 10));

        PlayerTank pt = new PlayerTank(10, 26, 2, TankDirection.UP, 1,
                new TankControl(VK_LEFT,VK_RIGHT, VK_UP, VK_DOWN, VK_SPACE) );
        GameActionUtil.updateTankImage(pt);

        tanks.add(pt);
        tanks.add(new TankAI(10, 20, TankDirection.UP, 1, Difficulty.EASY, AIType.BASIC));

        tanks.add(new TankAI(30, 20, TankDirection.LEFT, 1, Difficulty.EASY, AIType.BASIC));

        Collisions.loadCollisionInstance(blocks, allAnimations, powerUps, tanks);
    }

    @Test
    void checkCollisionTankAndBlock() {
        PlayerTank pt = (PlayerTank) tanks.stream().findFirst().get();
        assertEquals(false, Collisions.checkCollisionTankAndBlock(pt.getBounds()));
        pt.setY(25);
        assertEquals(true, Collisions.checkCollisionTankAndBlock(pt.getBounds()));
    }


    @Test
    void checkCollisionBulletBlocks() {
        PlayerTank pt = (PlayerTank) tanks.stream().findFirst().get();
        pt.setY(25);
        pt.fire();
        Collisions.checkCollisionBulletBlocks(pt.getBullets().get(0));
        assertEquals(false, pt.getBullets().get(0).isVisible());
    }

    @Test
    void checkCollisionBulletTanks() {
        TankAI tankAI = (TankAI) tanks.stream().skip(2).findFirst().get();
        tankAI.fire();
        Collisions.checkCollisionBulletTanks(tankAI.getBullets().get(0), tanks);
        assertEquals(false, tankAI.getBullets().get(0).isVisible());
    }

    @Test
    void testCheckCollisionTanks() {
        assertEquals(true, Collisions.checkCollisionTanks(tanks.get(0).getBounds(), tanks.get(0)));
    }

    @Test
    void getNearestTank() {
        assertEquals(true, Collisions.getNearestTank(tanks.get(0), true) != null);
    }

    @Test
    void checkPowerUpsTanks() {
    }
}