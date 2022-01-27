package assets.tetrisEvents;
import assets.GameData;

public abstract class TetrisEventListener {

    //auto subscribe to the eventManager! A bit unintuitive, but on each creation of a listener, it is auto added to the event manager's appropriate list.
    public TetrisEventListener(){
        TetrisEventListener type = getType();
        if (type instanceof GameOverListener)
             GameData.eventManager.addTetrisListener((GameOverListener)getType());
        else if (type instanceof LineClearedListener)
            GameData.eventManager.addTetrisListener((LineClearedListener)getType());
        else if (type instanceof NewPieceListener)
            GameData.eventManager.addTetrisListener((NewPieceListener)getType());
        else if (type instanceof PieceLandedListener)
            GameData.eventManager.addTetrisListener((PieceLandedListener)getType());
        else if (type instanceof PieceMovedListener)
            GameData.eventManager.addTetrisListener((PieceMovedListener)getType());

    }
    protected abstract TetrisEventListener getType();

    public abstract void onEventStart();
}
