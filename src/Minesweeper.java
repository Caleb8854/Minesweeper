import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
//import java.util.Timer;
//import java.util.TimerTask;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.lang3.time.StopWatch;


public class Minesweeper extends JPanel implements MouseListener {



    private int boardWidth;
    private int boardHeight;
    private int flagCount;
    StopWatch stopwatch;
    private JPanel header;
    private Tile[][] grid;
    private Graphics g;
    private Timer timer;


    Minesweeper(int w, int h, JPanel panel){
        boardWidth = w;
        boardHeight = h;
        header = panel;
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.white);
        flagCount = boardWidth/Constants.TILE_SIZE * boardHeight/Constants.TILE_SIZE / 5;
        stopwatch = new StopWatch();

        addMouseListener(this);
        board();

        timer = new Timer(1000, e -> updatePanel2Text());
        timer.setInitialDelay(0);
        updatePanel2Text();

    }

    public void updatePanel2Text() {
        header.removeAll();
        long time = stopwatch.getTime(TimeUnit.SECONDS);
        JLabel label = new JLabel("Flags: " + flagCount + "   Time: " + time);
        label.setForeground(Color.WHITE);
        header.add(label);
        header.revalidate();
        header.repaint();
    }

    public void paintComponent(Graphics graphics){
        this.g = graphics;
        super.paintComponent(g);

        for(int r = 0; r < boardHeight/Constants.TILE_SIZE; r++){
            for(int c = 0; c < boardWidth/Constants.TILE_SIZE; c++){
                fillTileBackground(r,c);
                drawNumberOnTile(r,c);
            }
        }
        g.setColor(Color.BLACK);
        drawGridLines(g);
    }

    public void fillTileBackground(int r, int c){
        if(grid[r][c].rightClicked()){
            g.setColor(Constants.COLOR_FLAG);
        } else if(!grid[r][c].isClicked()){
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
        if(grid[r][c].isClicked() && !grid[r][c].hasMine() && grid[r][c].getNumSurroundingMines() != 0){
            int x = c * Constants.TILE_SIZE + Constants.TILE_SIZE/2 - 5;
            int y = r * Constants.TILE_SIZE + Constants.TILE_SIZE/2 + 9;

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString(grid[r][c].getNumSurroundingMines() + "", x, y);
        }
    }

    public void drawGridLines(Graphics g){
        for(int i = 0; i <= boardWidth/Constants.TILE_SIZE; i++){
            g.drawLine(i*Constants.TILE_SIZE, 0 ,i*Constants.TILE_SIZE, boardHeight);

        }

        for(int j = 0; j <= boardHeight / Constants.TILE_SIZE; j++){
            g.drawLine(0, j*Constants.TILE_SIZE, boardWidth, j*Constants.TILE_SIZE);
        }

    }
    @Override
    public void mouseClicked(MouseEvent e) {

        PointerInfo a = MouseInfo.getPointerInfo();
        Point point = new Point(a.getLocation());
        SwingUtilities.convertPointFromScreen(point, e.getComponent());
        int x =(int) point.getX();
        int y=(int) point.getY();

        if(e.getButton() == MouseEvent.BUTTON1){

            handleLeftClick(x,y);

        } else if (e.getButton() == MouseEvent.BUTTON3){

            handleRightClick(x,y);
            updatePanel2Text();

        }


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
        grid = new Tile[boardHeight / Constants.TILE_SIZE][boardWidth / Constants.TILE_SIZE];

        for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[r].length; c++){
                grid[r][c] = new Tile(false);
            }
        }


    }

    public void generateBoard(int rowFirstClicked, int colFirstClicked){
        int numMines = boardWidth/Constants.TILE_SIZE * boardHeight/Constants.TILE_SIZE / 5;
        int count = 0;
        grid[rowFirstClicked][colFirstClicked].setNumSurroundingMines(0);
        ArrayList<Tile> allSurroundingTiles = surroundingTiles(rowFirstClicked,colFirstClicked);
        while(count < numMines){
            Random random = new Random();
            int ranNum1 = random.nextInt(grid.length);
            int ranNum2 = random.nextInt(grid[0].length);
            if(allSurroundingTiles.contains(grid[ranNum1][ranNum2])){
                continue;
            }
            if(!grid[ranNum1][ranNum2].hasMine()) {
                grid[ranNum1][ranNum2] = new Tile(true);
                count++;
            }
        }

        for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[r].length; c++){
                grid[r][c].setNumSurroundingMines(countSurroundingMines(r,c));
            }
        }
    }

    public ArrayList<Tile> surroundingTiles(int r, int c){
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        for(int i = r-1; i <= r+1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (onBoard(i, j)) {
                    tiles.add(grid[i][j]);
                }
            }
        }
        return tiles;
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

        if(i < 0 || j < 0 || i >= boardHeight / Constants.TILE_SIZE || j >= boardWidth / Constants.TILE_SIZE) return false;
        return true;

    }

    public void handleLeftClick(int xpos, int ypos){
        int col = xpos/Constants.TILE_SIZE;
        int row = ypos/Constants.TILE_SIZE;

        if(!onBoard(row,col) || grid[row][col].isClicked() || grid[row][col].rightClicked()){
            return;
        }
        if(checkIfFirstClick()) {
            timer.start();
            stopwatch.start();
            generateBoard(row,col);
            updatePanel2Text();
        }
        grid[row][col].setClicked();

        if(grid[row][col].getNumSurroundingMines() == 0 && !grid[row][col].hasMine()){
            clickAllZeroTiles(row,col);
        }
        repaint();
    }

    public void handleRightClick(int xpos, int ypos){
        int col = xpos/Constants.TILE_SIZE;
        int row = ypos/Constants.TILE_SIZE;

        if(!onBoard(row,col) || grid[row][col].isClicked()){
            return;
        }

        if(grid[row][col].rightClicked()){
            grid[row][col].unsetRightClicked();
            flagCount++;
        } else {
            grid[row][col].setRightClicked();
            flagCount--;
        }
        repaint();
    }

    public void clickAllZeroTiles(int row, int col){
        if(onBoard(row-1,col-1)) handleLeftClick((col - 1)*Constants.TILE_SIZE,(row-1)*Constants.TILE_SIZE);
        if(onBoard(row,col-1)) handleLeftClick((col - 1)*Constants.TILE_SIZE,(row)*Constants.TILE_SIZE);
        if(onBoard(row+1,col-1)) handleLeftClick((col - 1)*Constants.TILE_SIZE,(row+1)*Constants.TILE_SIZE);
        if(onBoard(row-1,col))handleLeftClick((col)*Constants.TILE_SIZE,(row-1)*Constants.TILE_SIZE);
        if(onBoard(row+1,col)) handleLeftClick((col)*Constants.TILE_SIZE,(row+1)*Constants.TILE_SIZE);
        if(onBoard(row-1,col+1)) handleLeftClick((col + 1)*Constants.TILE_SIZE,(row-1)*Constants.TILE_SIZE);
        if(onBoard(row,col+1))handleLeftClick((col + 1)*Constants.TILE_SIZE,(row)*Constants.TILE_SIZE);
        if(onBoard(row+1,col+1))handleLeftClick((col + 1)*Constants.TILE_SIZE,(row+1)*Constants.TILE_SIZE);
    }

    public boolean checkIfFirstClick(){
        for (Tile[] tiles : grid) {
            for (Tile tile : tiles) {
                if (tile.isClicked()) return false;
            }
        }
        return true;
    }


}
