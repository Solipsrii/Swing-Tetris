package swingAnimator.sequences;

import assets.GameData;
import assets.Piece;
import swingAnimator.AnimLayer;
import swingAnimator.SwingAnimatorListener;

import java.awt.*;

/**
 * Draws a single "outline", i.e: generates a single unit of blocks around the given Piece.
 * currently not in used, due to a bug in which the outline is drawn on top of the screen on new piece generation.
 * likely because "currentPiece" in manager is always the same Piece, with the same reference manipulations also come
 * problems. This is not a feature that is worth the hassle to deep-edit the code for.
 */
public class OutlineDrawSequence extends SwingAnimatorListener {
    private Piece currentPiece;
    private Color[][] well;
    private Color mainColor;
    private int maxFrames, frameDelay, countFrames;
    private boolean canDeleteSequence, drew;


    //reuisng code from OutlineExpansion. This is a poor solution. Should only access values, not calculate them.
    public OutlineDrawSequence(Piece inputPiece){


        maxFrames = GameData.ticksPerSecond / GameData.originalTicksLogic;
        countFrames = 0;
        frameDelay  = maxFrames*15;

        currentPiece = new Piece(inputPiece); //clones piece so weird draws don't occur.
        mainColor = currentPiece.shape[0].color;;
        well = new Color[GameData.Y][GameData.X];
    }

    public OutlineDrawSequence(Color color, Piece currentPiece){
        this(currentPiece);
        mainColor = color;

        for(int i=0;i<4;i++){
            int v = currentPiece.getActualYLocation(i);
            int h = currentPiece.getActualXLocation(i);
            well[v][h] = mainColor;
        }
        canDeleteSequence = false;
        drew = false;
    }

    //no need to set special coordinates, gotten coordinates from currentPiece.
    @Override
    public void animationInitialize(int x, int y) {

    }

    @Override
    public void animationUpdate() {
        //edit color of well around current piece.
        if (!drew) {
            drew = true;
            //start painting around piece
            paintWell(mainColor);
        }

        else {
            if (countFrames >= frameDelay) {
                //start deleting color
                paintWell(Color.BLACK);
                canDeleteSequence = true;
            }
            else
                countFrames++;
        }
    }

    @Override
    public boolean animationTerminate() {
        return canDeleteSequence;
    }

    @Override
    public boolean filterByLayer(AnimLayer layer) {
        return (layer == AnimLayer.BACKGROUND);
    }

    @Override
    public void draw(Graphics G) {
        G.setColor(mainColor);
        for(int i = 0; i <= GameData.Y-2; i++)
            for(int j= 1; j <= GameData.X-2; j++) {
                if (well[i][j] == mainColor)
                    G.fillRect(j * GameData.BLOCK_RATIO, i * GameData.BLOCK_RATIO, GameData.BLOCK_RATIO, GameData.BLOCK_RATIO);
            }
        System.out.println("Sequence: OutlineDraw");
    }


    private void paintWell(Color chosenColor){
        for(int i = currentPiece.getActualYLocation(0); i <= currentPiece.getActualYLocation(3); i++)
            for(int j=currentPiece.getActualXLocation(0); j <= currentPiece.getActualXLocation(3); j++) //well[y][x]
                well[i][j] = chosenColor;
    }
}
