package assets.panels;
import assets.Piece;

import java.awt.*;

/**
 * [P]
 * [X]
 * [X]
 * where "p" is preview, X is fodder. Piece-preview goes here.
 */

public class PanelPiecePreview extends GamePiecePanel {
    Piece previewPiece;



    public PanelPiecePreview(Piece piece){
        previewPiece = piece;

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(previewRatio, previewRatio);
    }
    @Override
    public Dimension getMaximumSize() {return getPreferredSize(); }
    @Override
    public Dimension getMinimumSize() {return getPreferredSize(); }


    /**
     * Should receive a Piece object, and draw it.
     */

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.GRAY);
        drawPieceCentered(g, previewPiece);
    }


}

