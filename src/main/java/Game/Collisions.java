package Game;

import Sprites.*;
import Sprites.PowerUps.PowerUp;
import Enum.PowerUpType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static Game.Const.*;


public class Collisions {

    private static LinkedList<Block> blocks;
    private static LinkedList<SpriteAnimation> allAnimations;
    private static LinkedList<PowerUp> powerUps;
    private static LinkedList<Tank> tanks;

    public static void loadCollisionInstance(LinkedList<Block> stageBlocks, LinkedList<SpriteAnimation> animations, LinkedList<PowerUp> allPowerUps, LinkedList<Tank> alltanks){
        blocks = stageBlocks;
        allAnimations = animations;
        tanks = alltanks;
        powerUps = allPowerUps;
    }

    public static boolean checkCollisionTankAndBlock(Rectangle rectangle) {

        for (Block block: blocks){
            if (!block.isIgnoreCollision() && block.isVisible()) {
               if (block.getBounds().intersects(rectangle) ) {
                   return true;
               }
            }
        };
        return false;
    }

    public static void checkCollisionBulletBlocks(Bullet bullet) {

        blocks.stream().filter(block -> !block.isIgnoreCollision() && block.isVisible()).forEach(block -> {
                    if (block.getBounds().intersects(bullet.getBounds())) {
                        if (block.isBlowUpBullet()) {
                            if (!block.isIgnoreBulletCollision(bullet.getParent().getStarLevel())) {
                                block.lowerHealth();
                                SoundUtility.bulletHitBrick();
                            }
                            bullet.setVisible(false);
                            if (!block.isVisible()) {
                                SoundUtility.explosion1();
                                allAnimations.add(new Explosion(block.getX(), block.getY()));
                            }
                        }
                    }
                }
        );
    }

    public static void checkCollisionBulletTanks(Bullet bullet, LinkedList<Tank> allTanks) {
        allTanks.stream().filter(tank -> tank != bullet.getParent() && tank.isVisible()
               && bullet.isVisible() && tank.getBounds().intersects(bullet.getBounds())).forEach(tank -> {
                        bullet.setVisible(false);
                        if (bullet.getTeamNumber()!=tank.getTeamNumber() || FRIENDLY_FIRE) {
                            if (!tank.isShield()) {

                                if (tank.isAI()) {
                                    TankAI tankAI = ((TankAI)tank);
                                    tankAI.decreaseHP();
                                    if (tankAI.getHealth() <= 0) {
                                        tankAI.setVisible(false);
                                        allAnimations.add(new ExplosionBig(tankAI.getX(), tankAI.getY()));
                                        GameActionUtil.incrementNum(tankAI);
                                        Board.decrementEnemies();
                                    } else SoundUtility.bulletHitBrick();

                                } else {
                                    tank.downLives();
                                    allAnimations.add(new ExplosionBig(tank.getX(), tank.getY()));
                                    SoundUtility.explosion1();
                                    tank.resetDefault(true);
                                    allAnimations.add(new TankShield(tank, SHIELD_TIME_3));
                                }

                            } else {
                                SoundUtility.bulletHitBrick();
                            }
                        }
                }
        );
    }

    public static boolean checkCollisionTanks(Rectangle rectangle) {
        return tanks.stream().anyMatch(tankB -> tankB.isVisible() && tankB.getBounds().intersects(rectangle));
    }

    public static boolean checkCollisionTanks(Rectangle rectangle, Tank tankA) {
        return tanks.stream().anyMatch(tankB -> tankB.isVisible() && !tankA.equals(tankB) && tankB.getBounds().intersects(rectangle) && tankA.isVisible());
    }

    public static Block getFirstCollisionBlock(Rectangle rectangle) {
        return blocks.stream().filter(blocks -> blocks.isVisible() && !blocks.isIgnoreCollision() && blocks.getBounds().intersects(rectangle)).findFirst().orElse(null);
    }

    public static Tank getFirstCollisionTank(Rectangle rectangle) {
        return tanks.stream().filter(tanks -> tanks.isVisible() && tanks.getBounds().intersects(rectangle)).findFirst().orElse(null);
    }

