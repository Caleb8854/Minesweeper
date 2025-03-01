import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        int width = 400;
        int height = 400;
        frame.setVisible(true);
        frame.setSize(width,height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Minesweeper minesweeper = new Minesweeper(width,height,10);
        frame.add(minesweeper);
        frame.pack();
    }
}