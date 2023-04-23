package assets.gameObjects;

import manager.Manager;

public abstract class GameObject {
    public GameObject() {
        System.out.println("New game object!");
    }

    public abstract void    update();
    public abstract boolean canUpdate();


    public Manager parent;
    public GameObject(Manager manager){
        this.parent = manager;
    }
}
