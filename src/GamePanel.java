
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener {

	Image headImage = new ImageIcon(Main.class.getResource("/head.png")).getImage();
	Image appleIcon = new ImageIcon(Main.class.getResource("/apple.png")).getImage();
	Image upmouth = new ImageIcon(Main.class.getResource("/upmouth.png")).getImage();
	Image downmouth = new ImageIcon(Main.class.getResource("/downmouth.png")).getImage();
	Image leftmouth = new ImageIcon(Main.class.getResource("/leftmouth.png")).getImage();
	Image rightmouth = new ImageIcon(Main.class.getResource("/rightmouth.png")).getImage();

	boolean running = false;
	boolean gameOver = false;

	final int SCREEN_WIDTH = 782;
	final int SCREEN_HEIGHT = 725;
	final int UNIT_SIZE = 25;

	int snakeLength = 3;
	int snake_x[] = new int[(750 * 635) / (UNIT_SIZE * UNIT_SIZE)];
	int snake_y[] = new int[(750 * 635) / (UNIT_SIZE * UNIT_SIZE)];
	int apple_x;
	int apple_y;

	char direction = 'R';
	int delay = 110;
	Timer timer;
	Random random;

	int score = 0;

	public GamePanel() {
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(new Color(0x476072));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());

		timer = new Timer(delay, this);
		random = new Random();

		newApple();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		// vẽ khung
		g.setColor(Color.white);
		g.drawRect(15, 10, 752, 52);
		g.drawRect(15, 72, 752, 627);

		g.drawImage(headImage, 16, 11, null);
		g.setColor(Color.black);
		g.fillRect(16, 73, 750, 625);

		// copyright
		g.setColor(Color.white);
		g.setFont(new Font("MV Boli", Font.PLAIN, 15));
		FontMetrics metrics = getFontMetrics(g.getFont());
		String copyright = "Made by CK with Love";
		g.drawString(copyright, (SCREEN_WIDTH - metrics.stringWidth(copyright)) / 2, SCREEN_HEIGHT - 9);

		// khởi tạo vị trí con rắn
		if (!running) {
			snake_x[0] = 116;
			snake_x[1] = 91;
			snake_x[2] = 66;

			snake_y[0] = 98;
			snake_y[1] = 98;
			snake_y[2] = 98;
		}

		// vẽ thân con rắn
		g.setColor(new Color(0x5089C6));
		for (int i = snakeLength - 1; i > 0; i--) {
			g.fillOval(snake_x[i], snake_y[i], UNIT_SIZE, UNIT_SIZE);
		}

		// vẽ đầu con rắn
		switch (direction) {
			case 'R':
				g.drawImage(rightmouth, snake_x[0], snake_y[0], null);
				break;
			case 'L':
				g.drawImage(leftmouth, snake_x[0], snake_y[0], null);
				break;
			case 'U':
				g.drawImage(upmouth, snake_x[0], snake_y[0], null);
				break;
			case 'D':
				g.drawImage(downmouth, snake_x[0], snake_y[0], null);
				break;
		}

		// vẽ quả táo
		g.drawImage(appleIcon, apple_x, apple_y, null);

		// hiển thị điểm
		g.setFont(new Font("Dialog", Font.PLAIN, 20));
		g.setColor(Color.white);
		g.drawString("Score : " + score, 600, 43);

		// hiển thị kết quả
		if (gameOver) {
			g.setColor(Color.red);
			g.setFont(new Font("MV Boli", Font.BOLD, 50));
			FontMetrics metrics1 = getFontMetrics(g.getFont());
			String message1 = "GAME OVER";
			g.drawString(message1, (SCREEN_WIDTH - metrics1.stringWidth(message1)) / 2, SCREEN_HEIGHT / 2);

			g.setColor(Color.white);
			g.setFont(new Font("Dialog", Font.PLAIN, 20));
			FontMetrics metrics2 = getFontMetrics(g.getFont());
			String message2 = "Press SPACE to replay game";
			g.drawString(message2, (SCREEN_WIDTH - metrics2.stringWidth(message2)) / 2, SCREEN_HEIGHT / 2 + 50);
		}

		g.dispose();
	}

	public void move() {
		for (int i = snakeLength; i > 0; i--) {
			snake_x[i] = snake_x[i - 1];
			snake_y[i] = snake_y[i - 1];
		}
		switch (direction) {
			case 'R':
				snake_x[0] += UNIT_SIZE;
				break;
			case 'L':
				snake_x[0] -= UNIT_SIZE;
				break;
			case 'U':
				snake_y[0] -= UNIT_SIZE;
				break;
			case 'D':
				snake_y[0] += UNIT_SIZE;
				break;
		}
		// khi đâm vào 1 cạnh nó sẽ đi ra ở cạnh đối diện
		// các con số đều được tính toán hoàn hảo theo kích thước khung
		if (snake_x[0] > 741) {
			snake_x[0] = 16;
		}
		if (snake_x[0] < 16) {
			snake_x[0] = 741;
		}
		if (snake_y[0] > 673) {
			snake_y[0] = 73;
		}
		if (snake_y[0] < 73) {
			snake_y[0] = 673;
		}

	}

	// tạo tọa độ mới cho quả táo
	public void newApple() {
		apple_x = random.nextInt(750 / UNIT_SIZE) * UNIT_SIZE + 16;
		apple_y = random.nextInt(625 / UNIT_SIZE) * UNIT_SIZE + 73;

		// tọa độ quả táo ko được trùng với con rắn
		for (int i = 0; i < snakeLength; i++) {
			if (snake_x[i] == apple_x && snake_y[i] == apple_y) {
				newApple();
			}
		}
	}

	// rắn ăn táo
	public void checkApple() {
		if (snake_x[0] == apple_x && snake_y[0] == apple_y) {
			snakeLength++;
			score++;
			if (delay > 10) {
				delay -= 1;
				timer.stop();
				timer = new Timer(delay, this);
				timer.start();
			}
			newApple();
		}
	}

	// kiểm tra va chạm
	public void checkCollision() {
		for (int i = 1; i < snakeLength; i++) {
			if (snake_x[0] == snake_x[i] && snake_y[0] == snake_y[i]) {
				timer.stop();
				gameOver = true;
			}
		}
	}

	public void restartGame() {
		running = false;
		gameOver = false;
		snakeLength = 3;
		score = 0;
		direction = 'R';
		delay = 110;
		newApple();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running && !gameOver) {
			move();
			checkApple();
			checkCollision();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (direction != 'R' && !gameOver) {
						direction = 'L';
						running = true;
						timer.start();
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (direction != 'L' && !gameOver) {
						direction = 'R';
						running = true;
						timer.start();
					}
					break;
				case KeyEvent.VK_UP:
					if (direction != 'D' && !gameOver) {
						direction = 'U';
						running = true;
						timer.start();
					}
					break;
				case KeyEvent.VK_DOWN:
					if (direction != 'U' && !gameOver) {
						direction = 'D';
						running = true;
						timer.start();
					}
					break;
				case KeyEvent.VK_SPACE:
					if (!gameOver) {
						if (timer.isRunning()) {
							timer.stop();
						} else {
							timer.start();
						}
					} else {
						restartGame();
					}
					break;

			}
		}
	}

}
