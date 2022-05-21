package Game;

import Enum.AIType;
import Enum.TankDirection;
import Enum.Difficulty;
import Sprites.*;
import Sprites.PowerUps.*;

import java.util.LinkedList;
import java.util.Random;

public class GameActionUtil {

    private static LinkedList<Tank> allTanks;
    private static LinkedList<Block> allBlocks;
    private static LinkedList<SpriteAnimation> allAnimations;
    private static LinkedList<PowerUp> powerUps;
    private static long lastTimeSpawnPowerUp = 0;
    private static int[] enemyTankNum = {0, 0, 0, 0};

    public static void loadGameActionUtil(LinkedList<Tank> tanksList, LinkedList<Block> blockList, LinkedList<SpriteAnimation> animations, LinkedList<PowerUp> allPowerUps){
        allTanks = tanksList;
        allBlocks = blockList;
        allAnimations = animations;
        powerUps = allPowerUps;
    }

    public static void resetScore() {
        enemyTankNum = new int[]{0, 0, 0, 0};
    }

    public static void incrementNum(TankAI tankAI) {
        AIType type = tankAI.getType();
        switch (type) {
            case BASIC : enemyTankNum[0] += 1; break;
            case FAST  : enemyTankNum[1] += 1; break;
            case POWER : enemyTankNum[2] += 1; break;
            case ARMOR : enemyTankNum[3] += 1; break;
        }
    }

    public static int[] getEnemyTankNum() {
        return enemyTankNum;
    }

    public static void updateBlocks() {
        for (int i = allBlocks.size()-1; i >= 0; i--) {
            Block b = allBlocks.get(i);
            if (!b.isVisible() ) {
                allBlocks.remove(b);
            } else {
                b.updateAnimation();
            }

        }
    }

    public static void updateTanks() {
        checkAndRemoveTank();
        allTanks.stream().filter(Sprite::isVisible).forEach(Tank::move);
    }

    public static void updateTankBullets() {
        LinkedList<Bullet> bullets = new LinkedList<>();
        for (Tank tanks : allTanks) {
            bullets.addAll(tanks.getBullets());
        }

        bullets.forEach(
                bullet ->
                {
                    if (bullet.isVisible()) {
                        bullet.move();
                        Collisions.checkCollisionBulletBlocks(bullet);
                        Collisions.checkCollisionBulletTanks(bullet, allTanks);
                    }
                    if (!bullet.isVisible()) {
                        bullet.getParent().getBullets().remove(bullet);
                    }
                });

    }

    private static void checkAndRemovePowerUps() {

        for (int i = powerUps.size()-1; i >= 0; i--) {
            PowerUp p = powerUps.get(i);
            p.updateAnimation();

            if ((System.currentTimeMillis() - p.getInitialTime() > Const.REMOVE_POWER_UP) || !p.isVisible()) {
                powerUps.remove(i);
            }
        }
    }

    public static void checkAndRemoveTank() {
        for (int i = allTanks.size()-1; i >= 0; i--) {
            Tank p = allTanks.get(i);
            if (!p.isVisible()) {
                allTanks.remove(i);
            }
        }
    }

    public static void updatePowerUps() {
        Collisions.checkPowerUpsTanks();
        checkAndRemovePowerUps();
    }

    public static void spawnPowerUp() {

        if ((System.currentTimeMillis() - lastTimeSpawnPowerUp) > Const.SPAWN_POWER_UP) {
            ObjectPosition op = Collisions.getRandomPosConsideringBlock(Const.POWER_UP_WIDTH, Const.POWER_UP_HEIGHT, 26, 26);
            switch(new Random().nextInt(5)) {
                case 0 : powerUps.add(new BombPowerUp(op.x(), op.y())); break;
                case 1 : powerUps.add(new ClockPowerUp(op.x(), op.y())); break;
                case 2 : powerUps.add(new ShieldPowerUp(op.x(), op.y())); break;
                case 3 : powerUps.add(new StarPowerUp(op.x(), op.y())); break;
                case 4 : powerUps.add(new TankPowerUp(op.x(), op.y())); break;
            }
            lastTimeSpawnPowerUp = System.currentTimeMillis();
        }
    }

