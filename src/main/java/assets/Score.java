package assets;


import assets.gameObjects.GameINTObject;
import manager.Manager;

public class Score extends GameINTObject {
    public int value;
    public int linesCleared;

   public Score(){
       super();
       value = 0;
       linesCleared = 0;}



    @Override
    public boolean canUpdate() {
       return (linesCleared != 0);
    }

    @Override
    public void update() {
            switch (linesCleared) {
                //TODO: Add special events for particular SCORE. Like, 4 in a row should make: "TTETTRRISSSS" in PanelManager.
                case (1):
                    value += 100;break;
                case (2):
                    value += 300;break;
                case (3):
                    value += 800;break;
                default:
                    value += 1500;break;
            }
            linesCleared = 0;
    }
}
