package Game;

import Enum.GameType;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A class for showing the totalScore
 *
 * @author Tongyu
 */
public class ScoreBoard extends JPanel implements ActionListener, KeyListener {

    /**
     * Initialize instance variables for the ScoreBoard
     */
    private GameType gameType;
    private MainWindow theView;
    private int stage, totalTankNum;
    private int totalScore = 0;
    private final int SHIFT = 80;
    private JButton restartButton;
    private final ImageUtil imageInstance = ImageUtil.getInstance();
    private int[] tankScoreList = {0, 0, 0, 0};
    private int[] tankNumList = {0, 0, 0, 0};

    /**
     * Constructor for the ScoreBoard. A restart button is added for the player
     * to restart the game
     *
     * @param theView GameView that represents the frame of the game
     */
    public ScoreBoard(MainWindow theView, GameType gameType) {
        this.theView = theView;
        this.setFocusable(true);
        theView.setForeground(Color.BLACK);
        this.setLayout(null);

        restartButton = new JButton();
        restartButton.setText("Restart");
        this.add(restartButton);
        restartButton.setBounds(400, 400,100, 30);
        restartButton.addActionListener(this);
        this.gameType = gameType;
    }

    /**
     * Draw the scoreBorad with different types of enemies on the screen.
     *
     * @param g Graphics
     */
    @Override
    public void paintComponent(Graphics g) {

        final float percentSizeMin = theView.getPercentSize();

        restartButton.setBounds(400 + GameActionUtil.getPercentSize(400, percentSizeMin), 400+ GameActionUtil.getPercentSize(400, percentSizeMin),
                100+ GameActionUtil.getPercentSize(100, percentSizeMin), 30 + GameActionUtil.getPercentSize(30, percentSizeMin));

        loadScore();
        stage = Board.getStage();
        super.paintComponent(g);
        Font font = Const.loadFont();
        ArrayList<Image> tankList = new ArrayList<>(
                Arrays.asList(imageInstance.getTankBasic(),
                        imageInstance.getTankFast(),
                        imageInstance.getTankPower(),
                        imageInstance.getTankArmor()));

        g.setFont(font);
        g.setColor(Color.WHITE);
        //g.drawString("STAGE   " + String.valueOf(stage), 97 + SHIFT, 60);
        g.drawString("STAGE   " + String.valueOf(stage), (97 + SHIFT) + GameActionUtil.getPercentSize((97 + SHIFT), percentSizeMin),
                60 + GameActionUtil.getPercentSize(60, percentSizeMin));

        g.setColor(Color.RED);

        if (gameType == GameType.SINGLE_PLAYER) {
            //g.drawString("1-PLAYER", 37 + SHIFT, 95);
            g.drawString("1-PLAYER", (37 + SHIFT) + GameActionUtil.getPercentSize((37 + SHIFT), percentSizeMin), 95 + GameActionUtil.getPercentSize(95, percentSizeMin));
        } else if (gameType == GameType.MULTIPLAYER) {
            //g.drawString("PLAYERS", 37 + SHIFT, 95);
            g.drawString("PLAYERS", (37 + SHIFT) + GameActionUtil.getPercentSize((37 + SHIFT), percentSizeMin), 95 + GameActionUtil.getPercentSize(95, percentSizeMin));
        }

        g.setColor(Color.orange);
        //g.drawString(String.valueOf(totalScore), 121 + SHIFT, 130);
        g.drawString(String.valueOf(totalScore), (121 + SHIFT) + GameActionUtil.getPercentSize((121 + SHIFT), percentSizeMin),
                130 + GameActionUtil.getPercentSize(130, percentSizeMin));

        for (int i = 0; i < 4; i++) {
            //g.drawImage(tankList.get(i), 226 + SHIFT, 160 + (i * 45), this);
            g.drawImage(tankList.get(i), (226 + SHIFT)  + GameActionUtil.getPercentSize((226 + SHIFT), percentSizeMin),
                    (160 + (i * 45)) + GameActionUtil.getPercentSize((160 + (i * 45)), percentSizeMin),
                    32 + GameActionUtil.getPercentSize(32, percentSizeMin),
                    32 + GameActionUtil.getPercentSize(32, percentSizeMin),
                    this);
            //g.drawImage(imageInstance.getArrow(), 206 + SHIFT, 168 + (i * 45),this);
            g.drawImage(imageInstance.getArrow(), (206 + SHIFT) + GameActionUtil.getPercentSize((206 + SHIFT), percentSizeMin),
                    (168 + (i * 45)) + GameActionUtil.getPercentSize((168 + (i * 45)), percentSizeMin),
                    14 + GameActionUtil.getPercentSize(14, percentSizeMin),
                    14 + GameActionUtil.getPercentSize(14, percentSizeMin),
                    this);
        }
        for (int i = 0; i < 4; i++) {
            g.setColor(Color.WHITE);
            //g.drawString(String.valueOf(tankScoreList[i]), 55 + SHIFT,180 + (i * 45));
            g.drawString(String.valueOf(tankScoreList[i]), (55 + SHIFT) + GameActionUtil.getPercentSize((55 + SHIFT), percentSizeMin),
                    (180 + (i * 45)) + GameActionUtil.getPercentSize((180 + (i * 45)), percentSizeMin));
            //g.drawString("PTS", 115 + SHIFT, 180 + (i * 45));
            g.drawString("PTS", (115 + SHIFT) + GameActionUtil.getPercentSize((115 + SHIFT), percentSizeMin),
                    (180 + (i * 45)) +  + GameActionUtil.getPercentSize((180 + (i * 45)), percentSizeMin));
        }

        for (int i = 0; i < 4; i++) {
            g.setColor(Color.WHITE);
            //g.drawString(String.valueOf(tankNumList[i]), 180 + SHIFT,180 + (i * 45));
            g.drawString(String.valueOf(tankNumList[i]), (180 + SHIFT) + GameActionUtil.getPercentSize((180 + SHIFT), percentSizeMin),
                    (180 + (i * 45)) +  + GameActionUtil.getPercentSize((180 + (i * 45)), percentSizeMin));
        }

        //g.drawLine(170, 330, 307, 330);
        g.drawLine(170 + GameActionUtil.getPercentSize(170, percentSizeMin), 330 + GameActionUtil.getPercentSize(330, percentSizeMin),
                307 + GameActionUtil.getPercentSize(307, percentSizeMin), 330 + GameActionUtil.getPercentSize(330, percentSizeMin));

        //g.drawString("TOTAL", 85 + SHIFT, 355);
        g.drawString("TOTAL", (85 + SHIFT) + GameActionUtil.getPercentSize((85 + SHIFT), percentSizeMin),
                355 + GameActionUtil.getPercentSize(355, percentSizeMin));
        //g.drawString(String.valueOf(totalTankNum), 180 + SHIFT, 355);
        g.drawString(String.valueOf(totalTankNum), (180 + SHIFT)  + GameActionUtil.getPercentSize((180 + SHIFT), percentSizeMin),
                355 + GameActionUtil.getPercentSize(355, percentSizeMin));
        g.setFont(font);
        g.setColor(Color.WHITE);
    }

    public void loadScore() {
        for (int i = 0; i < 4; i++) {
            int[] enemyTankNum = GameActionUtil.getEnemyTankNum();
            tankNumList[i] = enemyTankNum[i];
        }
        for (int i = 0; i < 4; i++) {
            tankScoreList[i] = tankNumList[i] * 100 * (i + 1);
        }
        for (Integer score : tankScoreList) {
            totalScore += score;
        }
        for (Integer num : tankNumList) {
            totalTankNum += num;
        }
    }

    public void restart() {
        Board.setGameOver(false);
        GameActionUtil.resetScore();
        loadMenu();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartButton) {
            restart();
        }
    }

    private void loadMenu() {
        theView.getGamePanel().removeAll();
        Menu menu = new Menu(theView);
        menu.revalidate();
        menu.repaint();
        theView.getGamePanel().add(menu);
        menu.requestFocusInWindow();
        theView.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            loadMenu();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            loadMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            loadMenu();
        }
    }
}

