package assets.tetrisEvents;

public abstract class LineClearedListener extends TetrisEventListener {

    public abstract void onEventEnd();

    @Override
    protected TetrisEventListener getType(){
        return this;
    }

}
