//MainGame.java
//Ryan Snow

//The Main Game class runs the game loop and sets up key listener and the screen and paints

//----IMPORTS----
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;


public class MainGame extends JPanel
{
	Player player = new Player(this);
	ArrayList<Wave> w = new ArrayList<>();
	Wave w1 = new Wave(1,3,player,this,true);
	Boss b = new Boss(100,100,this,player);

	BossWave bw = new BossWave(false,b,this,player);
	Wave w2 = new Wave(2,5,player,this,false);
	Wave w3 = new Wave(3,7,player,this,false);
	Wave w4 = new Wave(4,9,player,this,false);
	Wave w5 = new Wave(5,9,player,this,false);
	int wavesB = 0;
	boolean start = true;

	public MainGame()
	{

		//Listens for a KeyEvent, then sends it to the player class to be handled
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				player.keyTyped(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				player.keyReleased(e);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				player.keyPressed(e);
			}
		});
		setFocusable(true);
		w.add(w1);
		w.add(w2);
		w.add(w3);
		w.add(w4);
		w.add(w5);
		player.shoot();
		JOptionPane.showMessageDialog(this, "Space Adventure is a game where you have to destroy the enemy ships!\n  Killing all enemies will spawn the next wave.  There are five waves with\n a final boss at the end! \n\nCONTROLS:\nShoot-Space\nMove-Arrow Keys", "Space Adventure", JOptionPane.INFORMATION_MESSAGE);
	}

	public void move()
	{
		player.move();
	}

	public void gameOver()
	{

			JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.YES_NO_OPTION);
			System.exit(ABORT);
	}

	public void win()
	{
		JOptionPane.showMessageDialog(this,"YOU HAVE WON","You have beaten Space Adventure",JOptionPane.YES_NO_OPTION);
		System.exit(ABORT);
	}

	public void paint(Graphics g) {
		try{
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.black);
		g2d.fillRect(0,0,600,800);
		player.paint(g2d);

		if(wavesB == 0)
		{
		for(int i = 0; i < 5;i++)
		{
			player.checkCollision(w.get(i));
			if(w.get(i).swave && !w.get(i).beat)
			{
				w.get(i).update(g2d);
			}
			else if(!w.get(i).swave)
			{
				if(w.get(i-1).beat)
				{
					w.get(i).update(g2d);

				}
			}

		}
		checkifWon(w);
	}
		else if(!bw.done && wavesB == 1)
		{
			bw.update(g2d);
			player.checkBoss(b);
		}
	}
		catch(Exception e){}

	}

	public void checkifWon(ArrayList<Wave> waves)
	{
		int beaten = 0;
		for(Wave wa : waves)
		{
			if(wa.beat == true)
			{
				beaten++;
			}
		}
		if(beaten == 5)
		{
			wavesB = 1;
		}

	}


	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame("Space Adventure");
		MainGame game = new MainGame();
		frame.add(game);
		frame.setSize(600, 800);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		boolean start = true;
		while (true) {
			game.move();
			game.repaint();
			Thread.sleep(10);
		}
	}
}
