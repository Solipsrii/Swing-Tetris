package swingAnimator.sequences;

import assets.GameData;
import swingAnimator.AnimBehavior;
import swingAnimator.AnimLayer;
import swingAnimator.AnimObject;
import swingAnimator.SwingAnimatorListener;

import java.awt.*;
import java.util.ArrayList;

//TODO: write ENTIRE BG DROP AFTER LINE DROP ANIMATION. Change updateDropPieces method in Manager to store the colors, and after the animation ends, apply them to the 'well'.
public class BlockDropSequence extends SwingAnimatorListener {

    private Color[][] colorArray;
    private ArrayList<AnimObject> list;
    private AnimLayer layer;
    private  int finalY;
    private final int originalFinalY;

    public BlockDropSequence(Color[][] colorArray, int finalY){
        this.colorArray = colorArray;
        this.originalFinalY = finalY;
        this.finalY = finalY+1;
        this.layer = AnimLayer.FRONT;
        animationInitialize(0, 0);
    }

    @Override
    public void animationInitialize(int x, int y) {
        list = new ArrayList<>();
        int i = 0;
        for(y = colorArray.length-1; y > 0; y--) {
            finalY -= 1;
            for (x = 1; x < colorArray[0].length; x++) {
                if(colorArray[y][x] != Color.BLACK) {
                    //starting from cell #1 instead of #0, so X val is -1 offset
                    list.add(new AnimObject(x-1, y, finalY, colorArray[y][x], false, AnimBehavior.DROP));
                    list.get(i++).initialUpdate(1);
                }
            }
        }
    }

    @Override
    public void animationUpdate() {
            for (AnimObject AO : list) {
                if(AO.y < AO.finalY)
                    AO.update();
            }
        }

    @Override
    public boolean animationTerminate() {
        //communicate to Manager here, that it can copy the colors back to the BG.
        if(!list.isEmpty()){
            if (list.get(0).y+5 >= list.get(0).finalY) {
                // GameData.eventManager.eventEnd(EventType.LINE_CLEARED);
                list.clear();
                GameData.gameManager.updateDropPieces(originalFinalY, originalFinalY - colorArray.length + 1);
                return true;
            }
        } else {
            GameData.gameManager.updateDropPieces(originalFinalY, originalFinalY - colorArray.length + 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean filterByLayer(AnimLayer layer) {
        return (layer == AnimLayer.FRONT);
    }

    @Override
    public void draw(Graphics g) {
        final int GRID_SIZE = GameData.BLOCK_RATIO;

        for (AnimObject AO : list) {
            g.setColor(AO.color);
            g.drawRect(AO.x+GRID_SIZE, AO.y, GRID_SIZE, GRID_SIZE);
            g.fillRect(AO.x+GRID_SIZE, AO.y, GRID_SIZE - 1, GRID_SIZE - 1);
        }
        System.out.println("Sequence: BlockDropSequence");
    }
}
