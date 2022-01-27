package assets.gameObjects;

public abstract class Game2DObject extends GameObject{
    public Game2DObject(manager.Manager manager) {
        super(manager);
    }

    protected Game2DObject() {
        super();
    }

    public abstract assets.Block[] getBlocks();
    public abstract assets.Piece   getShape();
    public abstract assets.Move    getMovement();
    public abstract void    setMovement(assets.Move M);


}
