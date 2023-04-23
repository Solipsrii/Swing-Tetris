package assets;

import assets.gameObjects.GameObject;
import assets.tetrisEvents.EventType;

public class AutoDropper extends GameObject {
    private int timeLapsed = 1;
    private EventType eventType;

    public AutoDropper(manager.Manager parent){
        super(parent);
    }
    public void interrupt(){
        timeLapsed = 0;
    }




    @Override
    public void update() {
        timeLapsed += 1;
    }

    @Override
    public boolean canUpdate() {
        if (timeLapsed  >= GameData.ticksPerSecond+4) { //after 1.20 seconds.
            interrupt();
            parent.addGameObject(parent.currentPiece); //by default, piece's movement is always down.
            return false;
        }
        return true;
    }
}
