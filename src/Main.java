import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        int width = 800;
        int height = 650;

        JFrame frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(width, 50));
        header.setBackground(Constants.COLOR_DARK_GREEN);
        Minesweeper minesweeper = new Minesweeper(width, height - 50, header);

        frame.add(header, BorderLayout.NORTH);
        frame.add(minesweeper, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}