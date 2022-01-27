package swingAnimator.sequences;
import assets.Block;
import assets.GameData;
import assets.Piece;
import swingAnimator.*;

import java.awt.*;
import java.util.ArrayList;

public class OutlineExpansionSequence extends SwingAnimatorListener{
    private Piece delPiece;
    private Color mainColor;
    private int minX, minY, maxX, maxY;
    private boolean delete = false;
    private int originalMinX, originalMinY, originalMaxX, originalMaxY;
    private Color[][] well;
    int maxFrames, countFrames, frameDelay;

/**
   Creates new block in the background, behind the grey cracks of the blocks, to simulate an expanding color.
    Blocks are created one after another in an outline upon each iteration, and then gets deleted from the center outward,
    effectively creating a "shockwave" effect.
 */
    public OutlineExpansionSequence(Piece currentPiece){
        delPiece = new Piece();
        delPiece.updateShape(currentPiece,0,0);

        //due to updating the rotation method (stupidly), it is required (sadly) to find the min-max of each coordinate,
        //to properly generate the given effect.
             minX = Math.min(currentPiece.shape[0].templatePoint.x, currentPiece.shape[3].templatePoint.x);
        int minX2 = Math.min(currentPiece.shape[1].templatePoint.x, currentPiece.shape[2].templatePoint.x);
        minX = Math.min(minX, minX2) + currentPiece.currentLocation.x;

             maxX = Math.max(currentPiece.shape[0].templatePoint.x, currentPiece.shape[3].templatePoint.x);
        int maxX2 = Math.max(currentPiece.shape[1].templatePoint.x, currentPiece.shape[2].templatePoint.x);
        maxX = Math.max(maxX, maxX2) + currentPiece.currentLocation.x;

             minY = Math.min(currentPiece.shape[0].templatePoint.y, currentPiece.shape[3].templatePoint.y);
        int minY2 = Math.min(currentPiece.shape[1].templatePoint.y, currentPiece.shape[2].templatePoint.y);
        minY = Math.min(minY, minY2) + currentPiece.currentLocation.y;

             maxY = Math.max(currentPiece.shape[0].templatePoint.y, currentPiece.shape[3].templatePoint.y);
        int maxY2 = Math.max(currentPiece.shape[1].templatePoint.y, currentPiece.shape[2].templatePoint.y);
        maxY = Math.max(maxY, maxY2) + currentPiece.currentLocation.y;

        well = new Color[GameData.Y][GameData.X];
        for(int y=0; y < GameData.Y-1; y++)
            for (int x = 1; x < GameData.X-1; x++) { //1~9
                    well[y][x] = Color.BLACK;
            }
        for(int i=0;i<4;i++){
            int v = currentPiece.getActualYLocation(i);
            int h = currentPiece.getActualXLocation(i);
            well[v][h] = mainColor;
        }

        maxFrames = GameData.ticksPerSecond / GameData.originalTicksLogic;
        countFrames = maxFrames;
        frameDelay  = maxFrames*30;
        originalMaxX = maxX;
        originalMaxY = maxY;
        originalMinX = minX;
        originalMinY = minY;

        mainColor = currentPiece.shape[0].color;
    }

    public OutlineExpansionSequence(Color color, Piece currentPiece){
        this(currentPiece);
        mainColor = color;

        for(int i=0;i<4;i++){
            int v = currentPiece.getActualYLocation(i);
            int h = currentPiece.getActualXLocation(i);
            well[v][h] = mainColor;
        }
    }

    @Override
    public void animationInitialize(int x, int y) {
    }

    @Override
    public void animationUpdate() {
        //need to check UP DOWN LEFT RIGHT and NOT CREATE BLOCK if COLOR == COLOR, current piece as reference.
            //check X L R for colors

        if(countFrames <= frameDelay) {
            if (countFrames % (maxFrames + 1) >= maxFrames) {
                findMinMax();
                for (int i = minY; i <= maxY; i++)
                    for (int j = minX; j <= maxX; j++) {
                        if (well[i][j] == mainColor) {
                            if (j + 1 <= GameData.X - 2)
                                if (well[i][j + 1] == Color.BLACK)
                                    well[i][j + 1] = mainColor;
                            if (j - 1 >= 0)
                                if (well[i][j - 1] == Color.BLACK)
                                    well[i][j - 1] = mainColor;
                            if (i + 1 <= GameData.Y - 2)
                                if (well[i + 1][j] == Color.BLACK)
                                    well[i + 1][j] = mainColor;
                            if (i - 1 >= 0)
                                if (well[i - 1][j] == Color.BLACK)
                                    well[i - 1][j] = mainColor;
                        }
                    }
            }
        }

        if (countFrames >= frameDelay/2) {
            if (countFrames % maxFrames + 1 >= maxFrames) {
                if (!delete)
                {
                    for (int i = 0; i < 4; i++) {
                        int h = delPiece.getActualXLocation(i);
                        int v = delPiece.getActualYLocation(i);
                        well[v][h] = Color.WHITE; }
                    delete = true;
                }
                if (delete) {
                    findMinMaxDelete();
                    for (int i = originalMinY; i <= originalMaxY; i++)
                        for (int j = originalMinX; j <= originalMaxX; j++) {
                            if (well[i][j] == Color.WHITE) {
                                if (j + 1 <= GameData.X - 2)
                                    if (well[i][j + 1] == mainColor)
                                        well[i][j + 1] = Color.WHITE;
                                if (j - 1 != -1)
                                    if (well[i][j - 1] == mainColor)
                                        well[i][j - 1] = Color.WHITE;
                                if (i + 1 <= GameData.Y - 2)
                                    if (well[i + 1][j] == mainColor)
                                        well[i + 1][j] = Color.WHITE;
                                if (i - 1 != -1)
                                    if (well[i - 1][j] == mainColor)
                                        well[i - 1][j] = Color.WHITE;
                            }
                        }
                }
            }
        }
        countFrames++;
    }

    private void findMinMax(){
        //ensuring that values never get out of array bounds or into walls.
        minX = (minX<=1) ? 1 : minX-1;
        minY = (minY<=1) ? 1 : minY-1;
        maxX = (maxX >= GameData.X-3) ? GameData.X-3 : maxX+1;
        maxY = (maxY >= GameData.Y-3) ? GameData.Y-3 : maxY+1;
    }
    private void findMinMaxDelete() {
        originalMinX = (originalMinX<=1) ? 1 : originalMinX-1;
        originalMinY = (originalMinY<=1) ? 1 : originalMinY-1;
        originalMaxX = (originalMaxX>= GameData.X-2) ? GameData.X-2 : originalMaxX+1;
        originalMaxY = (originalMaxY >= GameData.Y-3) ? GameData.Y-3 : originalMaxY+1;
    }

    @Override
    public boolean animationTerminate() {
        return (countFrames >= frameDelay*1.7);
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
        System.out.println("Sequence: OutlineExpansion");
    }
}
