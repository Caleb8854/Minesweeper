import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.Timer;


import org.apache.commons.lang3.time.StopWatch;


public class Minesweeper extends JPanel implements MouseListener {



    private int boardWidth;
    private int boardHeight;
    private int flagCount;
    private long fastestTime;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private int numOpenTiles;
    private boolean win;
    private boolean end = false;
    StopWatch stopwatch;
    private JPanel header;
    private JPanel endScreen;
    private JPanel overlay;
    private JButton restart;
    private JLabel timeLabel;
    private Tile[][] grid;
    private Timer timer;



    Minesweeper(int w, int h, JPanel panel1, JPanel panel2, JPanel o){
        boardWidth = w;
        boardHeight = h;
        header = panel1;
        overlay = o;
        endScreen = panel2;

        endScreen.setVisible(false);
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.white);
        flagCount = boardWidth/Constants.TILE_SIZE * boardHeight/Constants.TILE_SIZE / 5;
        stopwatch = new StopWatch();

        addMouseListener(this);
        board();

        timer = new Timer(1000, e -> updatePanel2Text());
        timer.setInitialDelay(0);

        restart = new JButton("Restart");

        endScreen.setLayout(null);

        updatePanel2Text();
        restart.addActionListener(e -> {
            restart();
        });

        restart.setBounds(20, 30, 90, 22);
        endScreen.add(restart);
        restart.setVisible(true);


    }

    public void updatePanel2Text() {
        long time = stopwatch.getTime(TimeUnit.SECONDS);
        String text = "Flags: " + flagCount + "   Time: " + time;

        if(timeLabel == null){
            timeLabel = new JLabel(text);
            timeLabel.setForeground(Color.WHITE);
            timeLabel.setFont(new Font("Helvetica", Font.PLAIN, 30));
            header.add(timeLabel, BorderLayout.CENTER);
        } else {
            timeLabel.setText(text);
        }

        header.revalidate();
        header.repaint();
    }

    public void updateEndScreen(){
        endScreen.removeAll();
        overlay.setVisible(true);
        restart.setVisible(true);
        String timeText;
        String personalBestText;

        if (win) {
            long time = stopwatch.getTime(TimeUnit.SECONDS);
            timeText = "Time: " + time;

            if (fastestTime == 0 || time < fastestTime) {
                fastestTime = time;
            }
        } else {
            timeText = "Time: ---";
        }

        if (fastestTime > 0) {
            personalBestText = "PB: " + fastestTime;
        } else {
            personalBestText = "PB: ---";
        }

        JLabel label = new JLabel(timeText + "   " + personalBestText);
        label.setBounds(138,30,120,22);
        label.setForeground(Color.WHITE);
        endScreen.add(label);
        endScreen.add(restart);
        endScreen.revalidate();
        endScreen.repaint();


    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        for(int r = 0; r < boardHeight/Constants.TILE_SIZE; r++){
            for(int c = 0; c < boardWidth/Constants.TILE_SIZE; c++){
                fillTileBackground(r,c,g);
                drawNumberOnTile(r,c,g);
            }
        }
        g.setColor(Color.BLACK);
        drawGridLines(g);
    }

    public void fillTileBackground(int r, int c, Graphics g){
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

    public void drawNumberOnTile(int r, int c, Graphics g){
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

    public void restart(){
        endScreen.setVisible(false);
        overlay.setVisible(false);
        stopwatch.reset();
        timer.stop();
        flagCount = boardWidth / Constants.TILE_SIZE * boardHeight / Constants.TILE_SIZE / 5;
        restart.setVisible(false);
        end = false;
        board();
        repaint();
        updatePanel2Text();

    }
    @Override
    public void mouseClicked(MouseEvent e) {

        if(end) return;
        int x = e.getX();
        int y = e.getY();

        if(e.getButton() == MouseEvent.BUTTON1){

            handleLeftClick(x,y);
            if(numOpenTiles == 0){
                timer.stop();
                end = true;
                win = true;
                updateEndScreen();
                endScreen.setVisible(true);
            }


        } else if (e.getButton() == MouseEvent.BUTTON3){

            handleRightClick(x,y);
            updatePanel2Text();

        }


    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(end) return;
        if(SwingUtilities.isLeftMouseButton(e)){
            leftPressed = true;
        }
        if(SwingUtilities.isRightMouseButton(e)){
            rightPressed = true;
        }
        if(leftPressed && rightPressed){
            chord(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(end) return;
        if(SwingUtilities.isLeftMouseButton(e)){
            leftPressed = false;
        }
        if(SwingUtilities.isRightMouseButton(e)){
            rightPressed = false;
        }

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
        numOpenTiles = boardWidth / Constants.TILE_SIZE * boardHeight/ Constants.TILE_SIZE - numMines;
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


        if(!onBoard(row,col) || grid[row][col].isClicked() || grid[row][col].rightClicked()) return;

        if(checkIfFirstClick()) {
            timer.start();
            stopwatch.start();
            generateBoard(row,col);
            updatePanel2Text();
        }
        grid[row][col].setClicked();
        numOpenTiles--;
        if(grid[row][col].hasMine()) {
            timer.stop();
            end = true;
            win = false;
            revealRemainingMines();
            updateEndScreen();
            endScreen.setVisible(true);
        }
        if(grid[row][col].getNumSurroundingMines() == 0 && !grid[row][col].hasMine()){
            clickAllZeroTiles(row,col);
        }
        repaint();
    }

    public void handleRightClick(int xpos, int ypos){
        int col = xpos/Constants.TILE_SIZE;
        int row = ypos/Constants.TILE_SIZE;

        if(!onBoard(row,col) || grid[row][col].isClicked()) return;

        if(grid[row][col].rightClicked()){
            grid[row][col].unsetRightClicked();
            flagCount++;
        } else {
            grid[row][col].setRightClicked();
            flagCount--;
        }
        repaint();
    }

    private void chord(int xpos, int ypos){
        int col = xpos/Constants.TILE_SIZE;
        int row = ypos/Constants.TILE_SIZE;

        if(!grid[row][col].isClicked()) return;

        if(countSurroundingFlags(row,col) == grid[row][col].getNumSurroundingMines()){
            for(int i = row-1; i <= row+1; i++){
                for(int j = col - 1; j <= col + 1; j++){
                    if(onBoard(i,j) && !grid[i][j].rightClicked() && !grid[i][j].isClicked()) handleLeftClick(j * Constants.TILE_SIZE, i * Constants.TILE_SIZE);

                }
            }
        }
        repaint();

    }

    public void revealRemainingMines(){
        for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[r].length; c++){
                if(grid[r][c].hasMine() && !grid[r][c].rightClicked()) grid[r][c].setClicked();
                if(!grid[r][c].hasMine() && grid[r][c].rightClicked()) grid[r][c].unsetRightClicked();
            }
        }
    }

    public int countSurroundingFlags(int r, int c){
        int count = 0;
        for(int i = r-1; i <= r+1; i++){
            for(int j = c - 1; j <= c + 1; j++){
                if(onBoard(i,j) && grid[i][j].rightClicked()) count++;

            }
        }
        return count;
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
