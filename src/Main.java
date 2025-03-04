import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        int width = 600;
        int height = 600;
        frame.setVisible(true);
        frame.setSize(width,height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Minesweeper minesweeper = new Minesweeper(width,height,15);
        frame.add(minesweeper);
        frame.pack();
    }
}