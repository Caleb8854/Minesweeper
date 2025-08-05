import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        int width = 800;
        int height = 650;
        int headerHeight = 50;

        JFrame frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(width,height));

        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(width, headerHeight));
        header.setBackground(Constants.COLOR_GREEN);

        JPanel endScreen = new JPanel();
        endScreen.setBackground(new Color(255, 255, 255));
        endScreen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true));
        endScreen.setLayout(null);
        endScreen.setOpaque(true);

        JPanel overlay = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        overlay.setOpaque(false);
        overlay.setBounds(0, 0, width, height);
        overlay.setVisible(false);


        Minesweeper minesweeper = new Minesweeper(width, height - 50, header, endScreen, overlay);

        header.setBounds(0, 0, width, headerHeight);
        minesweeper.setBounds(0, headerHeight, width, height - headerHeight);
        endScreen.setBounds(width/2 - 150, height/2 - 150, 300, 300);

        layeredPane.add(header, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(minesweeper, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(endScreen, JLayeredPane.POPUP_LAYER);
        layeredPane.add(overlay, JLayeredPane.MODAL_LAYER);

        frame.add(layeredPane);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}