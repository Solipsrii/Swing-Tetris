package assets.tetrisEvents;

public abstract class PieceMovedListener extends TetrisEventListener{
    public PieceMovedListener() {
        super();
    }

    public abstract void onEventEnd();

    @Override
    protected TetrisEventListener getType(){
        return this;
    }
}
