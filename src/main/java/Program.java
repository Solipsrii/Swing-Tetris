import assets.tetrisEvents.MenuType;
import manager.PanelManager;

import javax.swing.*;

public class Program {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                   JFrame f = new JFrame();
                   assets.GameData.FRAME = f;
                   f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                   manager.Manager gameManager = new manager.Manager(f);

                   f.add(gameManager.getPanelManager());
                   f.pack();
                   f.setVisible(true);

                   gameManager.startGame();
               }
           });
    }
}
