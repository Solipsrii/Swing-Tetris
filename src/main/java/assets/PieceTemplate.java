package assets;

/**
 * Holds an array of Blocks, that represent a Piece.
 * stores methods like "get random piece", and returns an array of points that represent a shape, and its rotations.
 */

import java.awt.Point;
import java.awt.Color;

public class PieceTemplate {

     private int index = 0;

     public static PieceTemplate L =             new PieceTemplate(0);

     public static PieceTemplate REVERESE_L =    new PieceTemplate(1);

     public static PieceTemplate TRIANGLE =      new PieceTemplate(2);

     public static PieceTemplate ZIGZAG =        new PieceTemplate(3);

     public static PieceTemplate LINE  =         new PieceTemplate(4);

     public static PieceTemplate SQUARE =        new PieceTemplate(5);


    public PieceTemplate(int index){
        this.index = index;
    }
    //1. shape index, 2.rotation of piece, 3.pieces
    private static Point[][] template = new Point[][] {
        // L
        {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2)},
        //Reverse L
        {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2)},
        //triangle thing
        {new Point(1, 0), new Point(1, 1), new Point(0, 1), new Point(2, 1)},
        //Z
        {new Point(1, 0), new Point(1, 1), new Point(0, 1), new Point(0, 2)},
        // |
        {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3)},
        //[]
        {new Point(1, 0), new Point(1, 1), new Point(0, 0), new Point(0, 1)}

    };
    public static Color[] colors = new Color[] {
            Color.RED, Color.ORANGE, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA
        };
        //end of template initialization
        //end of color template initialization

   public  Color getColor(int index){
        return colors[index];
   }

   public Point[] getShapeTemplate(){
       return template[index];
   }
   public Point getPoint(int index){
       return template[this.index][index];
   }

    public static int getNumOfShapes(){
        return template.length;
    }
    public static int getNumOfColors(){
       return colors.length;
    }
    public static Point[] getShapeTemplate(int index){ return template[index];}
}