package manager;

import assets.*;
import assets.panels.PanelFodder;
import assets.panels.PanelGame;
import assets.panels.PanelPiecePreview;
import assets.tetrisEvents.MenuEventListener;
import assets.tetrisEvents.TetrisEventListener;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import java.awt.*;


/**
 * Remember paintComponent, not componentSSSS!
 * Create private ENUM type in class, and then create a method that uses parameter enum type
 */


public class PanelManager extends JPanel {
    private final int
            MAX_X = GameData.BLOCK_RATIO * GameData.X,
            MAX_Y = GameData.BLOCK_RATIO * GameData.Y;

    private JPanel pauseMenu, bufferPausePanelL, bufferPausePanelR;
    private JPanel featuresPanel;
    private PanelGame gamePanel;
    private PanelPiecePreview piecePreviewPanel;
    private PanelFodder fodderPanel;
    private PanelManager self = this;
    private EventManager eventManager = GameData.eventManager;
    private Manager gameManager = GameData.gameManager;


    //manager-objects references grabbed here
    private Piece nextPiece = gameManager.nextPiece;
    private Piece currrentPiece = gameManager.currentPiece;
    private Block[][] well = gameManager.well;
    private Score score = gameManager.score;

    /**
     * Meun Event Listener : auto adds itself to EventManager.
     */
    private MenuEventListener menuListener = new MenuEventListener() {

        @Override
        public void onGamePause() {
            hideGameMenu();
            resumePauseMenu();
        }

        @Override
        public void onGameResume() {
            hidePauseMenu();
            resumeGameMenu();
        }

        @Override
        public void onGameStart() {

             featuresPanel = new JPanel();
            gamePanel = new PanelGame(well, currrentPiece);


            fodderPanel = new PanelFodder(score);
            piecePreviewPanel = new PanelPiecePreview(nextPiece);
            featuresPanel.add(piecePreviewPanel);
            featuresPanel.add(fodderPanel);
            featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));

            add(gamePanel);
            add(featuresPanel);
            setLayout(new BoxLayout(self, BoxLayout.X_AXIS));
            featuresPanel.setAlignmentY(Component.TOP_ALIGNMENT);
            gamePanel.setAlignmentY(Component.TOP_ALIGNMENT);


            pauseMenu = new JPanel(){
                @Override
                public Dimension getPreferredSize() {
                    return gamePanel.getPreferredSize();
                }

                @Override
                public Dimension getMaximumSize() {
                    return getPreferredSize();
                }

                @Override
                public Dimension getMinimumSize() {
                    return getPreferredSize();
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    //TODO:: Add in PAUSE MENU paint method, yo.
                    g.setColor(Color.BLUE);
                }
            };

            bufferPausePanelL = new JPanel(){
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(gamePanel.getPreferredSize().width/3, gamePanel.getPreferredSize().height);
                }

                @Override
                public Dimension getMaximumSize() {
                    return getPreferredSize();
                }

                @Override
                public Dimension getMinimumSize() {
                    return getPreferredSize();
                }

            };

            bufferPausePanelR = new JPanel(){
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(gamePanel.getPreferredSize().width/3, gamePanel.getPreferredSize().height);
                }

                @Override
                public Dimension getMaximumSize() {
                    return getPreferredSize();
                }

                @Override
                public Dimension getMinimumSize() {
                    return getPreferredSize();
                }

            };

            pauseMenu.setAlignmentY(Component.TOP_ALIGNMENT);
            bufferPausePanelL.setAlignmentY(Component.TOP_ALIGNMENT);
            bufferPausePanelL.setBackground(Color.BLACK);
            bufferPausePanelR.setAlignmentY(Component.TOP_ALIGNMENT);
            bufferPausePanelR.setBackground(Color.BLACK);
        }
        //^END OF "CONSTRUCTOR"
    };

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(new PanelGame(null, null).getPreferredSize().width +
                new PanelPiecePreview(null).getPreferredSize().width
                , MAX_Y);
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
    }






    private void refreshPage(){
        GameData.FRAME.pack();
        GameData.FRAME.repaint();
    }

    private void hideGameMenu(){
        remove(gamePanel);
        remove(featuresPanel);
    }
    private void resumeGameMenu(){
        add(gamePanel);
        add(featuresPanel);
        refreshPage();
    }

    private void resumePauseMenu(){
        add(bufferPausePanelL);
        add(pauseMenu);
        add(bufferPausePanelR);
        refreshPage();
    }

    private void hidePauseMenu(){
        remove(bufferPausePanelL);
        remove(pauseMenu);
        remove(bufferPausePanelR);
    }
}