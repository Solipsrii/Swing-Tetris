package assets.tetrisEvents;

public abstract class NewPieceListener extends TetrisEventListener {
    public NewPieceListener(){
        super();
    }
    @Override
    protected TetrisEventListener getType(){
        return this;
    }
}
