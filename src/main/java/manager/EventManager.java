package manager;

import assets.GameData;
import assets.tetrisEvents.*;

import java.util.ArrayList;


public class EventManager {
    private ArrayList<GameOverListener>      gameOverDispatcher =        GameData.gameOverDispatcher;
    private ArrayList<LineClearedListener>   lineClearedDispatcher =     GameData.lineClearedDispatcher;
    private ArrayList<NewPieceListener>      newPieceDispatcher =        GameData.newPieceDispatcher;
    private ArrayList<PieceLandedListener>   pieceLandedDispatcher =     GameData.pieceLandedDispatcher;
    private ArrayList<PieceMovedListener>    pieceMovedDispatcher =      GameData.pieceMovedDispatcher;
    private ArrayList<MenuEventListener>     menuEventDispatcher =       GameData.menuChangedDispatcher;


    public void eventStart(EventType e){
        switch(e){
            case GAME_OVER:
                for (TetrisEventListener listeners: gameOverDispatcher)
                    listeners.onEventStart();
                break;

            case NEW_PIECE:
                for (TetrisEventListener listeners: newPieceDispatcher)
                    listeners.onEventStart();

                break;

            case PIECE_MOVED:
                for (TetrisEventListener listeners: pieceMovedDispatcher)
                    listeners.onEventStart();
                break;

            case LINE_CLEARED:
                for (TetrisEventListener listeners: lineClearedDispatcher)
                    listeners.onEventStart();
                break;

            case PIECE_LANDED:
                for (TetrisEventListener listeners: pieceLandedDispatcher)
                    listeners.onEventStart();
                break;
            default:
        }
    }

    public void eventEnd(EventType e){
        switch(e){
            case GAME_OVER:
                for (GameOverListener listeners: gameOverDispatcher)
                    listeners.onEventEnd();
                break;


            case LINE_CLEARED:
                for (LineClearedListener listeners: lineClearedDispatcher)
                    listeners.onEventEnd();
                break;
            default:
        }
    }



    public void eventStart(MenuType MT){
        switch(MT){
            case GAME_START:
                for(MenuEventListener listeners : menuEventDispatcher)
                    listeners.onGameStart();
                break;

            case GAME_PAUSED:
                for(MenuEventListener listeners : menuEventDispatcher)
                    listeners.onGamePause();
                break;

            case GAME_RESUMED:
            for(MenuEventListener listeners : menuEventDispatcher)
                listeners.onGameResume();
            break;
        }
    }

    //game-objects
    public void addTetrisListener(GameOverListener listener){
        gameOverDispatcher.add(listener);
    }
    public void removeTetrisListener(GameOverListener listener){
        gameOverDispatcher.remove(listener);
    }

    public void addTetrisListener(LineClearedListener listener){
        lineClearedDispatcher.add(listener);
    }
    public void removeTetrisListener(LineClearedListener listener){
        lineClearedDispatcher.remove(listener);
    }

    public void addTetrisListener(NewPieceListener listener){
        newPieceDispatcher.add(listener);
    }
    public void removeTetrisListener(NewPieceListener listener){
        newPieceDispatcher.remove(listener);
    }

    public void addTetrisListener(PieceLandedListener listener){
        pieceLandedDispatcher.add(listener);
    }
    public void removeTetrisListener(PieceLandedListener listener){
        pieceLandedDispatcher.remove(listener);
    }

    public void addTetrisListener(PieceMovedListener listener){
        pieceMovedDispatcher.add(listener);
    }
    public void removeTetrisListener(PieceMovedListener listener){
        pieceMovedDispatcher.remove(listener);
    }

    //menus
    public void addMenuListener(MenuEventListener listener){
        menuEventDispatcher.add(listener);
    }
    public void removeMenuListener(MenuEventListener listener){
        menuEventDispatcher.remove(listener);
    }


}
