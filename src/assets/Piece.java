package assets;


import assets.gameObjects.Game2DObject;
import assets.tetrisEvents.EventType;
import manager.Manager;

import java.awt.*;

/**
 * This class holds the individual point-template of the chosen tetramino piece.
 * It stores piece-data, such as the shape template, rotation value, color, etc.
 * This class only stores data of the piece, and does not handle logic and calculations for the piece.
 */


public class Piece extends Game2DObject{
    public int shapeIndex;
    public  Block[] shape;
    public Point currentLocation = new Point(0,0);
    private java.util.Random rand = new java.util.Random();
    private Move movement = Move.DOWN;
    public boolean hasMoved = false;

public Piece(){
    shape = new Block[4];
    for(int i=0;i<4;i++)
        shape[i] = new Block(0,0);
}

    public Piece(Manager parent) {
        super(parent);
        generateNewPiece();
    }

    public Piece (Manager parent, int x, int y){
        super(parent);
        generateNewPiece(x,y);
    }

    public Piece(Piece clonePiece){
        shape = new Block[4];
        for (int i=0; i<4;i++) {
            int x = clonePiece.shape[i].templatePoint.x;
            int y = clonePiece.shape[i].templatePoint.y;
            shape[i] = new Block(x,y, clonePiece.shape[0].color);
        }
        currentLocation = clonePiece.currentLocation;
        hasMoved = clonePiece.hasMoved;
    }
    //end of constructors


    public void generateNewPiece(){
        generateNewPiece(0,0);
    }
    public void generateNewPiece(int startingLocationX, int startingLocationY){
        generateNewPiece(startingLocationX,startingLocationY, rand.nextInt(PieceTemplate.getNumOfShapes()));
    }
    public void generateNewPiece(int startingLocationX, int startingLocationY, int index){
        shapeIndex = index;
        shape = new Block[4];
        PieceTemplate template = new PieceTemplate(index);
        int colorIndex = rand.nextInt(PieceTemplate.getNumOfColors());

        for (int point = 0; point < 4; point++) {
            int x = template.getPoint(point).x;
            int y = template.getPoint(point).y;
            shape[point] = new Block(x, y, template.getColor(colorIndex)); //initialize shape
        }
        currentLocation.setLocation(startingLocationX, startingLocationY);
    }


    public void rotate(Move i){
        Block[] tmpContainer = new Block[4];
        int maxX = Math.max(shape[3].templatePoint.x, shape[0].templatePoint.x);
        maxX     = Math.max(maxX,                     shape[2].templatePoint.x);
        int maxY = Math.max(shape[3].templatePoint.y, shape[0].templatePoint.y);
        maxY     = Math.max(maxY,                     shape[2].templatePoint.y);

        final int maxLength = Math.max(maxX,maxY);

        for (int h = 0; h <= maxLength; h++) //horizontal
            for (int v = 0; v <= maxLength; v++) //vertical
                for (int chk = 0; chk < 4; chk++) {
                    if (i == Move.ROTATE_CW && shape[chk].templatePoint.x == h && shape[chk].templatePoint.y == v) { //place current location within 'adapted location'.
                        tmpContainer[chk] = new Block(maxLength - v, h, shape[0].color); //reverse 'em!
                        break;
                    }

                    else if(i == Move.ROTATE_CCW && shape[chk].templatePoint.x == maxLength - v && shape[chk].templatePoint.y == h) { //place 'adapted location' within current XY.
                        tmpContainer[chk] = new Block(h, v, shape[0].color); //reverse 'em!
                        break;
                    }
                }

        updateBlocks(tmpContainer);
    }


    public int getActualXLocation(int templateIndex){
        return (currentLocation.x + shape[templateIndex].templatePoint.x);
    }

    public int getActualYLocation(int templateIndex){
        return (currentLocation.y + shape[templateIndex].templatePoint.y);
    }

    public int getDrawingXLocation(int templateIndex){
        return ((currentLocation.x + shape[templateIndex].templatePoint.x) * GameData.BLOCK_RATIO);
    }

    public int getDrawingYLocation(int templateIndex){
        return ((currentLocation.y + shape[templateIndex].templatePoint.y) * GameData.BLOCK_RATIO);
    }


    public void updateBlocks(Block[] tmpContainer) {
        for (int i = 0; i < 4; i++) {
            if (shape[i] == null) {
                shape[i] = new Block(tmpContainer[i].templatePoint.x, tmpContainer[i].templatePoint.y, tmpContainer[i].color);
            }
            else
                shape[i].replaceBlock(tmpContainer[i]);
        }
    }



    public void updateShape(Piece piece, int modX, int modY){
        for(int i=0;i<4;i++) {
                shape[i].replaceBlock(piece.shape[i]);
            }
        if(modX != 0  || modY != 0)
            currentLocation.setLocation(modX,modY);
        else
            currentLocation.setLocation(piece.currentLocation.x, piece.currentLocation.y);
        }

    public void modXY(int modX, int modY){
        currentLocation.setLocation(currentLocation.x + modX, currentLocation.y + modY);
    }



    //validation methods
    public boolean isOutOfBounds() {
        for (Block B : shape) {
            int x = B.templatePoint.x + currentLocation.x, y = B.templatePoint.y + currentLocation.y;
            if (x < 0 || y < 0 || x > GameData.X - 1 || y > GameData.Y - 1)
                return true;
        }
        return false;
    }

    public int getMaxY(){
    int maxY = 0;
     for(Block B : shape){
         if (B.templatePoint.y > maxY)
             maxY = B.templatePoint.y;
     }
     return maxY;
    }

    public int getMaxX(){
    int maxX = 0;
        for(Block B : shape){
            if (B.templatePoint.x > maxX)
                maxX = B.templatePoint.x;
        }
        return maxX;
    }




    /******************************************

     G A M E   O B J E C T  METHODS HERE

     *****************************************/


    @Override
    public Block[] getBlocks() {
        return shape;
    }

    @Override
    public Piece getShape() {
        return this;
    }

    @Override
    public Move getMovement() {
        return movement;
    }

    @Override
    public void setMovement(Move M) {
        if (!hasMoved) {
            movement = M;
            hasMoved = true;
        }
    }

    @Override
    public void update() {
        parent.update(this); //potentially update XY coordinates of all blocks in array
        hasMoved = false;          //free movement flag
        movement = Move.DOWN;      //default to move down, for autoDropper convenience.
    }

    @Override
    public boolean canUpdate() {
        if(!isOutOfBounds())
            if (!parent.canCollide(this, movement)) {
                GameData.eventManager.eventStart(EventType.PIECE_MOVED);
                return true;
        }

        hasMoved = false;
        if (movement == Move.DOWN)
            GameData.eventManager.eventStart(EventType.PIECE_LANDED);
        else
            movement = Move.DOWN;
        return false;
    }



}



