import java.awt.*;

public class Tile {

    private Boolean mine;
    private Boolean clicked;
    private Boolean rightClicked;
    private int numMines;

    Tile(Boolean m){
        mine = m;
        clicked = false;
        rightClicked = false;
        numMines = 0;
    }

    public boolean hasMine(){
        return mine;
    }

    public boolean isClicked(){
        return clicked;
    }

    public boolean rightClicked(){
        return rightClicked;
    }

    public void setMine(){
        mine = true;
    }

    public void setClicked(){
        clicked = true;
    }

    public void setRightClicked(){
        rightClicked = true;
    }

    public void unsetRightClicked(){
        rightClicked = false;
    }

    public void setNumSurroundingMines(int n){
        numMines = n;
    }

    public int getNumSurroundingMines(){
        return numMines;
    }





}
