package assets;

public enum Move {
    LEFT(-1),
    RIGHT(1),
    DOWN(1),
    ROTATE_CW(0),
    ROTATE_CCW(0);

    public int val = 0;
    Move(int i){
        val = i;
    }

}
