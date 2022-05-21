package Game;

public class Main {

    public static void main(String[] args)  {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow theView = new MainWindow();
                theView.getGamePanel().add(new Menu(theView));
                theView.setVisible(true);
            }
        });

    }

}
