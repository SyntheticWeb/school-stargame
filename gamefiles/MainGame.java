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
	Player player = new Player(this);// The controllable player

	ArrayList<Wave> w = new ArrayList<>(); // Contains all the waves for this game

	Boss b = new Boss(100,100,this,player);// The boss the player will face

//----Creates each wave for the game, including the boss wave----
	BossWave bw = new BossWave(false,b,this,player);
	Wave w1 = new Wave(1,3,player,this,true);//w1 = wave 1, etc.
	Wave w2 = new Wave(2,5,player,this,false);
	Wave w3 = new Wave(3,7,player,this,false);
	Wave w4 = new Wave(4,9,player,this,false);
	Wave w5 = new Wave(5,9,player,this,false);
//----------------------------------------------------------------


	int wavesB = 0;//waveB stands is a switch for deciding whether or not the player has beaten enough waves to fight the final boss


//The main game class
	public MainGame()
	{

//------------Listens for a KeyEvent, then sends it to the player class to be handled--------
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
//--------------------------------------------------------------------------------------------

//-----Adds the waves to the waves arraylist---
		w.add(w1);
		w.add(w2);
		w.add(w3);
		w.add(w4);
		w.add(w5);
//----------------------------------------------

		player.shoot();//This is here to help start the game, as for some reason nothing will draw unless the player has shot once, weird bug that i have no idea why it happens!

		//This option pane includes the instructions and welcome message when the game begins.
		JOptionPane.showMessageDialog(this, "Space Adventure is a game where you have to destroy the enemy ships!\n  Killing all enemies will spawn the next wave.  There are five waves with\n a final boss at the end! \n\nCONTROLS:\nShoot-Space\nMove-Arrow Keys", "Space Adventure", JOptionPane.INFORMATION_MESSAGE);
	}

	//Updates the players movement (Should eventually try and move this to a players update method)
	public void move()
	{
		player.move();
	}

//-----Message run when the player collides with an enemies bullet-------------------------------------
	public void gameOver()
	{

			JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.YES_NO_OPTION);
			System.exit(ABORT);
	}
//------------------------------------------------------------------------------------------------------

//-----Message run when the player kills the final boss, and has beaten every wave-------------------------------------
	public void win()
	{
		JOptionPane.showMessageDialog(this,"YOU HAVE WON","You have beaten Space Adventure",JOptionPane.YES_NO_OPTION);
		System.exit(ABORT);
	}
//---------------------------------------------------------------------------------------------------------------------

//---Paint method for this java application----
	public void paint(Graphics g) {

		try{//I use this try catch block b/c it keeps giving me repaint errors in the console
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;//Use a graphics 2d object instead
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);//Use anti alias to make it seem less choppy
		g2d.setColor(Color.black);//Set the background color of the screen
		g2d.fillRect(0,0,600,800);//Draw the background
		player.paint(g2d);//draw the player

		//This condition block decides whether or not to spawn the final boss, if all waves are beaten wavesB = 1, which activates the bass
		if(wavesB == 0)
		{
		//Goes through and updates the neccesary waves in the game
		for(int i = 0; i < 5;i++)
		{
			player.checkCollision(w.get(i));//Detects if the player has been hit by anybullets in this wave
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
		checkifWon(w);//Decides wheter or not wavesB but now = 1
	}
		else if(!bw.done && wavesB == 1)
		{
			bw.update(g2d);
			player.checkBoss(b);
		}
	}
		catch(Exception e){}

	}
//-------------------------------------------------------------------------------------------


//---This method checks if the wave has been beaten, if so it adds when to beaten, when beaten equals 5, wavesB becomes 1 and the boss wave begins---
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
//------------------------------------------------------------------------


//Main method for this game
	public static void main(String[] args) throws InterruptedException {
		//Sets up the java frame and creates a new MainGame class
		JFrame frame = new JFrame("Space Adventure");
		MainGame game = new MainGame();
		frame.add(game);
		frame.setSize(600, 800);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);//Sets the games location when it starts to the center of the screen
		//Main Game Loop
		while (true) {
			game.move();
			game.repaint();
			Thread.sleep(10);
		}

	}
}
