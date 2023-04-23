package assets;

import swingAnimator.SwingAnimatorAdapter;
import assets.tetrisEvents.*;
import manager.EventManager;
import manager.Manager;

import java.util.ArrayList;

public class GameData {
            //size of each block
    final public static int BLOCK_RATIO = 32;
    final public static int X = 12, Y = 22;
    //About ticks-per-second and ticks-logic -- math is TPS divided by TL, resulting in amount of game-loops to wait on. 60 fps means 60/20 = 3 "frames" to wait on updating the logic, and just rendering the result.
    //i.e: 19/20 = 0.95 = int 0. No draw at all. The overall method will need to be updated to compensate for this, it uses TPS as a reference.
    //by using something like DRAW-TICKS and LOGIC-TICKS. Those can be manually set or automatically computed (like in modern engines, using delta-time).
    final public static int ticksPerSecond = (60);
    final public static int originalTicksLogic = (20);
    public static int linesToDrop = 0;

    public static Block[][] well;
    //<---THIS CANNOT BE STATIC! THIS MUUSTTTT BE INSTANCED! HO-LY SH-IT!
    /*
        Let's see. GamePanel needs to acquire an array, an instanced array, from LineClearedSequence.
        So, every time a NEW LineClearedSequence is declared, it should immediately be used through...oh.
     */

    public static ArrayList<GameOverListener> gameOverDispatcher = new ArrayList<>();
    public static ArrayList<LineClearedListener> lineClearedDispatcher = new ArrayList<>();
    public static ArrayList<NewPieceListener> newPieceDispatcher = new ArrayList<>();
    public static ArrayList<PieceLandedListener> pieceLandedDispatcher = new ArrayList<>();
    public static ArrayList<PieceMovedListener> pieceMovedDispatcher = new ArrayList<>();
    public static ArrayList<MenuEventListener> menuChangedDispatcher = new ArrayList<>();

    public static Manager gameManager;
    public static EventManager eventManager;
    public static SwingAnimatorAdapter animationDispatcher;
    public static javax.swing.JFrame FRAME;
    }
