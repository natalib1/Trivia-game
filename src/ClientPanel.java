/*  trivia
 *  the panel of the game
 *  Natali Boniel, 201122140 */

package Q1;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

public class ClientPanel extends JPanel{

	private JLabel score = new JLabel("0", SwingConstants.CENTER);
	private JLabel timer = new JLabel("10", SwingConstants.CENTER);
	private JButton b_end_game = new JButton("End Game");
	private Downcount count;

	public ClientPanel() 
	{
		super();
		this.setLayout(new GridLayout(1, 3));
		this.add(timer);
		this.add(score);
		this.add(b_end_game);
	}

	public void startTimer() 
	{
		if (count != null)
			count.cancel(true);
		count = new Downcount(timer);
		count.execute();
	}

	public void stopTimer() 
	{
		if (count != null)
			count.cancel(true);
	}

	public void changeScore(int change) 
	{
		int score = Integer.parseInt(this.score.getText());
		this.score.setText((score + change) + "");
	}

	public String getScore() 
	{
		return score.getText();
	}

	public JButton getEndGame() 
	{
		return b_end_game;
	}

	private class Downcount extends SwingWorker<Integer, Integer> {
		final JLabel count;
		
		public Downcount(JLabel count) 
		{
			this.count = count;
		}
	
		protected Integer doInBackground() throws Exception {
			for (int i = 10; i >= 0 && !isCancelled(); i--) 
			{
				this.count.setText(i + "");
				try 
				{
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
			return 0;
		}
	}
}