    public static ObjectPosition getRandomPosConsideringBlock(int width, int height, int maxX, int maxY) {
        int loopCount = 0;
        return getRandomPosConsideringBlock(width, height, maxX, maxY, loopCount);
    }

    public static ObjectPosition getRandomPosConsideringBlock(int width, int height, int maxX, int maxY, int recursionCount) {

        ObjectPosition op = getRandomPos(maxX, maxY);
        Rectangle r = new Rectangle(op.x(), op.y(), width, height);

        while (true) {

            Sprite b = getFirstCollisionBlock(r);

            if (b == null)  {
                b = getFirstCollisionTank(r);
                if (b == null)  {
                    break;
                }

            }

            if (r.x > b.getX() + b.getWidth()) {
                break;
            }

            r.x += 1;
        }

        if (r.x + r.width > Const.BOARD_WIDTH) {

            r.x = op.x();

            while (true) {

                Sprite b = getFirstCollisionBlock(r);
                if (b == null) {
                    b = getFirstCollisionTank(r);
                    if (b == null)  {
                        break;
                    }
                }

                if (r.x + r.width < b.getX()) {
                    break;
                }

                r.x -= 1;
            }
        }

        if (recursionCount > 99) {
            return new ObjectPosition(0, 0);
        }

        if (r.x < 0) {
            recursionCount +=1;
            getRandomPosConsideringBlock(width, height, maxX, maxY, recursionCount);
        }

        while (true) {

            Sprite b = getFirstCollisionBlock(r);
            if (b == null) {
                b = getFirstCollisionTank(r);
                if (b == null)  {
                    break;
                }
            }

            if (r.y > b.getY() + b.getHeight()) {
                break;
            }

            r.y +=  1;
        }

        if (r.y + r.height > Const.BOARD_HEIGHT) {

            r.y = op.y();

            while (true) {

                Sprite b = getFirstCollisionBlock(r);
                if (b == null) {
                    b = getFirstCollisionTank(r);
                    if (b == null)  {
                        break;
                    }
                }

                if (r.y + r.height < b.getY()) {
                    break;
                }

                r.y -= 1;
            }
        }

        if (recursionCount > 99) {
            return new ObjectPosition(0, 0);
        }

        if (r.y < 0) {
            recursionCount +=1;
            getRandomPosConsideringBlock(width, height, maxX, maxY, recursionCount);
        }

        op = new ObjectPosition(r.x, r.y);

        return op;
    }

    public double calculateOverlapPercentage(Rectangle A, Rectangle B)
    {
        double result = 0.0;

        if (!A.intersects(B)) return 0.0;
        if (A.x == B.x && A.y == B.y && A.width == B.width && A.height == B.height) return 100.0;

        double SA = A.width * A.height;
        double SB = B.width * B.height;

        double aRight = A.x * A.getWidth();
        double bRight = B.x * B.getWidth();
        double aLeft = A.x;
        double bLeft = B.x;
        double aBottom = A.y + A.getHeight();
        double bBottom = B.y + B.getHeight();
        double aTop = A.y;
        double bTop = B.y;

        double SI = Math.max(0,  Math.min(aRight, bRight) - Math.max(aLeft, bLeft)) *
                Math.max(0, Math.min(aBottom, bBottom) - Math.max(aTop, bTop));
        double SU = SA + SB - SI;
        result = SI / SU;
        result *= 100.0;
        return result;
    }

    public static ObjectPosition getRandomPos() {
        return new ObjectPosition(new Random().nextInt(26*Const.BLOCK_WIDTH), new Random().nextInt(26*Const.BLOCK_HEIGHT));
    }

    public static ObjectPosition getRandomPos(int maxX, int maxY) {
        return new ObjectPosition(new Random().nextInt(maxX*Const.BLOCK_WIDTH), new Random().nextInt(maxY*Const.BLOCK_HEIGHT));
    }

