package swingAnimator.sequences;

import swingAnimator.AnimBehavior;
import swingAnimator.AnimLayer;
import swingAnimator.AnimObject;
import swingAnimator.SwingAnimatorListener;
import assets.GameData;

import java.awt.*;
import java.util.ArrayList;

public class LineClearedSequence extends SwingAnimatorListener {
    //variables here
    private ArrayList<AnimObject> list;
    private ArrayList<AnimObject> removalList;
    private AnimLayer layer;

    public LineClearedSequence(int y){
        this.layer = AnimLayer.FRONT;
        animationInitialize(0, y);
    }

    @Override
    public void animationInitialize(int x, int y) {
        java.util.Random rand = new java.util.Random();
        list = new ArrayList<>();
        removalList = new ArrayList<>();

        for (int i=1; i < GameData.X - 2; i++) {
            //initialize the entire line of blocks into the list
            list.add(new AnimObject(i, y, GameData.well[y][i].color, true, AnimBehavior.RANDOM_DIRECTION));
            list.get(i-1).initialUpdate(rand.nextInt(GameData.BLOCK_RATIO/2)+(GameData.BLOCK_RATIO/5));
        }
    }

    @Override
    public void animationUpdate() {
            //in the explosion animation, all I care about is whether the pieces get out of the screen.
            for(AnimObject AO : list)
            {
                //use array max XY values and expand them based on display size. Check if in-bounds.
                if(!(AO.x < 0 || AO.x > (GameData.X-1) * GameData.BLOCK_RATIO || AO.y > (GameData.Y) * GameData.BLOCK_RATIO))
                    AO.update();
                else
                    removalList.add(AO);
            }
            list.removeAll(removalList);
    }

    @Override
    public boolean animationTerminate() {
        return(list.isEmpty());
    }


    @Override
    public boolean filterByLayer(AnimLayer layer) {
        return (layer == this.layer);
    }


    //access this method through Panel!
    @Override
    public void draw(Graphics G) {
        final int GRID_SIZE = GameData.BLOCK_RATIO;

        for (AnimObject AO : list){
            G.setColor(AO.color);
            G.drawRect(AO.x, AO.y, GRID_SIZE, GRID_SIZE);
            G.fillRect(AO.x, AO.y, GRID_SIZE-1, GRID_SIZE-1);
        }
        System.out.println("Sequence: LineCleared");
    }
}