    public static void spawnTankAI(Difficulty difficulty) {

        ObjectPosition op = Collisions.getRandomPosConsideringBlock(32, 32, 26, 1);
        int randomType = (new Random()).nextInt(20);
        AIType typeAI;
        if (randomType < 2) {
            typeAI = AIType.ARMOR;
        } else if (randomType >= 2 && randomType < 7) {
            typeAI = AIType.POWER;
        } else if (randomType >= 8 && randomType < 13) {
            typeAI = AIType.FAST;
        } else {
            typeAI = AIType.BASIC;
        }

        allAnimations.add(new TankSpawn(op.x(), op.y()));
        Tank tankAI = new TankAI(op.x(), op.y(), TankDirection.DOWN, 2, difficulty,  typeAI);
        allTanks.add(tankAI);

    }

    public static void updateTankImage(Tank tank) {

        String dirString = tank.getTankDirection().getDirectionString();

        String [] imageMoveSrcArray = new String[2];
        imageMoveSrcArray[0] = "image/tank_player1_" + dirString + "_c0_t1" + ( (tank.getStarLevel() > 1)? "_s" + tank.getStarLevel() :"") + ".png";
        imageMoveSrcArray[1] = "image/tank_player1_" + dirString + "_c0_t2" + ( (tank.getStarLevel() > 1)? "_s" + tank.getStarLevel() :"") + ".png";

        if (tank.getDx() == 0 && tank.getDy() == 0) {
            tank.loadImage(imageMoveSrcArray[0]);
            SoundUtility.tankStopped();
        }
        else if (tank.getImagePath().equalsIgnoreCase(imageMoveSrcArray[0])) {
            tank.loadImage(imageMoveSrcArray[1]);
            SoundUtility.tankMoving();
        } else {
            tank.loadImage(imageMoveSrcArray[0]);
            SoundUtility.tankMoving();
        }

    }

    public static void updateTankAIImage(TankAI tank) {

        String dirString = tank.getTankDirection().getDirectionString();
        String AITypeString = tank.getType().getAITypeString();
        int subImgNum = 0;
        if (tank.getType() == AIType.ARMOR) {
            subImgNum = 3 - tank.getHealth();
        }
        String [] imageMoveSrcArray = new String[2];
        imageMoveSrcArray[0] = "image/" + AITypeString + "_" + dirString + "_c" + subImgNum + "_t1.png";
        imageMoveSrcArray[1] = "image/" + AITypeString + "_" + dirString + "_c" + subImgNum + "_t2.png";

        if (tank.getDx() == 0 && tank.getDy() == 0) {
            tank.loadImage(imageMoveSrcArray[0]);
           // SoundUtility.tankStopped();
        }
        else if (tank.getImagePath().equalsIgnoreCase(imageMoveSrcArray[0])) {
            tank.loadImage(imageMoveSrcArray[1]);
          //  SoundUtility.tankMoving();
        } else {
            tank.loadImage(imageMoveSrcArray[0]);
          //  SoundUtility.tankMoving();
        }

    }

    public static void updateAnimations() {
        for (int i = 0; i < allAnimations.size(); i++) {
            if (allAnimations.get(i).isVisible() == false) {
                allAnimations.remove(i);
            } else {
                allAnimations.get(i).updateAnimation();
            }
        }
    }

    public static void resetLastTimeSpawnPowerUp() {
        lastTimeSpawnPowerUp = 0;
    }

    public static int getPercentSizeX(SpriteAnimation spriteAnimation, float percentSizeWidth) {
        return spriteAnimation.getX() + getPercentSize(spriteAnimation.getX(), percentSizeWidth);
    }

    public static int getPercentSizeY(SpriteAnimation spriteAnimation, float percentSizeWidth) {
        return spriteAnimation.getY() + getPercentSize(spriteAnimation.getY(), percentSizeWidth);
    }

    public static int getPercentSizeWidth(SpriteAnimation spriteAnimation, float percentSizeWidth) {
        return spriteAnimation.getWidth() + getPercentSize(spriteAnimation.getWidth(), percentSizeWidth);
    }

    public static int getPercentSizeHeight(SpriteAnimation spriteAnimation, float percentSizeWidth) {
        return spriteAnimation.getHeight() + getPercentSize(spriteAnimation.getHeight(), percentSizeWidth);
    }

    public static int getPercentSize(int val, float percentSizeWidth) {
        return (int)(val * percentSizeWidth)/100;
    }
}
