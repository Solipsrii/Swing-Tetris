package assets.panels;

import assets.Block;
import assets.GameData;
import assets.Piece;
import assets.tetrisEvents.TetrisEventListener;
import swingAnimator.AnimLayer;
import swingAnimator.SwingAnimatorListener;

import java.awt.*;
import java.util.ArrayList;

public class PanelGame extends GamePiecePanel {

    Block[][] well;
    Piece currentPiece;
    TetrisEventListener landedListener;
    ArrayList<SwingAnimatorListener> drawablesFRONT;
    ArrayList<SwingAnimatorListener> drawablesBACKGROUND;
    boolean lineCleared;

    public PanelGame(Block[][] well, Piece currentPiece){
        this.well = well;
        this.currentPiece = currentPiece;
        this.lineCleared = false;
        this.drawablesFRONT = new ArrayList<>();
        this.drawablesBACKGROUND = new ArrayList<>();
    }




    @Override
    public Dimension getPreferredSize() { //maxX - featuresPanel.getPreferredSize().width
        final int
                MAX_X = GameData.BLOCK_RATIO * GameData.X,
                MAX_Y = GameData.BLOCK_RATIO * GameData.Y;
        return new Dimension(MAX_X, MAX_Y);
    }
    @Override
    public Dimension getMaximumSize() {return getPreferredSize(); }
    @Override
    public Dimension getMinimumSize() {return getPreferredSize(); }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(SwingAnimatorListener drawable : GameData.animationDispatcher.getAllListeners()) {
            //decide whether or not to draw the layer in the background or foreground.
            if (drawable.filterByLayer(AnimLayer.FRONT))
                drawablesFRONT.add(drawable);
            else
                drawablesBACKGROUND.add(drawable);
        }

        //DRAW BG ANIMATIONS BEHIND...WELL...THE BACKGROUND.
        for (SwingAnimatorListener drawable : drawablesBACKGROUND)
            drawable.draw(g);

        drawBackground  (g, well);
        drawPiece(g, currentPiece);
        setBackground(Color.GRAY);

        //draw front
        for (SwingAnimatorListener drawable : drawablesFRONT)
            drawable.draw(g);

        drawablesFRONT.clear();
        drawablesBACKGROUND.clear();
    }



    /**********************************
     *  HELPER METHODS FROM HERE
     ***********************************/




}