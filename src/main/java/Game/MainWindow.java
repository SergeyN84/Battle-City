package Game;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private JPanel gamePanel;

    public MainWindow() {
       initComponents();
       setLocationRelativeTo(null);
   }

    private void initComponents() {

        gamePanel = new javax.swing.JPanel();

        setIconImage(ImageUtil.getInstance().getTank_player1_right_c0_t1());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Battle City");

        setPreferredSize(new java.awt.Dimension(512, 470));
        //setPreferredSize(new java.awt.Dimension(462, 470));

        gamePanel.setMinimumSize(new java.awt.Dimension(500, 500));
        gamePanel.setSize(new java.awt.Dimension(528, 448));
        gamePanel.setLayout(new java.awt.GridLayout(1, 0));
        //this.setResizable(false);

        getContentPane().add(gamePanel);

//        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
//        getContentPane().setLayout(layout);
//        layout.setHorizontalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addComponent(gamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//        );
//        layout.setVerticalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addComponent(gamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//        );

        pack();
    }

    public float getPercentSize() {
        Dimension size = this.getBounds().getSize();
        final float percentSizeHeight = (size.height - 470 == 0 || size.height - 470 < 0)? 0: (float) ( ( size.height - 470 ) * 100 ) / 470;
        final float percentSizeWidth = (size.width - 512 == 0 || size.width - 512 < 0)? 0: (float) ( ( size.width - 512 ) *100 ) / 512;
        return Math.min(percentSizeWidth, percentSizeHeight);
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

}
