import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

public class Minesweeper extends JPanel implements MouseListener {



    private int boardWidth;
    private int boardHeight;
    private int boardSize;
    private JPanel panel;
    private Tile[][] grid;
    private Graphics g;


    Minesweeper(int w, int h, int boardSize){
        this.boardSize = boardSize;
        boardWidth = w;
        boardHeight = h;
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.white);
        panel = new JPanel();
        addMouseListener(this);
        board();

    }

    public void paintComponent(Graphics graphics){
        this.g = graphics;
        super.paintComponent(g);

        for(int r = 0; r < boardWidth/Constants.TILE_SIZE; r++){
            for(int c = 0; c < boardHeight/Constants.TILE_SIZE; c++){
                fillTileBackground(r,c);
                drawNumberOnTile(r,c);
            }
        }
        g.setColor(Color.BLACK);
        drawGridLines(g);
    }

    public void fillTileBackground(int r, int c){
        if(!grid[r][c].isClicked()){
            g.setColor(Constants.COLOR_GREEN);
        } else {
            if(!grid[r][c].hasMine()){
                g.setColor(Constants.COLOR_BROWN);
            } else {
                g.setColor(Constants.COLOR_MINE);
            }
        }

        g.fillRect(c * Constants.TILE_SIZE,r*Constants.TILE_SIZE,Constants.TILE_SIZE,Constants.TILE_SIZE);
    }

    public void drawNumberOnTile(int r, int c){
        if(grid[r][c].isClicked() && !grid[r][c].hasMine()){
            int x = c * Constants.TILE_SIZE + Constants.TILE_SIZE/2 - 5;
            int y = r * Constants.TILE_SIZE + Constants.TILE_SIZE/2 + 9;
           // System.out.println(x + ","+y);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString(grid[r][c].getNumSurroundingMines() + "", x, y);
        }
    }

    public void drawGridLines(Graphics g){
        for(int i = 0; i < boardWidth/Constants.TILE_SIZE; i++){
            g.drawLine(i*Constants.TILE_SIZE, 0 ,i*Constants.TILE_SIZE, boardHeight);
            g.drawLine(0, i*Constants.TILE_SIZE, boardWidth, i*Constants.TILE_SIZE);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        PointerInfo a = MouseInfo.getPointerInfo();
        Point point = new Point(a.getLocation());
        SwingUtilities.convertPointFromScreen(point, e.getComponent());
        int x =(int) point.getX();
        int y=(int) point.getY();
     //   System.out.println(x + ", " + y);

        handleMouseClick(x,y);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void board(){
        grid = new Tile[boardWidth / Constants.TILE_SIZE][boardHeight / Constants.TILE_SIZE];

        for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[r].length; c++){
                grid[r][c] = new Tile(false);
            }
        }
        // randomly place n mines
        grid[3][2] = new Tile(true);
        grid[5][6] = new Tile(true);
        grid[1][8] = new Tile(true);
        grid[1][1] = new Tile(true);

       // board generated above
        //now that the board is generated, for each tile, count the number of mines surrounding it
        for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[r].length; c++){
                grid[r][c].setNumSurroundingMines(countSurroundingMines(r,c));
            }
        }

    }

    public int countSurroundingMines(int r, int c){
        int count = 0;
        for(int i = r-1; i <= r+1; i++){
            for(int j = c - 1; j <= c + 1; j++){
                if(onBoard(i,j) && (i != r || j != c)) {
                    if(grid[i][j].hasMine()) count++;
                }
            }
        }
        return count;
    }

    public boolean onBoard(int i, int j){

        if(i < 0 || j < 0 || i >= boardSize || j >= boardSize) return false;
        return true;

    }

    public void handleMouseClick(int xpos, int ypos){
        int col = xpos/Constants.TILE_SIZE;
        int row = ypos/Constants.TILE_SIZE;
        //System.out.println(row + ", " + col);
        if(!grid[row][col].isClicked()){
            grid[row][col].setClicked();
        }
        repaint();
    }





    //get mouse coordinates when u click,calc index on array, update backend, update board

}
