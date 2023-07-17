import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

public class GamePanel extends JPanel {
    private GameMatrix gameMatrix;
    private BricksPanel bricksPanel;
    private TopPanel topPanel;
    private JLabel levelLbl;
    private JLabel scoreLbl;
    private JLabel[][] bricks;
    private int n;
    private int m;
    private Timer timer;
    private TimerListener timerLis;
    private KeyListener keyLis;
    private int interval;
    private Point ballLocation;
    private Point ballDirection;
    private Point paddleLocation;
    private JButton start;
    private ButtonListener ButtonLis;
    private int paddleWidth;
    private int paddleHeight;
    private int ballRadius;
    private int topPanelHeight;
    private GlassPane glassPane;

    private int level;
    int[][] matrix;
    int score;
    private Image background;
    private Image paddle_icon;
    private Image ball_icon;
    private Image game_over;
    private Image cur_background;
    private Image top_panel_background;

    public GamePanel(int n, int m) {
        setLayout(new BorderLayout());
        this.n = n;
        this.m = m;
        score = 0;
        background = Toolkit.getDefaultToolkit().getImage("background.png");
        paddle_icon = Toolkit.getDefaultToolkit().getImage("paddle_icon.png");
        ball_icon = Toolkit.getDefaultToolkit().getImage("ball_icon.png");
        game_over = Toolkit.getDefaultToolkit().getImage("game_over.jpg");
        top_panel_background = Toolkit.getDefaultToolkit().getImage("top_panel_background.png");
        cur_background = background;
        glassPane = new GlassPane();
        ballDirection = new Point(5, -4);
        paddleLocation = new Point(-1000, -1000);
        ballLocation = new Point(-1000, -1000);
        interval = 10;
        timerLis = new TimerListener();
        keyLis = new KeyListener();
        ButtonLis = new ButtonListener();
        timer = new Timer(interval, timerLis);
        bricksPanel = new BricksPanel();
        topPanel = new TopPanel();
        bricksPanel.setLayout(new GridLayout(4 * n, m, 7, 7));

        add(topPanel, BorderLayout.NORTH);
        add(bricksPanel, BorderLayout.CENTER);

        gameMatrix = new GameMatrix(n, m, 1);
        matrix = gameMatrix.getMatrix();
        level = 1;
        bricks = new JLabel[4 * n][m];
        scoreLbl = new JLabel();
        levelLbl = new JLabel();
        scoreLbl.setText("SCORE: 0");
        levelLbl.setText("LEVEL: 1");

        drawFirstBricks();
        start = new JButton();
        start.addActionListener(ButtonLis);
        start.setEnabled(true);
        start.setText("Start");

        scoreLbl.setBorder(new LineBorder(Color.BLACK, 4));
        scoreLbl.setOpaque(true);
        scoreLbl.setBackground(Color.ORANGE);


        levelLbl.setBorder(new LineBorder(Color.BLACK, 4));
        levelLbl.setOpaque(true);
        levelLbl.setBackground(Color.ORANGE);

        topPanel.add(scoreLbl);
        topPanel.add(start);
        topPanel.add(levelLbl);

        paddleHeight = 25;
        paddleWidth = 180;

        ballRadius = 25;

        addKeyListener(keyLis);
        setFocusable(true);
        requestFocus();

        topPanelHeight = 0;
    }

    public void start() {
        topPanelHeight = topPanel.getHeight();
        ballLocation.x = getWidth() / 2 - ballRadius / 2;
        ballLocation.y = getHeight() - 50 - ballRadius;
        paddleLocation.x = getWidth() / 2 - paddleWidth / 2;
        paddleLocation.y = getHeight() - 20 - paddleHeight;
        timer.start();
    }

