package swingAnimator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/*
This class handles "on tick events", and when it has to start and close.
It refers to CallbackListener in order to obtain its actual functionality, which is currently empty.
In this class, I need to implement the start, stop, and goto "callback" methods.
 ##
 CallbackListener handles what happens during animation, and end of animation.

 1. Create handling constructors for callback
 */
public class SwingAnimator {

    private Timer timer = null;
    public ArrayList<SwingAnimatorListener> listeners = new ArrayList<>();
    private ArrayList<SwingAnimatorListener> removeListener = new ArrayList<>();
    //private SwingAnimatorCallback callback;
    public int FPS;

    public SwingAnimator(int FPS) {
        setFPS(FPS);
        timer = new Timer(FPS, new CallbackListener());
    }

    public SwingAnimator(int FPS, SwingAnimatorListener listener)
    {
        this(FPS); //this.fps = FPS
        listeners.add(listener);
    }

    public void setFPS(int framesPerSecond){
        FPS = 1000 / framesPerSecond;}

    public void addListener(SwingAnimatorListener listener){
                listeners.add(listener);
    }
    public void addListenerAndStart(SwingAnimatorListener listener){
        addListener(listener);
        start();
    }
    public void removeListener(SwingAnimatorListener listener) {
        listeners.remove(listener); }
    public void removeListener(int index){
        listeners.remove(index); }
    public void removeListener(ArrayList<SwingAnimatorListener> removeList){
        this.listeners.removeAll(removeList);
        this.removeListener.removeAll(removeList);
    }

    public void start(){
        if (isRunning())
            return;
        //initiate animation call
        timer.start();

    }
    public void stop(){
        if (isRunning())
            timer.stop();
    }
    public boolean isRunning(){
        if (timer != null)
                return (timer.isRunning());
            return false;
    }

    /**
     * runs on every 'tick', and calls to timer's & ALL animation-objects end, and timer's runtime functionalities.
     */
    private class CallbackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                if(!listeners.isEmpty()) { //listeners exist to operate upon
                    for (SwingAnimatorListener l : listeners) {
                        if (l.animationTerminate()) //on event-object's end, remove it from the list.
                            removeListener.add(l);
                        else
                            l.animationUpdate();
                    }
                }
            else
                timer.stop(); //if the timer ran, but then all animations ended, self-close.
            if (!(removeListener.isEmpty())) {
                removeListener(removeListener);
            }

        }
    }
}
