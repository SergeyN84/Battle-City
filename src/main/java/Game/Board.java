package Game;

import Enum.*;
import Log.SLF4J;
import Sprites.*;
import Sprites.PowerUps.PowerUp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import static Game.Const.SHIELD_TIME_3;
import static java.awt.event.KeyEvent.*;

public class Board extends JPanel implements ActionListener {

    private static Font font = Const.loadFont();
    private ImageUtil imageUtilInstance = ImageUtil.getInstance();
    private Timer gameTimer, gameOverTimer, stageFrameTimer;
    private static boolean sourceBoardTimerCreated = false;
    private static boolean gameOverTimerStarted = false;
    private static boolean showLevelFrame = true;
    private static boolean stageFrameTimerCreated = false;

    private static StringBuilder cheatCodeBuffer = new StringBuilder();
    private static boolean cheatCodeIsActive = false;

    private final MainWindow gameWindow;
    private final GameType gameType;
    private boolean pause = false;
    private static boolean gameOver = false;
    private static int stage = 1;
    private int numAICreated;
    private Tank tankP1, tankP2;
    //private boolean nextLevelProcess = false;
    public static int numEnemiesStage = Const.NUM_ENEMIES;

    private LinkedList<Block> blocks = new LinkedList<>();
    private LinkedList<Tank> tanks = new LinkedList<>();
    private LinkedList<SpriteAnimation> allAnimations = new LinkedList<>();
    private LinkedList<PowerUp> powerUps = new LinkedList<>();

    private static int stageFrameH = Const.BOARD_HEIGHT/2;
    private static int yPos = Const.BOARD_HEIGHT;
    private int direction = -1;
    private final int stopYPos = 250;


    public Board(MainWindow mainWindow, GameType gameType) {
        gameWindow = mainWindow;
        this.gameType = gameType;
        initBoard();
    }

   private void checkPlayersTankHeath(){

        if (tanks.stream().filter(tank -> !tank.isAI()).allMatch(tank -> tank.getLives() == 0))  {
            setGameOver(true);
        }
   }

    public static void setGameOver(Boolean aGameOver){
        gameOver = aGameOver;
        gameOverTimerStarted = !gameOver;
        sourceBoardTimerCreated = !gameOver;
        if (gameOver) {
            yPos = Const.BOARD_HEIGHT;
            SoundUtility.gameOver();
        }
    }

   private void initPlayerTank(boolean resetStarLevel) {

        if (tankP1 == null) {
           tankP1 = new PlayerTank(Const.INIT_PLAYER1_X, Const.INIT_PLAYER1_Y, 2, TankDirection.UP, 1,
                   new TankControl(VK_LEFT,VK_RIGHT, VK_UP, VK_DOWN, VK_SPACE)
             );
            tankP1.setCaption("P1");

       }
       else {
           tankP1.resetDefault(resetStarLevel);
       }
       allAnimations.add(new TankShield(tankP1, SHIELD_TIME_3));
       tanks.add(tankP1);

       if (gameType == GameType.MULTIPLAYER) {

           if (tankP2 == null) {
               tankP2 = new PlayerTank(Const.INIT_PLAYER2_X, Const.INIT_PLAYER2_Y, 2, TankDirection.UP, 1,
                       new TankControl(VK_A, VK_D, VK_W, VK_S, VK_F)
               );

               tankP2.setCaption("P2");

           } else {
               tankP2.resetDefault(resetStarLevel);
           }
           allAnimations.add(new TankShield(tankP2, SHIELD_TIME_3));
           tanks.add(tankP2);

       }
   }

   private void initBoard(){
        showFrameLevel();
        this.setLayout(null);
        addKeyListener(new keyEventListener());
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(Const.BOARD_WIDTH, Const.BOARD_HEIGHT));

        numAICreated = 0;
        gameTimer = new Timer(Const.GAME_DELAY, this);
        gameTimer.start();

        gameOverTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(
                    ActionEvent e) {
                yPos += direction;
                if (yPos == stopYPos) {
                    direction = 0;
                } else if (yPos > getHeight()) {
                    yPos = getHeight();
                } else if (yPos < 0) {
                    yPos = 0;
                    direction *= -1;
                }
                repaint();
            }
        });
       gameOverTimer.setRepeats(true);
       gameOverTimer.setCoalesce(true);


        initPlayerTank(true);

        initBlocks();
        loadUtilInstance();
    }

    private void drawObjects(Graphics g, float percentSizeMin) {

        tanks.stream().filter(tank -> tank.isVisible()).forEach(tank ->
                {
                  g.drawImage(tank.getImage(), GameActionUtil.getPercentSizeX(tank, percentSizeMin) -3, GameActionUtil.getPercentSizeY(tank, percentSizeMin)-3, tank.getWidth()+6 + (int)((tank.getWidth()+6) * percentSizeMin)/100, tank.getHeight()+6 + (int)((tank.getHeight()+6)* percentSizeMin)/100, this);
                  //g.drawImage(tank.getImage(), tank.getX()-3, tank.getY()- 3, this);

                  g.setColor(Color.GREEN);

                  if (!tank.getCaption().isEmpty()) {
                      g.drawString(tank.getCaption(), GameActionUtil.getPercentSizeX(tank, percentSizeMin) , GameActionUtil.getPercentSizeY(tank, percentSizeMin));
                      //g.drawString(tank.getCaption(), tank.getX(), tank.getY());
                  }

//                  g.setColor(Color.YELLOW);
//                  if ( Collisions.test(tank.getWidth(), tank.getHeight(), tank.getX(), tank.getY(), tank) ) {
//                      g.drawString("0", tank.getX()+16, tank.getY()+16);
//                  }
//                  else
//                      g.drawString("1", tank.getX()+16, tank.getY()+16);
//
//                  g.setColor(Color.GREEN);

                    //if (nearestTank != null && tank.equals(nearestTank)) {
                    //    g.drawRect(tank.getX(), tank.getY(), tank.getWidth(), tank.getHeight());
                    //}
                }
        );

        allAnimations.stream().filter(spriteAnimation -> spriteAnimation.isVisible()).forEach(spriteAnimation ->
                g.drawImage(spriteAnimation.getImage(), GameActionUtil.getPercentSizeX(spriteAnimation, percentSizeMin) -3, GameActionUtil.getPercentSizeY(spriteAnimation, percentSizeMin)-3, GameActionUtil.getPercentSizeWidth(spriteAnimation, percentSizeMin), GameActionUtil.getPercentSizeHeight(spriteAnimation, percentSizeMin), this)
                //g.drawImage(spriteAnimation.getImage(), spriteAnimation.getX()-3, spriteAnimation.getY()-3, this)
        );

        blocks.stream().filter(block -> block.isVisible()).forEach(block -> g.drawImage(block.getImage(), GameActionUtil.getPercentSizeX(block, percentSizeMin), GameActionUtil.getPercentSizeY(block, percentSizeMin), GameActionUtil.getPercentSizeWidth(block, percentSizeMin), GameActionUtil.getPercentSizeHeight(block, percentSizeMin), this));
        //blocks.stream().filter(block -> block.isVisible()).forEach(block -> g.drawImage(block.getImage(), block.getX(), block.getY(), this));

        powerUps.stream().filter(powerUp -> powerUp.isVisible()).forEach(powerUp -> g.drawImage(powerUp.getImage(), GameActionUtil.getPercentSizeX(powerUp, percentSizeMin), GameActionUtil.getPercentSizeY(powerUp, percentSizeMin), GameActionUtil.getPercentSizeWidth(powerUp, percentSizeMin), GameActionUtil.getPercentSizeHeight(powerUp, percentSizeMin), this));
        //powerUps.stream().filter(powerUp -> powerUp.isVisible()).forEach(powerUp -> g.drawImage(powerUp.getImage(), powerUp.getX(), powerUp.getY(),this));

        tanks.forEach(tank -> {
            tank.getBullets().forEach(bullet -> {if (bullet.isVisible()) {
                g.drawImage(bullet.getImage(), GameActionUtil.getPercentSizeX(bullet, percentSizeMin), GameActionUtil.getPercentSizeY(bullet, percentSizeMin), GameActionUtil.getPercentSizeWidth(bullet, percentSizeMin), GameActionUtil.getPercentSizeHeight(bullet, percentSizeMin), this);
                //g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this);
            }});
        });

        drawEdge(g, percentSizeMin);
        g.setColor(Color.red);
        g.drawRect(0, 0, Const.BOARD_WIDTH + GameActionUtil.getPercentSize(Const.BOARD_WIDTH, percentSizeMin) , Const.BOARD_HEIGHT + GameActionUtil.getPercentSize(Const.BOARD_HEIGHT, percentSizeMin));
        //g.fillRect(0, 0, Const.BOARD_WIDTH, Const.BOARD_HEIGHT);

    }

    private void drawEnemies(Graphics g, int numEnemies, float percentSizeMin) {
        Image enemyIcon = imageUtilInstance.getEnemyIcon();
        int count = 1;
        int initY = 4;
        for (int i = 0; i < numEnemies; i++) {
            switch (count) {
                case 0:
                    count = 1;
                    //g.drawImage(enemyIcon, (Const.INIT_X + 1) * 16, initY * 16, this);
                    g.drawImage(enemyIcon, ( (Const.INIT_X + 1) * 16) + GameActionUtil.getPercentSize(( (Const.INIT_X + 1) * 16), percentSizeMin), (initY * 16) + GameActionUtil.getPercentSize((initY * 16), percentSizeMin), 16 + GameActionUtil.getPercentSize(16, percentSizeMin), 16 + GameActionUtil.getPercentSize(16, percentSizeMin), this);
                    initY++;
                    break;
                case 1:
                    count--;
                    //g.drawImage(enemyIcon, Const.INIT_X * 16, initY * 16, this);
                    g.drawImage(enemyIcon, ( (Const.INIT_X) * 16) + GameActionUtil.getPercentSize(( (Const.INIT_X) * 16), percentSizeMin), (initY * 16) + GameActionUtil.getPercentSize((initY * 16), percentSizeMin), 16 + GameActionUtil.getPercentSize(16, percentSizeMin), 16 + GameActionUtil.getPercentSize(16, percentSizeMin), this);
                    break;
                default:
                    break;
            }
        }
    }

    private void drawEdge(Graphics g, float percentSizeMin) {

        g.setColor(Color.GRAY);
        //g.fillRect((Const.INIT_X-1)* 16, 0, 4*16, 27*16);
        g.fillRect((Const.INIT_X-1)* 16 + GameActionUtil.getPercentSize( (Const.INIT_X-1)* 16, percentSizeMin), 0, 4*16 + GameActionUtil.getPercentSize(4*16, percentSizeMin), 27*16 + GameActionUtil.getPercentSize(27*16, percentSizeMin));
        drawEnemies(g, numEnemiesStage, percentSizeMin);

        g.setColor(Color.BLACK);

        // Draw lives
        String ipText = "1P";
        int lives = tankP1.getLives();
        Font font = Const.loadFont();
        g.setFont(font);
        //g.drawString(ipText, Const.INIT_X * 16, 17 * 16);
        g.drawString(ipText, Const.INIT_X * 16 + GameActionUtil.getPercentSize(Const.INIT_X * 16, percentSizeMin), 17 * 16 + GameActionUtil.getPercentSize(17 * 16, percentSizeMin));

        Image liveIcon = imageUtilInstance.getLives();


        if (cheatCodeIsActive) {
            g.drawString("Ch", (Const.INIT_X) * 16 + GameActionUtil.getPercentSize((Const.INIT_X) * 16, percentSizeMin), 13 * 16 + GameActionUtil.getPercentSize(13 * 16, percentSizeMin));
            g.drawString("Mod", (Const.INIT_X) * 16 + GameActionUtil.getPercentSize((Const.INIT_X) * 16, percentSizeMin), 14 * 16 + GameActionUtil.getPercentSize(14 * 16, percentSizeMin));
        }

        g.drawImage(liveIcon, (Const.INIT_X * 16) + GameActionUtil.getPercentSize((Const.INIT_X * 16), percentSizeMin), (17 * 16) + GameActionUtil.getPercentSize((17 * 16), percentSizeMin), 16 + GameActionUtil.getPercentSize(16, percentSizeMin), 16 + GameActionUtil.getPercentSize(16, percentSizeMin), this);
        //g.drawImage(liveIcon, Const.INIT_X * 16, 17 * 16, this);
        g.drawString(String.valueOf(lives < 0 ? 0 : lives), ((Const.INIT_X + 1) * 16) + GameActionUtil.getPercentSize(((Const.INIT_X + 1) * 16), percentSizeMin),  (18 * 16) + + GameActionUtil.getPercentSize((18 * 16), percentSizeMin));
        //g.drawString(String.valueOf(lives < 0 ? 0 : lives), (Const.INIT_X + 1) * 16,  18 * 16);

        if (gameType == GameType.MULTIPLAYER) {

            ipText = "2P";
            lives = tankP2.getLives();
            g.setFont(font);
            //g.drawString(ipText, Const.INIT_X * 16, 20 * 16);
            g.drawString(ipText, Const.INIT_X * 16 + GameActionUtil.getPercentSize(Const.INIT_X * 16, percentSizeMin), 20 * 16 + GameActionUtil.getPercentSize(20 * 16, percentSizeMin));

            //g.drawImage(liveIcon, Const.INIT_X * 16, 20 * 16, this);
            g.drawImage(liveIcon, (Const.INIT_X * 16) + GameActionUtil.getPercentSize((Const.INIT_X * 16), percentSizeMin), (20 * 16) + GameActionUtil.getPercentSize((20 * 16), percentSizeMin), 16 + GameActionUtil.getPercentSize(16, percentSizeMin), 16 + GameActionUtil.getPercentSize(16, percentSizeMin), this);

            //g.drawString(String.valueOf(lives < 0 ? 0 : lives), (Const.INIT_X + 1) * 16, 21 * 16);
            g.drawString(String.valueOf(lives < 0 ? 0 : lives), ((Const.INIT_X + 1) * 16) + GameActionUtil.getPercentSize(((Const.INIT_X + 1) * 16), percentSizeMin),  (21 * 16) + GameActionUtil.getPercentSize((21 * 16), percentSizeMin));
        }

        // Draw stages
        Image flagIcon = imageUtilInstance.getFlagIcon();
        //g.drawImage(flagIcon, Const.INIT_X * 16, 22 * 16, this);
        g.drawImage(flagIcon, (Const.INIT_X * 16) + GameActionUtil.getPercentSize((Const.INIT_X * 16), percentSizeMin), (22 * 16) + GameActionUtil.getPercentSize((22 * 16), percentSizeMin), 32 + GameActionUtil.getPercentSize(32, percentSizeMin), 32 + GameActionUtil.getPercentSize(32, percentSizeMin), this);

        //g.drawString(String.valueOf(stage), (Const.INIT_X + 1) * 16, 25 * 16);
        g.drawString(String.valueOf(stage), ((Const.INIT_X + 1) * 16) + GameActionUtil.getPercentSize(((Const.INIT_X + 1) * 16), percentSizeMin),  (25 * 16) + + GameActionUtil.getPercentSize((25 * 16), percentSizeMin));
    }



    private void initBlocks(){

        SoundUtility.startStage();
        try (BufferedReader br = new BufferedReader(new FileReader("stages/" + String.valueOf(stage)))) {

            String currentLine;
            int lineNum = 1;

            while ( (currentLine = br.readLine())  != null) {
                if (!currentLine.isEmpty()) {
                    char[] arrayOfChars = currentLine.trim().toCharArray();
                    int x_pos = 0;
                    for (char ch:arrayOfChars) {
                        switch (ch) {
                            case '#':
                                blocks.add(new Brick(x_pos * Const.BLOCK_WIDTH, lineNum * Const.BLOCK_HEIGHT));
                                break;
                            case '@':
                                blocks.add(new Steel(x_pos * Const.BLOCK_WIDTH, lineNum * Const.BLOCK_HEIGHT));
                                break;
                            case '%':
                                blocks.add(new Tree(x_pos * Const.BLOCK_WIDTH, lineNum * Const.BLOCK_HEIGHT));
                                break;
                            case '~':
                                blocks.add(new River(x_pos * Const.BLOCK_WIDTH, lineNum * Const.BLOCK_HEIGHT));
                                break;
                        }
                        x_pos += 1;
                    }
                    lineNum += 1;
                }
            }

            blocks.add(new Base(Const.BASE_POS_X, Const.BASE_POS_Y, allAnimations));

        } catch (IOException e) {
            SLF4J.getLogger().error("initBlocks error {}", e.getMessage());
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (pause) {
            return;
        }

        if (gameOver) {
            gameTimer.stop();
            return;
        }

        if (showLevelFrame) {
            repaint();
            return;
        }

        GameActionUtil.updateBlocks();
        GameActionUtil.updateTanks();
        GameActionUtil.updateTankBullets();
        GameActionUtil.updateAnimations();
        GameActionUtil.updatePowerUps();
        GameActionUtil.spawnPowerUp();
        spawnTankAI();
        checkPlayersTankHeath();

        nextLevel();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        final float percentSizeMin = gameWindow.getPercentSize();

        //drawEdge(g, percentSizeMin);
        drawObjects(g, percentSizeMin);
        endGame(g, percentSizeMin);
        gameLevelFrame(g, percentSizeMin);

    }

    private class keyEventListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_PAUSE) {
                if (!pause) {
                    SoundUtility.pause();
                }
                pause = !pause;
            }

            if (cheatCodeIsActive) {

                if (e.getKeyCode() == KeyEvent.VK_B) {
                    tankP1.upStarLevel();
                }
                if (e.getKeyCode() == VK_M) {
                    tanks.stream().filter(tank2 -> tank2.isVisible() && tank2.isAI()).forEach(tank2 -> tank2.setFrozen(true));
                }

                if (e.getKeyCode() == VK_N) {

                    tanks.stream().filter(enemy -> enemy.isVisible() && enemy.isAI()).forEach(enemy ->
                            {
                                Board.numEnemiesStage -= 1;
                                enemy.setVisible(false);
                                allAnimations.add(new ExplosionBig(enemy.getX(), enemy.getY()));
                                GameActionUtil.incrementNum((TankAI) enemy);
                                Board.decrementEnemies();
                            }

                    );

                }
            } else {

                cheatCodeBuffer.append((char)e.getKeyCode());
                if (!Const.ACTIVATE_CHEAT_MODE_WORD.contains(cheatCodeBuffer.toString())) {
                    cheatCodeBuffer.delete(0, cheatCodeBuffer.length()-1);
                }

                if (cheatCodeBuffer.toString().equalsIgnoreCase(Const.ACTIVATE_CHEAT_MODE_WORD)) {
                    cheatCodeIsActive = true;
                }
            }

            tanks.stream().filter(tank -> !tank.isAI()).forEach(tank -> ((PlayerTank)tank ).keyPressed(e));
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (pause) {
                return;
            }
            tanks.stream().filter(tank -> !tank.isAI()).forEach(tank -> ((PlayerTank)tank).keyReleased(e));
        }
    }

    public void nextLevel() {

        if (gameOver) return;

        boolean enemyIsEmpty = !tanks.stream().filter(tank -> tank.isAI()).anyMatch(tank -> tank.isAI());

        if (enemyIsEmpty) {
            if (stage == 35) {
                loadScoreBoard(gameWindow, gameType);
            } else {
                stage += 1;
                numAICreated = 0;
                numEnemiesStage = Const.NUM_ENEMIES;
                blocks.clear();
                tanks.clear();
                initPlayerTank(false);
                initBlocks();
                Collisions.loadCollisionInstance(blocks, allAnimations, powerUps, tanks);
                GameActionUtil.loadGameActionUtil(tanks, blocks, allAnimations, powerUps);
                showFrameLevel();
            }
        }
    }

    private void loadUtilInstance() {
        Collisions.loadCollisionInstance(blocks, allAnimations, powerUps, tanks);
        GameActionUtil.loadGameActionUtil(tanks, blocks, allAnimations, powerUps);
    }

    public static void decrementEnemies() {
        numEnemiesStage -= 1;
    }

    private void spawnTankAI() {

        numAICreated = 0;

        while (numAICreated < numEnemiesStage) {
            if (tanks.stream().filter(tank -> tank.isVisible() && tank.isAI()).count() < 4) {

                if (numAICreated < 2) {
                    GameActionUtil.spawnTankAI(Difficulty.EASY);
                } else if (numAICreated >= 2 && numAICreated < 6) {
                    GameActionUtil.spawnTankAI(Difficulty.NORMAL);
                } else if (numAICreated >= 6) {
                    GameActionUtil.spawnTankAI(Difficulty.HARD);
                }
                numAICreated++;
            } else {
                break;
            }
        }

    }

    public static int getStage() {
        return stage;
    }

    public void endGame(Graphics g, float percentSizeMin) {

        if (gameOver) {
            g.setFont(font);
            g.setColor(Color.RED);
            //g.drawString("GAME OVER", Const.BOARD_WIDTH / 2 - 85, yPos);
            g.drawString("GAME OVER", (Const.BOARD_WIDTH / 2 - 85) + GameActionUtil.getPercentSize((Const.BOARD_WIDTH / 2 - 85), percentSizeMin),
                    yPos + GameActionUtil.getPercentSize(yPos ,percentSizeMin));

            //if (yPos == stopYPos && !sourceBoardTimerCreated) {
            if (yPos + GameActionUtil.getPercentSize(yPos, percentSizeMin) == stopYPos + GameActionUtil.getPercentSize(stopYPos, percentSizeMin) && !sourceBoardTimerCreated) {
                sourceBoardTimerCreated = true;
                gameOverTimer.stop();
                SoundUtility.stopAllSound();
                Timer sourceBoardTimer = new Timer(3000, new ActionListener() {
                    @Override
                    public void actionPerformed(
                            ActionEvent e) {
                        loadScoreBoard(gameWindow, gameType);
                    }
                });
                sourceBoardTimer.setRepeats(false);
                sourceBoardTimer.start();
            }
        }

        if (gameOver && !gameOverTimerStarted) {
            gameOverTimerStarted = true;
            gameOverTimer.start();
        }
    }

    public void gameLevelFrame(Graphics g, float percentSizeMin) {

        if (showLevelFrame) {

            g.setColor(Color.GRAY);

            //g.drawRect(0, 0, Const.BOARD_WIDTH + getPercentSize(Const.BOARD_WIDTH, percentSizeMin) , Const.BOARD_HEIGHT + getPercentSize(Const.BOARD_HEIGHT, percentSizeMin));

            //g.fillRect(0, (Const.BOARD_WIDTH / 2) , Const.BOARD_WIDTH, stageFrameH *-1);

            g.fillRect(0, (Const.BOARD_WIDTH / 2) + GameActionUtil.getPercentSize((Const.BOARD_WIDTH / 2), percentSizeMin) , (Const.BOARD_WIDTH) + GameActionUtil.getPercentSize((Const.BOARD_WIDTH), percentSizeMin), (stageFrameH + GameActionUtil.getPercentSize((stageFrameH), percentSizeMin))*-1) ;

            //g.fillRect(0, (Const.BOARD_WIDTH / 2) - 30, Const.BOARD_WIDTH, stageFrameH);

            g.fillRect(0, ((Const.BOARD_WIDTH / 2)) + GameActionUtil.getPercentSize(((Const.BOARD_WIDTH / 2)), percentSizeMin) , (Const.BOARD_WIDTH) + GameActionUtil.getPercentSize((Const.BOARD_WIDTH), percentSizeMin), stageFrameH + GameActionUtil.getPercentSize((stageFrameH), percentSizeMin)) ;

            g.setFont(font);
            g.setColor(Color.BLACK);
            //g.drawString("STAGE" + stage, Const.BOARD_WIDTH / 2 - 85, Const.BOARD_HEIGHT/2);
            g.drawString("STAGE" + stage, (Const.BOARD_WIDTH / 2 - 85) + GameActionUtil.getPercentSize((Const.BOARD_WIDTH / 2 - 85), percentSizeMin), (Const.BOARD_HEIGHT/2) + GameActionUtil.getPercentSize((Const.BOARD_HEIGHT/2), percentSizeMin));

            if (!stageFrameTimerCreated) {
                stageFrameTimerCreated = true;
                gameOverTimer.stop();
                stageFrameTimer = new Timer(3, new ActionListener() {
                    @Override
                    public void actionPerformed(
                            ActionEvent e) {

                        stageFrameH -=1;

                        if (stageFrameH <= 30) {
                            showLevelFrame = false;
                            stageFrameTimer.stop();
                        }


                    }
                });
               //stageFrameTimer.setRepeats(false);
                stageFrameTimer.start();
            }
        }

    }

    public static void showFrameLevel() {
        showLevelFrame = true;
        stageFrameTimerCreated = false;
        stageFrameH = Const.BOARD_WIDTH/2;
    }

    public void loadScoreBoard(MainWindow theView, GameType gameType) {
        theView.getGamePanel().removeAll();
        ScoreBoard scoreBoard = new ScoreBoard(theView, gameType);
        scoreBoard.setBackground(Color.BLACK);
        theView.getGamePanel().add(scoreBoard);
        scoreBoard.requestFocusInWindow();
        SoundUtility.statistics();
        theView.setVisible(true);
    }

}


