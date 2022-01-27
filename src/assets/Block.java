package assets;

import java.awt.*;

public class Block {
    public Point
            templatePoint;
    public Color color; private Color hideColor;
    public boolean isHidden = false;

    public Block(){
        //temp constructor
        templatePoint = new Point(0,0);
    }

    public Block(int x, int y){
        templatePoint = new Point(x, y);
        color = Color.BLACK;
    }

    public Block(int x, int y, Color color){
        this(x,y);
        this.color = color;
    }


    /**collision checking here. "otherxy" is PROJECTED.*/
    //->

    public boolean isEmpty(){
        return (color == Color.BLACK);
    }

    public boolean isOutOfBounds(){
        return (templatePoint.x < 0 || templatePoint.x >= GameData.X || templatePoint.y >= GameData.Y);
    }

    public void replaceBlock(Block B){
        replaceXY(B);
        color = B.color;
    }

    private void replaceXY(Block B) {
        templatePoint.setLocation(B.templatePoint.x, B.templatePoint.y);
    }

    public void toggleHide(){
        if (hideColor == null) {
            hideColor = color;
            color = Color.BLACK;
        } else {
            color = hideColor;
            hideColor = null;
        }
    }
}
