package swingAnimator.sequences;

import assets.GameData;
import assets.Piece;
import swingAnimator.AnimLayer;
import swingAnimator.SwingAnimatorListener;

import java.awt.*;

public class OutlineExpansionManySequence extends SwingAnimatorListener {
int framesPassed, framesMax, index = 0;
Color[] colorArray;
Piece currentPiece;

    public OutlineExpansionManySequence(Color[] colorArray, Piece currentPiece){
        this.colorArray = colorArray;
        this.currentPiece = new Piece();
        this.currentPiece.updateShape(currentPiece, 0,0);
        framesMax = (GameData.ticksPerSecond / GameData.originalTicksLogic)*6;
        framesPassed = framesMax;
    }
    @Override
    public void animationInitialize(int x, int y) {}

    @Override
    public void animationUpdate() {
        if (framesPassed++%(framesMax+1) >= framesMax){
            new OutlineExpansionSequence(colorArray[index++], currentPiece);
        }
    }

    @Override
    public boolean animationTerminate() {
        return (index >= colorArray.length);
    }

    @Override
    public boolean filterByLayer(AnimLayer layer) {
        return AnimLayer.BACKGROUND == layer;
    }

    @Override
    public void draw(Graphics G) {
        System.out.println("!!!!!TEST!!");
    }
}
