/**
 * NOTES:
 * TODO:: ADD ANIMATIONS!
 * TODO:: POSSIBLY CREATE A NEW LISTENER INTERFACE FOR SPECIFIC EVENTS, LIKE PIECE-LANDED-ANIMATOR!
 * TODO: create notes for the different animation sequences, to make sure I understand how they shouldbe implemented.
 *          line-break -- must be triggered on EventType.LineCleared. On init, structure a line of animobjects.
 *                        to make it "explode", on init, I tell each block what its initial speed is.
 *                        Then, on-update, it continues to calculate its speed, minus gravity. The speed calculation is
 *                        expecting to lose out due to drag, the key is initial speed.
 *                        So, initial speed, random direction which is set on construction (1 / -1),
 */


package manager; /**
 * Stores and manipulates objects, to be "sent" to the PanelManager.
 */

import swingAnimator.sequences.BlockDropSequence;
import swingAnimator.sequences.LineClearedSequence;
import assets.*;
import swingAnimator.*;
import swingAnimator.sequences.*;
import assets.gameObjects.*;
import assets.tetrisEvents.*;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.event.MenuEvent;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;

public class Manager {
    final int
            BLOCK_RATIO = GameData.BLOCK_RATIO,
            MAX_GAME_X = GameData.X,
            MAX_GAME_Y = GameData.Y;

    public Timer loopTimer = new Timer(1000 / GameData.ticksPerSecond, e ->
    {
        update();
        draw();
    });

    public SwingAnimator animationTimer;
    public Piece currentPiece, nextPiece;
    public Block[][] well;
    public boolean gameOverFlag, pauseMenuFlag = false;
    private java.util.Random rand;
    private ArrayList<GameObject> itemQueue;
    private ArrayList<GameObject> bufferQueue;
    public Score score;
    int xxx;

    private AutoDropper autoDropper;
    //panels
    private PanelManager panelManager;
    private JFrame frameParent;
    //dispatcher
    EventManager eventManager;
    SwingAnimatorAdapter animationDispatcher;

    //add special GameObject types here to be passed back.
    //static public GameINTObject CALLBACK_SCORE;

    //listeners
    SwingAnimatorListener animationLineCleared_Explode;


    private Manager() {

        //initialize

        GameData.gameManager = this;

        autoDropper = new AutoDropper(this);

        score = new Score();
        nextPiece = new Piece(this);
        currentPiece = new Piece(this); //creates a random piece
        animationTimer = new SwingAnimator(GameData.ticksPerSecond);
        itemQueue  = new ArrayList<>();
        bufferQueue= new ArrayList<>();

        rand = new java.util.Random();

        animationDispatcher = new SwingAnimatorAdapter();
        GameData.animationDispatcher = animationDispatcher;

        eventManager = new EventManager(); //tying the reference to the global event dispatcher.
        GameData.eventManager = eventManager;

        //listeners go here
        //line cleared listener old here

        /**
         * ON CLEAR
         */
        //listener is auto-added to the eventManager.
        LineClearedListener lineClearedListener = new LineClearedListener() {
            @Override
            public void onEventEnd() {

            }

            @Override
            public void onEventStart() {
                Color[] color = new Color[3];
                for(int i=0;i<color.length;i++)
                    color[i] = PieceTemplate.colors[rand.nextInt(PieceTemplate.getNumOfColors())];
                new OutlineExpansionManySequence(color, currentPiece);
            }
        };

        /**
         * ON LAND
         */
        //listener is auto-added to the eventManager. I.E: each time a piece is landed, this is triggered.
        PieceLandedListener pieceLandedListener = new PieceLandedListener() {

            @Override
            public void onEventStart() {
                updateFixToWell(currentPiece);
                generateNextPiece();

                //game-over logic
                if (canCollide(currentPiece, Move.DOWN)) {
                    eventManager.eventStart(EventType.GAME_OVER);
                    //eventManager.eventStart(MenuType.GAME_PAUSED);
                }
            }
        };

            /**
            * ON GAME OVER
            */
          GameOverListener gameOverListener = new GameOverListener() {
                           @Override
                           public void onEventEnd() {

                           }

                           @Override
                           public void onEventStart() {
                            bufferQueue.clear();
                            gameOverFlag = true;
                            //start menu here
                           }
                       };

        /**
         * Menu Listener (resume, pause).
         * NOTE: PanelManager also has such a listener.
         */
        //menu-switching behavior goes here
                MenuEventListener MenuListener = new MenuEventListener() {
                    @Override
                    public void onGamePause() {
                        loopTimer.stop();
                    }

                    @Override
                    public void onGameResume() {
                        loopTimer.start();
                    }

                    @Override
                    public void onGameStart() {
                        //has its own method
                    }
                };

        //end of listeners

        //BG array initialize
        well = new Block[MAX_GAME_Y][MAX_GAME_X];
        for (int y = 0; y < MAX_GAME_Y; y++) {
            for (int x = 0; x < MAX_GAME_X; x++) {
                if (x == 0 || y == MAX_GAME_Y - 1 || x == MAX_GAME_X - 1)
                    well[y][x] = new Block(x, y, Color.DARK_GRAY);
                else
                    well[y][x] = new Block(x, y); //empty block!
            }
        }


        GameData.well = well;
        //initialize end
    }

