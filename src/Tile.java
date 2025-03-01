import java.awt.*;

public class Tile {

    private Boolean mine;
    private Boolean clicked;
    private int numMines;

    Tile(Boolean m){
        mine = m;
        clicked = false;
        numMines = 0;
    }

    public boolean hasMine(){
        return mine;
    }

    public boolean isClicked(){
        return clicked;
    }

    public void setMine(){
        mine = true;
    }

    public void setClicked(){
        clicked = true;
    }

    public void setNumSurroundingMines(int n){
        numMines = n;
    }

    public int getNumSurroundingMines(){
        return numMines;
    }





}
