package swingAnimator;
/*
     TO DO: Create new animObject  [V]
                   Create xVelocity, yVelocity that'll store each animObject speed, and thus, XY location. [v]
                   Create proper initialization method, that takes the physAnimation into heart. That is,
                   end-location influences the overall speed.

                   Find a way to define speed and acceleration.
                   I am using BLOCK_RATIO to essentially create a grid.
                   And so, I have a total of (BLOCK_RATIO * MAX_X or * MAX_Y) worth of space.
                   Or as of right now: 234x & 572y
                   I don't think I have a "realistic" way to go about speed. Just one that seems appropriate.

                   Need to decide on: TERMINAL_SPEED val.

 */


import assets.GameData;

import java.awt.*;
import java.util.Random;

public class AnimObject {
    public double xV = 0, yV = 0;
    public int x, y, initialX, initialY, direction;
    public int finalY;
    public Color color;
    final private int
            BLOCK_RATIO = GameData.BLOCK_RATIO;
    private int framesLap = (int)(GameData.ticksPerSecond / GameData.originalTicksLogic);
    private final AnimBehavior behavior;
    final private double
            drag = BLOCK_RATIO / 50,
            TERMINAL_SPEED = BLOCK_RATIO, //or, 17.5.
            GRAVITY = 0.5,
            divideBy = (double)(GameData.ticksPerSecond/GameData.originalTicksLogic);
    public boolean noclip = false;




    //going by My tetris game, there is currently only 10X 22Y possible locations to move in, out of a total 12X 23Y.
    private AnimObject(Color color, boolean noclipFlag, AnimBehavior behavior) {
        noclip = noclipFlag;
        this.color = color;
        this.behavior = behavior;
    }

    public AnimObject(int xStart, int yStart, Color color, boolean noclipFlag, AnimBehavior behavior) //for possible future blocks that'd "rest on" other blocks?
    {
        this(color, noclipFlag, behavior);
        this.x = (xStart * BLOCK_RATIO);
        this.y = (yStart * BLOCK_RATIO); //starting locations ON DISPLAY, not array index!
        this.initialX = x;
        this.initialY = y;
    }
    public AnimObject(int xStart, int yStart, int yEnd, Color color, boolean noclipFlag, AnimBehavior behavior) //for possible future blocks that'd "rest on" other blocks?
    {
        this(color, noclipFlag, behavior);
        this.x = (xStart * BLOCK_RATIO);
        this.y = (yStart * BLOCK_RATIO); //starting locations ON DISPLAY, not array index!
        this.initialX = x;
        this.initialY = y;
        finalY = yEnd * BLOCK_RATIO;
    }


    public void update() {
        //well, to "update" a single block, I need to make it move. Therefore, I need to decide how an object moves.
        //let's just assume that the object is "dropped". How is it dragged down?
        //nvm, the important bit is -- how do I judge if it touched anything? I guess I can use the Well array...

            //intervals of frame skipping b4 the velocity itself updates.
            //"divideby" is the current FPS amount (say, 60) divided by base frame logic (20 FPS). So, 3 frames to skip.
            //That way, animation is relatively kept consistent, and it's a lazy solution.
           if(framesLap++ >= divideBy) {
               if (behavior == AnimBehavior.RANDOM_DIRECTION || behavior == AnimBehavior.SCROLL_SIZEWAYS)
                   xV = calculateXV(1);
               if (behavior == AnimBehavior.RANDOM_DIRECTION || behavior == AnimBehavior.DROP)
                    yV = calculateYV(1);
               framesLap = 0;
        }
           //update X & Y locations based on "speed" per frame.
            x += (int) xV;
            y += (int) yV;
    }

    public void initialUpdate(int initialSpeed){
        if(behavior == AnimBehavior.RANDOM_DIRECTION) {
            direction = (new Random().nextBoolean() ? 1 : -1); //Direction is either left (-1) or right (+1).
            xV += (initialSpeed) * direction;
            yV += initialSpeed * -1;
            xV = calculateXV(divideBy);
            yV = calculateYV(divideBy);
        }
        else if(behavior == AnimBehavior.SCROLL_UP)
             yV += initialSpeed * -1;
        else if(behavior == AnimBehavior.SCROLL_DOWN)
             yV += initialSpeed;
        if (behavior == AnimBehavior.SCROLL_SIZEWAYS) {
            direction = (1); //Direction is either left (-1) or right (+1).
            yV += initialSpeed * -1;
            xV += (initialSpeed) * direction;
        }


    }

    public double calculateXV(double divideBy) {
        double tmp = 0;
            switch (direction) {
                case (-1):
                    tmp = (xV + drag)/divideBy;
                   return Math.min(tmp, 0);
                case (1):
                    tmp = (xV - drag)/divideBy;
                    return Math.max(tmp, 0);
        }

        return tmp;
    }
        public double calculateYV(double divideBy)
        {
                double tmp = (yV + GRAVITY) / divideBy;
                return (Math.min(tmp, TERMINAL_SPEED));
        }

        /*
            public boolean collidesInRange(int otherX, int otherY) {
    if (isEmpty()) return false; //if THIS block cannot possibly be hit.
        int gridSize = GameData.BLOCK_RATIO; //size of each "square".
        int leftFace = blockPointRange.x, rightFace = leftFace+gridSize;
        int topFace = blockPointRange.y, bottomFace = topFace+gridSize;
        int otherLeftFace = otherX; int otherRightFace = otherX+gridSize;
        int otherTopFace  = otherY; int otherBottomFace= otherY+gridSize;

        if (leftFace <= otherLeftFace || rightFace >= otherRightFace)
            if (topFace <= otherTopFace || bottomFace >= otherBottomFace)
                return true; //if either "face" touches the other, it intersects, baby.
        return false;
    }
         */
}