    public Manager(JFrame frameParent){
     this();
     this.frameParent = frameParent;
     this.frameParent.addKeyListener(keyListener);
     panelManager = new PanelManager();
    }

    public PanelManager getPanelManager(){return panelManager;}

    public void startGame(){
        generateNextPiece();
        draw();
        eventManager.eventStart(MenuType.GAME_START);
        loopTimer.start();
    }



    // ********************
    /// START OF MAIN LOOP
    // *******************


    public void update() {
        //disabling the keyboard from interrupting the main thread.
           frameParent.removeKeyListener(keyListener);
           if (!gameOverFlag){
            addGameObject(autoDropper); //auto-queue the autoDropper to check if it can auto-drop the piece 1 space.
            itemQueue.addAll(bufferQueue);
            bufferQueue.clear();
        }
        //
        //main loop here. Must ensure that nothing can be added to the itemQueue once update() is called.
        //

        while(itemQueue.size() != 0){
            if (itemQueue.get(0).canUpdate()) {
                itemQueue.get(0).update();
                itemQueue.remove(0);
            }
            else
                itemQueue.remove(0);
        }
        animationDispatcher.update();
        frameParent.addKeyListener(keyListener);
    }


    public void draw() {

        //repaint();
        panelManager.repaint();
    }


    // *****
    //  END OF MAIN LOOP
    // ******



    public void addGameObject(GameObject item){
        bufferQueue.add(item);
    }
    public void addGameObjects(ArrayList<GameObject> list){
        bufferQueue.addAll(list);
    }

    /**************************************************
     *      CLASS HELPER METHODS FROM HERE ON!
     **************************************************/

    //Create new piece on tetrinopooop shape change, i.e: when wall-fixing.
    private void generateNextPiece() {
    //currentPiece.generateNewPiece(5,0,2);
        currentPiece.updateShape(nextPiece, 5, 0); //updates XY and color, maintains same reference.
        nextPiece.generateNewPiece(0,0);
        //nextPiece.generateNewPiece();
        eventManager.eventStart(EventType.NEW_PIECE);
    }


    private void addMovement(Move M){
        if(!currentPiece.hasMoved) {
            frameParent.removeKeyListener(keyListener);
            currentPiece.setMovement(M);
            addGameObject(currentPiece);
        }
    }


    /*****************************
     * VALIDATION checking HERE
     *****************************/


    public boolean canCollide(Game2DObject G2D, Move M){
       int moveX = 0, moveY = 0;
        boolean returnValue = false;

            //decide which transformation values to validate
            switch(M){
                case ROTATE_CCW:
                case ROTATE_CW:
                 currentPiece.rotate(M);
                 break;

                case DOWN:
                 moveY = 1;
                 break;

                case LEFT :
                case RIGHT:
                 moveX = M.val; //choose between LEFT or RIGHT, the only options left.
                    break;
        }

            //process checking. If false, & DOWN was pressed, make sure PIECE is FIXED in the well.
        if (!currentPiece.isOutOfBounds()){
            for(int i=0;i<4;i++)
                {
                    int x = currentPiece.getActualXLocation(i) + moveX, y = currentPiece.getActualYLocation(i) + moveY;
                    //true if collides
                    if (!well[y][x].isEmpty()) //if block is not empty, then piece can only hit it.
                    {
                        returnValue = true;
                     //   if (M == Move.DOWN)
                          //  eventManager.eventStart(EventType.PIECE_LANDED);
                            break;
                    }
                }
            }
        else
                returnValue = false; //technically it doesn't collide with anything, it's out of bounds.

            if (returnValue){
                if(M == Move.ROTATE_CW)
                    currentPiece.rotate(Move.ROTATE_CCW);
                else if(M == Move.ROTATE_CCW)
                    currentPiece.rotate(Move.ROTATE_CW);
                }

            return returnValue;
    }

    /**
     * update methods here
     */

