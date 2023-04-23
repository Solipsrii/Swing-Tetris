package assets.tetrisEvents;

public abstract class PieceLandedListener extends TetrisEventListener {
    public PieceLandedListener() {
        super();
    }

    @Override
    protected TetrisEventListener getType(){
        return this;
    }
}
