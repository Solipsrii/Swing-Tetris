package assets.tetrisEvents;

import assets.GameData;

public abstract class MenuEventListener {
   // autosubscribe to event manager
    public MenuEventListener(){
        GameData.eventManager.addMenuListener(this);
    }
    public abstract void onGamePause();
    public abstract void onGameResume();
    public abstract void onGameStart();

}
