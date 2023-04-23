package swingAnimator.sequences;

import assets.GameData;
import swingAnimator.AnimBehavior;
import swingAnimator.AnimLayer;
import swingAnimator.AnimObject;
import swingAnimator.SwingAnimatorListener;

import java.awt.*;
import java.util.ArrayList;

public class threeXSequence extends SwingAnimatorListener {
    private ArrayList<AnimObject> list;
    final int MAX_X = GameData.X-2,
              MAX_Y = GameData.Y-1;
    DIRECTION direction;


    public threeXSequence(DIRECTION direction){
        list = new ArrayList<>();
        this.direction = direction;
        animationInitialize(0, 0);
    }

    @Override
    public void animationInitialize(int x, int y) {
        int counter = 0;
        Color color;

        AnimBehavior behavior = null;
        if (direction == DIRECTION.UP)
            behavior = AnimBehavior.SCROLL_UP;

        else if (direction == DIRECTION.DOWN)
            behavior = AnimBehavior.SCROLL_DOWN;

        else if (direction == DIRECTION.SIDEWAYS)
            behavior = AnimBehavior.SCROLL_SIZEWAYS;

        for (int j=1; j < GameData.X - 1; j++)
            for(int i=MAX_Y-1; i >= 0 ; i--) {
                    color = Color.RED;
                    //initialize the entire line of blocks into the list
                    list.add(new AnimObject(j, i, color, true, behavior));
                    list.get(counter++).initialUpdate(5);
            }
    }

    @Override
    public void animationUpdate() {
        for (AnimObject AO : list) {
            //recycle top row to become the bottom row.
            switch (direction) {
                case DOWN:
                    if (AO.y >= MAX_Y)
                        AO.y = -1;
                    else
                        AO.update();
                    break;


                case UP:
                  //  if (AO.y <= -26)
                    //    AO.y = MAX_Y * GameData.BLOCK_RATIO;
                    //else
                        AO.update();
                    break;

                case SIDEWAYS:
                   /*
                    if (AO.x >= (MAX_X)*GameData.BLOCK_RATIO || AO.y <= -1) {
                        AO.x = AO.initialX;
                        AO.y = AO.initialY;
                    }
                    else

                    */
                        AO.update();
                    break;
            }
        }
    }

    @Override
    public boolean animationTerminate() {
        //TODO:: combo sequence needs to end after a counter ticks, like, 10 seconds without any score landing.
        return false;
    }

    @Override
    public boolean filterByLayer(AnimLayer layer) {
        return (layer == AnimLayer.BACKGROUND);
    }

    @Override
    public void draw(Graphics G) {
        final int GRID_SIZE = GameData.BLOCK_RATIO;

        for (AnimObject AO : list) {
            G.setColor(AO.color);
            G.fillRect(AO.x, AO.y, GRID_SIZE-2, GRID_SIZE-2);
        }
    }

   public enum DIRECTION {
        UP,
        DOWN,
        SIDEWAYS;
    }
}
