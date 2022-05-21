package Game;

import Enum.*;
import Log.SLF4J;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.event.KeyEvent.*;

public class Menu extends JPanel implements KeyListener {

    private final MainWindow gameWindow;
    private int yPos = Const.BOARD_HEIGHT;
    private int yTankPos = yPos;

    private int [] menuPos = new int [] {0,0,0};

    private int direction = -1;
    private final int stopYPos = 100;
    private int currentTankImg = 1;
    private static Font menuFont = Const.loadFont();

    private Image background, getTank_player1_right_c0_t1, getTank_player1_right_c0_t2;
    private final ImageUtil imageInstance = ImageUtil.getInstance();

    private void loadBord(GameType gameType){
        gameWindow.getGamePanel().removeAll();
        Board panel = new Board(gameWindow, gameType);
        gameWindow.getGamePanel().add(panel);
        panel.requestFocusInWindow();
        gameWindow.setVisible(true);
    }

    public Menu(MainWindow mainWindow) {
        gameWindow = mainWindow;
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);
        addKeyListener(this);
        loadImages();
        createTimer();
    }

    private void createTimer(){
        new Timer(8, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(yPos > stopYPos) {
                    yPos -= 2;
                    yTankPos = yPos + background.getHeight(null) + 25;
                }
                repaint();
            }
        }).start();
    }

    private void loadImages(){
        background = imageInstance.getBackground();
        getTank_player1_right_c0_t1 = imageInstance.getTank_player1_right_c0_t1();
        getTank_player1_right_c0_t2 = imageInstance.getTank_player1_right_c0_t2();
    }

    private void drawMenu(Graphics g, float percentSizeMin) {

        if (currentTankImg == 1) {
            //g.drawImage(getTank_player1_right_c0_t1, (Const.BOARD_WIDTH / 2 - 90), yTankPos, this);
            g.drawImage(getTank_player1_right_c0_t1, (Const.BOARD_WIDTH / 2 - 90) + GameActionUtil.getPercentSize((Const.BOARD_WIDTH / 2 - 90), percentSizeMin), yTankPos + GameActionUtil.getPercentSize(yTankPos, percentSizeMin),
                    32 + GameActionUtil.getPercentSize(32, percentSizeMin), 32 + GameActionUtil.getPercentSize(32, percentSizeMin),this);
            currentTankImg = 2;
        } else {
            //g.drawImage(getTank_player1_right_c0_t2, Const.BOARD_WIDTH / 2 - 90, yTankPos, this);
            g.drawImage(getTank_player1_right_c0_t2, (Const.BOARD_WIDTH / 2 - 90) + GameActionUtil.getPercentSize((Const.BOARD_WIDTH / 2 - 90), percentSizeMin), yTankPos + GameActionUtil.getPercentSize(yTankPos, percentSizeMin),
                    32 + GameActionUtil.getPercentSize(32, percentSizeMin), 32 + GameActionUtil.getPercentSize(32, percentSizeMin),this);
            currentTankImg = 1;
        }

        g.setFont(menuFont);
        g.setColor(Color.WHITE);
//        g.drawString("PRESS ENTER", Const.BOARD_WIDTH / 2 - 80, Const.BOARD_HEIGHT * 4 / 5 + 25);
        g.drawString("PRESS ENTER",
                (Const.BOARD_WIDTH / 2 - 80) + GameActionUtil.getPercentSize((Const.BOARD_WIDTH / 2 - 80), percentSizeMin),
                (Const.BOARD_HEIGHT * 4 / 5 + 25) + GameActionUtil.getPercentSize((Const.BOARD_HEIGHT * 4 / 5 + 25), percentSizeMin));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final float percentSizeMin = gameWindow.getPercentSize();

        //g.drawImage(background,Const.BOARD_WIDTH / 2 - background.getWidth(null) / 2 - 10, yPos, this);
        g.drawImage(background,(Const.BOARD_WIDTH / 2 - background.getWidth(null) / 2 - 10) +
                GameActionUtil.getPercentSize((Const.BOARD_WIDTH / 2 - background.getWidth(null) / 2 - 10), percentSizeMin),
                yPos + GameActionUtil.getPercentSize(yPos, percentSizeMin),
                376 + GameActionUtil.getPercentSize(376, percentSizeMin),
                136 + GameActionUtil.getPercentSize(136, percentSizeMin),
                this);

        g.setFont(menuFont);
        menuPos[0] = yPos + background.getHeight(null) + 25;
        //g.drawString("1 PLAYER", Const.BOARD_WIDTH / 2 - 56, yPos + background.getHeight(null) + 50);
        g.drawString("1 PLAYER", (Const.BOARD_WIDTH / 2 - 56) + GameActionUtil.getPercentSize((Const.BOARD_WIDTH / 2 - 56), percentSizeMin),
                (yPos + background.getHeight(null) + 50) + GameActionUtil.getPercentSize((yPos + background.getHeight(null) + 50), percentSizeMin));

        menuPos[1] = yPos + background.getHeight(null) + 55;
        //g.drawString("2 PLAYER", Const.BOARD_WIDTH / 2 - 56,  yPos + background.getHeight(null) + 80);
        g.drawString("2 PLAYER", (Const.BOARD_WIDTH / 2 - 56) + GameActionUtil.getPercentSize((Const.BOARD_WIDTH / 2 - 56), percentSizeMin),
                (yPos + background.getHeight(null) + 80) + GameActionUtil.getPercentSize((yPos + background.getHeight(null) + 80), percentSizeMin));

        menuPos[2] = yPos + background.getHeight(null) + 85;
        //g.drawString("Exit", Const.BOARD_WIDTH / 2 - 56, yPos + background.getHeight(null) + 110);
        g.drawString("Exit", (Const.BOARD_WIDTH / 2 - 56) + GameActionUtil.getPercentSize((Const.BOARD_WIDTH / 2 - 56), percentSizeMin),
                (yPos + background.getHeight(null) + 110) + GameActionUtil.getPercentSize((yPos + background.getHeight(null) + 110), percentSizeMin));

        if (yPos == stopYPos) {
            drawMenu(g, percentSizeMin);
        }
    }

    private int getMenuPosition(int keyCode) {

        if ( (keyCode == VK_DOWN || keyCode == VK_UP) && yPos == stopYPos) {  {

            if (keyCode == VK_DOWN && yTankPos == menuPos[2]) {
                return menuPos[0];
            }

            if (keyCode == VK_UP && yTankPos == menuPos[0]) {
                return menuPos[2];
            }

            if (keyCode == VK_DOWN) {
                for (int i = 0; i < menuPos.length; i++) {
                    if (menuPos[i] > yTankPos) {
                        return menuPos[i];
                    }
                }
            }

            if (keyCode == VK_UP) {
                for (int i = menuPos.length-1; i >= 0; i--) {
                    if (menuPos[i] < yTankPos) {
                        return menuPos[i];
                    }
                }
            }

        }

    }
        return yTankPos;

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        yTankPos = getMenuPosition(e.getKeyCode());

        if (e.getKeyCode() == VK_ENTER && yTankPos==menuPos[2]) {
            System.exit(0);
        }

        if (e.getKeyCode() == VK_ENTER && yTankPos==menuPos[0] ) {
            loadBord(GameType.SINGLE_PLAYER);
        }
        else if (e.getKeyCode() == VK_ENTER && yTankPos==menuPos[1] ) {
            loadBord(GameType.MULTIPLAYER);
        }

    }
}
