package swingAnimator;

import assets.GameData;

public abstract class SwingAnimatorListener {
    //auto-add this listener on creation to the dispatcher (static SwingAnimationAdapter)
    public SwingAnimatorListener(){
        GameData.animationDispatcher.addListener(this);
    }

    public abstract void animationInitialize(int x, int y);
    public abstract void animationUpdate();
    public abstract boolean animationTerminate();
    public abstract boolean filterByLayer(AnimLayer layer);

    public abstract void draw(java.awt.Graphics G);
}
