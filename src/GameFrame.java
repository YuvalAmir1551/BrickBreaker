import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1540, 800);
        setTitle("Brick Breaker");

        GamePanel gamePanel = new GamePanel(5, 15);  // Adjust the parameters as needed
        setContentPane(gamePanel);
        setGlassPane(gamePanel.getGlassPane());
        gamePanel.getGlassPane().setVisible(true);
        setVisible(true);
    }
}
