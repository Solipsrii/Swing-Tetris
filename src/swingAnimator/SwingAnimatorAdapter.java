package swingAnimator;

import java.util.ArrayList;

public class SwingAnimatorAdapter {
    private ArrayList<SwingAnimatorListener> listeners;
    private ArrayList<SwingAnimatorListener> removalBuffer;
    private ArrayList<SwingAnimatorListener> adderBuffer;

    public SwingAnimatorAdapter() {
        listeners = new ArrayList<>();
        adderBuffer = new ArrayList<>();
        removalBuffer = new ArrayList<>();
    }

    public void addListener(SwingAnimatorListener listener) {
        adderBuffer.add(listener);
    }

        //main operation
        public void update () {
            if (!listeners.isEmpty()) { //listeners exist to operate upon
                for (SwingAnimatorListener l : listeners) {
                    if (l.animationTerminate()) //on anim-object's end, remove it from the list.
                        removalBuffer.add(l);
                    else
                        l.animationUpdate();
                }
            }

            if (!removalBuffer.isEmpty()) {
                listeners.removeAll(removalBuffer);
                removalBuffer.clear();
            }
            if (!adderBuffer.isEmpty()) {
                listeners.addAll(adderBuffer);
                adderBuffer.clear();
            }
        }

        public ArrayList<SwingAnimatorListener> getAllListeners () {
            return listeners;
        }
    }
