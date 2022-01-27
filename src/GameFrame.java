import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {

	Image iconApp = new ImageIcon(Main.class.getResource("/snake.png")).getImage();

	public GameFrame() {
		this.add(new GamePanel());
		this.pack();
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Snake game");
		this.setIconImage(iconApp);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