    //Making the assumption this method will invoke ONLY after "canUpdate" has returned true.
    public void update(Game2DObject G2D) {
        Move M = G2D.getMovement();
                        switch(M)
                        {
                            case DOWN:
                                G2D.getShape().modXY(0, 1);
                                break;
                            case LEFT:
                            case RIGHT:
                                G2D.getShape().modXY(M.val, 0);
                                break;
                            case ROTATE_CW:
                            case ROTATE_CCW:
                                //G2D.getShape().updateBlocks(G2D.getShape().getRotation(M));
                                break;
                        }

            }

        public void update(GameINTObject IOBJ){
           //thus far not needed as a visitor pattern.
    }


    /****************************
     * METHODS FOR UPDATING THE WELL'S STATUS. ADDING NEW BLOCKS TO IT (LANDED), CLEARING LINES, AND LAUNCHING EVENTS!
     ***************************/

    public void updateFixToWell(Piece pieceToSet){
        for(int i=0;i<4;i++)
            well[pieceToSet.getActualYLocation(i)][pieceToSet.getActualXLocation(i)].color = pieceToSet.shape[i].color;
        clearLines();
    }

    private void clearLines(){
        //check if lines can be clear. scan from bottom -> up. scans horizontally from right to left.
        GameData.linesToDrop = 0; int yStart=-1;
        int maxY = Math.max(currentPiece.getActualYLocation(0), currentPiece.getActualYLocation(3));
        maxY = Math.max(maxY, currentPiece.getActualYLocation(2));
        for (int y = GameData.Y-2; y >= 1; y--) {
            for (int x = GameData.X - 2; x > 0; x--) {
                if (well[y][x].isEmpty())
                    break;

                if (x==1) //as long as there were no empty spaces, the loop kept going until it reached the other wall!
                {
                    if(yStart==-1)
                        yStart=y;
                    GameData.linesToDrop++;
                    new LineClearedSequence(y); //auto subscribes to the animationDispatcher, animate current y-line to break apart
                    eventManager.eventStart(EventType.LINE_CLEARED);
                    clearLine(y);
                }
            }
        }

        if (yStart != -1){
            Color[][] colorArray = new Color[yStart - GameData.linesToDrop+1][MAX_GAME_X-1];
            for(int i= (yStart-GameData.linesToDrop); i > 0; i--)
                for (int x = 1; x < MAX_GAME_X-1; x++){
                    colorArray[i][x] = well[i][x].color;
                    well[i][x].toggleHide();
                }

            new BlockDropSequence(colorArray, yStart);
            score.linesCleared = GameData.linesToDrop;
            addGameObject(score);
        }
    }

    private void clearLine(int y){
        int x = MAX_GAME_X-2;
        while(x > 0){
            well[y][x--].color = Color.BLACK;
        }
        eventManager.eventStart(EventType.LINE_CLEARED);
    }


    public void updateDropPieces(int yStart, int numOfLinesToDrop){
        int dropFrom;
        for(int i = yStart; i > 0; i--) {
            dropFrom = i - numOfLinesToDrop;
            if (dropFrom > 0)
                for (int x = 1; x < MAX_GAME_X - 1; x++) {
                    well[dropFrom][x].toggleHide();
                    well[i][x].color = well[dropFrom][x].color;
                }
        }
    }


    private KeyListener keyListener = new KeyListener() {
            public void keyPressed(KeyEvent e) {
                //TODO: Possibly create a new logic for game-over menu buttons?
                    if(!gameOverFlag) {
                        if(!pauseMenuFlag)
                        switch (e.getKeyCode())
                        {
                            case KeyEvent.VK_UP:
                                addMovement(Move.ROTATE_CW);
                                break;
                            case KeyEvent.VK_DOWN:
                                addMovement(Move.ROTATE_CCW);
                                break;
                            case KeyEvent.VK_LEFT:
                                addMovement(Move.LEFT);
                                break;
                            case KeyEvent.VK_RIGHT:
                                addMovement(Move.RIGHT);
                                break;
                            case KeyEvent.VK_SPACE:
                                addMovement(Move.DOWN);
                                autoDropper.interrupt();
                                break;
                            case KeyEvent.VK_ESCAPE:
                                pauseMenuFlag = true;
                                eventManager.eventStart(MenuType.GAME_PAUSED);
                                break;
                        }
                    else
                        switch (e.getKeyCode())
                        {
                            case KeyEvent.VK_ENTER:
                            case KeyEvent.VK_ESCAPE:
                                pauseMenuFlag = false;
                                eventManager.eventStart(MenuType.GAME_RESUMED);
                        }
                    }
                    //ELSE for GAME OVER SHIZ
            }
        @Override
        public void keyReleased(KeyEvent e) { }
        @Override
        public void keyTyped(KeyEvent e) { }
    };

}




