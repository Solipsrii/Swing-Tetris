package assets.panels;
import assets.GameData;
import assets.Score;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PanelFodder extends GamePiecePanel {
    /**
     * [p]
     * [x]
     * [x]
     * where "X" is the fodder space. P is preview. Score goes in here mayb?
     */
    Score score;
    JPanel scorePanel;
    final int
            MAX_Y = GameData.BLOCK_RATIO * GameData.Y,
            Max_X = GameData.BLOCK_RATIO * GameData.X,
            LOCALMAXY = MAX_Y - previewRatio;
             BufferedImage image;


    public PanelFodder(Score scr) {
        score = scr;
        scorePanel = new JPanel(){
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }

            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(previewRatio, LOCALMAXY / 2);
            }

            //PAINT SCORE HERE!
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); //clean score paint
                setBackground(Color.GRAY);
                g.setColor(Color.BLACK);
                g.drawString(""+scr.value, previewRatio/4, this.getPreferredSize().height - GameData.BLOCK_RATIO);
                if (image != null)
                    g.drawImage(image,0,0,this);

            }
        };

       add(scorePanel);
        setBackground(Color.GRAY);
        /*
        try {
            image = ImageIO.read(getClass().getResource("resources/images/x.png"));
        } catch(IOException e){ e.printStackTrace();}
    */
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(previewRatio, MAX_Y - previewRatio);
    }
}