    public void drawFirstBricks() {
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < m; j++) {
                JLabel label = new JLabel();
                bricksPanel.add(label);
                bricks[i][j] = label;
            }
        }
        LineBorder border = new LineBorder(Color.GRAY, 5);
        for (int i = 1; i < n + 1; i++) {
            for (int j = 0; j < m; j++) {
                JLabel label = new JLabel();
                label.setOpaque(true);
                label.setBackground(Color.ORANGE);
                label.setBorder(border);
                bricksPanel.add(label);
                bricks[i][j] = label;
            }
        }
        for (int i = n + 1; i < 4 * n; i++) {
            for (int j = 0; j < m; j++) {
                JLabel label = new JLabel();
                bricksPanel.add(label);
                bricks[i][j] = label;
            }
        }
    }

    public class KeyListener extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    if (paddleLocation.x > 15)
                        paddleLocation.x -= 15;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (paddleLocation.x + paddleWidth < getWidth() - 20)
                        paddleLocation.x += 15;
                    break;
            }
            glassPane.repaint();
        }
    }

    public class TimerListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (ballLocation.x <= 0 || ballLocation.x + ballRadius >= getWidth())
                ballDirection.x *= -1;
            else if (ballLocation.y <= topPanelHeight)
                ballDirection.y *= -1;
            else if (ballLocation.y + ballRadius + 3 >= paddleLocation.y) {
                    if (ballLocation.x + ballRadius >= paddleLocation.x && ballLocation.x <= paddleLocation.x + paddleWidth) {
                        if (ballLocation.x + ballRadius < paddleLocation.x + paddleWidth / 3) {
                            ballDirection.x = -5;
                        } else if (ballLocation.x > paddleLocation.x + 2 * paddleWidth / 3) {
                        ballDirection.x = 5;
                        }
                        ballDirection.y *= -1;
                    }
            }
            if (ballLocation.y + ballRadius >= getHeight()) {
                timer.stop();
                start.setText("Try again");
                cur_background = game_over;
                start.setEnabled(true);
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (matrix[i][j] > 0) {
                        JLabel brick = bricks[i + 1][j];
                        Rectangle brickBounds = brick.getBounds();
                        if (
                                        ballLocation.y - ballRadius - 10 <= brickBounds.y + brickBounds.height &&
                                        ballLocation.y + ballRadius >= brickBounds.y &&
                                        ballLocation.x + ballRadius >= brickBounds.x &&
                                        ballLocation.x <= brickBounds.x + brickBounds.width
                        ) {
                            matrix[i][j]--;
                            if (matrix[i][j] == 0) {
                                brick.setVisible(false);
                            }
                            else{
                                brick.setBackground(Color.ORANGE);
                            }
                            if (ballLocation.x + ballRadius < brickBounds.x + 3 || ballLocation.x > brickBounds.x + brickBounds.width - 3)
                                ballDirection.x *= -1;
                            else
                                ballDirection.y *= -1;
                            score += 5;
                            if (LevelEnd()){
                                level++;
                                newGame();
                            }
                            break;
                        }
                    }
                }
            }
            ballLocation.x += ballDirection.x;
            ballLocation.y += ballDirection.y;
            glassPane.repaint();
        }
    }

    public class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (start.getText().equals("Start")) {
                start();
            }
            else {
                score = 0;
                level = 1;
                scoreLbl.setText("SCORE: 0");
                levelLbl.setText("LEVEL: 1");
                start.setText("Start");
                cur_background = background;
                newGame();
            }
            start.setEnabled(false);
            glassPane.repaint();
        }
    }

    public class GlassPane extends JPanel {
        public GlassPane() {
            setOpaque(false);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            scoreLbl.setText("SCORE: " + score);
            g.drawImage(ball_icon, ballLocation.x, ballLocation.y, ballRadius, ballRadius, this);
            g.drawImage(paddle_icon, paddleLocation.x, paddleLocation.y, paddleWidth, paddleHeight, this);
        }
    }

    public GlassPane getGlassPane() {
        return glassPane;
    }
    public boolean LevelEnd(){
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (matrix[i][j] > 0)
                    return false;
            }
        }
        return true;
    }
    public void newGame(){
        gameMatrix = new GameMatrix(n, m, level);
        matrix = gameMatrix.getMatrix();
        levelLbl.setText("LEVEL: " + level);
        for (int i = 1; i < n + 1; i++) {
            for (int j = 0; j < m; j++) {
                JLabel brick = bricks[i][j];
                if (matrix[i - 1][j] == 1)
                    brick.setBackground(Color.ORANGE);
                else
                    brick.setBackground(Color.GRAY);
                brick.setVisible(true);
            }
        }
        paddleLocation.x = getWidth() / 2 - paddleWidth / 2;
        paddleLocation.y = getHeight() - 20 - paddleHeight;
        start();
        glassPane.repaint();
    }
    public class BricksPanel extends JPanel{
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(cur_background, 0, 0, getWidth(), getHeight(), this);
        }
    }
    public class TopPanel extends JPanel{
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(top_panel_background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
