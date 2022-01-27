package assets.panels;

import assets.Block;
import assets.GameData;
import assets.Piece;

import javax.swing.*;
import java.awt.*;

public class GamePiecePanel extends JPanel {
    protected int previewRatio = (int)(GameData.BLOCK_RATIO*5);

    //DRAWABLE METHODS
    public void drawPiece(Graphics g, Piece piece) {

        for(int i=0;i<4;i++){
            g.setColor(Color.GRAY);
            g.drawRect(piece.getDrawingXLocation(i), piece.getDrawingYLocation(i), GameData.BLOCK_RATIO, GameData.BLOCK_RATIO);
            g.setColor(piece.shape[0].color);
            g.fillRect(piece.getDrawingXLocation(i), piece.getDrawingYLocation(i), GameData.BLOCK_RATIO, GameData.BLOCK_RATIO);
        }
    }

    public void drawBackground(Graphics g, Block[][] well) {
        final int GRID_SIZE = GameData.BLOCK_RATIO;
        for (int x = 0; x < GameData.X; x++) {
            for (int y = 0; y < GameData.Y; y++) {
                g.setColor(Color.GRAY);
                g.drawRect(x * GRID_SIZE, GRID_SIZE * y, GRID_SIZE, GRID_SIZE);
               g.setColor(well[y][x].color);
               g.fillRect(x * GRID_SIZE, GRID_SIZE * y, GRID_SIZE-1 , GRID_SIZE-1);
            }
        }
    }

    public void drawPieceCentered(Graphics g, Piece piece) {
        final int GRID_SIZE = GameData.BLOCK_RATIO;

        g.setColor(piece.shape[0].color);
        int offsetX = ((4 - piece.getMaxX())*GRID_SIZE)/2; //get drawing-relevant coordinates
        int offsetY = ((4 - piece.getMaxY())*GRID_SIZE)/2; //max of 4 grid-size blocks - 0xy, and /2 to get "center" coordinates (i.e: 4/2).
            if ((offsetY == GRID_SIZE/2))
                offsetX -= 10; //a special setting for the LINE piece.

        for(int i=0;i<4;i++){
            g.drawRect(piece.getDrawingXLocation(i) + offsetX, piece.getDrawingYLocation(i) + offsetY, GRID_SIZE, GRID_SIZE);
            g.fillRect(piece.getDrawingXLocation(i) + offsetX, piece.getDrawingYLocation(i) + offsetY, GRID_SIZE - 1, GRID_SIZE - 1);
        }
    }
}
