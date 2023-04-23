package assets.tetrisEvents;

public abstract class GameOverListener extends TetrisEventListener {
    public GameOverListener() {
        super();
    }

    public abstract void onEventEnd();

    @Override
    protected TetrisEventListener getType(){
        return this;
    }
}
