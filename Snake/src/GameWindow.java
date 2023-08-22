import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    GameWindow() {
        this.add(new GamePanel());
        this.setTitle("Snake - The Game");
        ImageIcon snake = new ImageIcon(getClass().getResource("snakeIcon.png"));
        this.setIconImage(snake.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