    public static Tank getNearestTank(Tank currentTank, boolean isAI) {

        //tanks.stream().filter(tank -> ( (!isAI && !tank.isAI()) || (isAI && tank.isAI()) ) && tank.isVisible() && !tank.equals(currentTank)).collect(Collectors.toCollection(ArrayList::new));

        return
                tanks.stream().filter(tank -> ( (!isAI && !tank.isAI()) || (isAI && tank.isAI()) ) && tank.isVisible() && !tank.equals(currentTank)).
        min(Comparator.comparingDouble(s -> s.calculateDistanceBetweenTank(s, currentTank))).orElse(null);


    }

    public static void checkPowerUpsTanks() {

        tanks.stream().filter(tank -> tank.isVisible()).forEach(tank ->

                powerUps.stream().filter(powerUp -> powerUp.isVisible() && powerUp.getBounds().intersects(tank.getBounds())).forEach(powerUp ->
                {

                    if (powerUp.powerUpType() == PowerUpType.SHIELD) {
                        tank.setShield(true);
                        allAnimations.add(new TankShield(tank, SHIELD_TIME_10));
                        powerUp.setVisible(false);
                        SoundUtility.powerUpPick();
                    } else if (powerUp.powerUpType() == PowerUpType.TANK) {
                        powerUp.setVisible(false);
                        tank.upLives();
                        SoundUtility.powerUpPick();
                    } else if (powerUp.powerUpType() == PowerUpType.STAR) {
                        powerUp.setVisible(false);
                        tank.upStarLevel();
                        SoundUtility.powerUpPick();
                    } else if (powerUp.powerUpType() == PowerUpType.BOMB) {

                        if (!tank.isAI()) {
                            powerUp.setVisible(false);
                            tanks.stream().filter(enemy -> enemy.isVisible() && enemy.isAI()).forEach(enemy ->
                            {
                                Board.numEnemiesStage -= 1;
                                enemy.setVisible(false);
                                allAnimations.add(new ExplosionBig(enemy.getX(), enemy.getY()));
                                GameActionUtil.incrementNum((TankAI) enemy);
                                Board.decrementEnemies();
                            }

                            );
                            SoundUtility.powerUpPick();
                        }

                    } else if (powerUp.powerUpType() == PowerUpType.CLOCK) {

                        if (!tank.isAI()) {
                            powerUp.setVisible(false);
                            tanks.stream().filter(tank2 -> tank2.isVisible() && tank2.isAI()).forEach(tank2 -> tank2.setFrozen(true));
                            SoundUtility.powerUpPick();
                        }
                    }

                }
                )
                );

//        for (int i = 0; i < powerUps.size(); i++) {
//            PowerUp p = powerUps.get(i);
//            p.updateAnimation();
//            PowerUpType type = p.getPowerUpType();
//
//            if ( (System.currentTimeMillis() - p.getInitialTime() > 10000) || !p.isVisible() ) {
//                powerUps.remove(i);
//            }
//
//            LinkedList<> =
//
//            if (r1.intersects(r2)) {
//                powerUps.remove(i);
//                SoundUtility.powerupPick();
//                if (type.equals(BlockType.TANK)) {
//                    tank.upHealth();
//                } else if (type.equals(BlockType.SHIELD)) {
//                    tank.shield = true;
//                    animations.add(new TankShield(tank, 1));
//                } else if (type.equals(BlockType.SHOVEL)) {
//
//                } else if (type.equals(BlockType.STAR)) {
//                    tank.upStarLevel();
//                } else if (type.equals(BlockType.CLOCK)) {
//                    for (int x = 0; x < enemy.size(); x++) {
//                        enemy.get(x).frozen = true;
//                        enemy.get(x).frozenStartTime = System.currentTimeMillis();
//                    }
//                } else if (type.equals(BlockType.BOMB)) {
//                    for (int x = 0; x < enemy.size(); x++) {
//                        enemy.get(x).vis = false;
//                        for (TankAI ai : enemy) {
//                            CollisionUtility.incrementNum(ai);
//                        }
//                        Board.decrementEnemies(enemy.size());
//                        animations.add(new ExplodingTank(enemy.get(x).x,
//                                enemy.get(x).y));
//                    }
//                }
//            }
//        }

    }
}
