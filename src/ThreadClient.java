/*  trivia
 *  client Class 
 *  Natali Boniel, 201122140 */

package Q1;

import java.net.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;

public class ThreadClient extends Thread{

	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private JFrame frame = new JFrame("TRIVIA");
	private JTextArea t1 = new JTextArea(20, 70);
	private ClientPanel cp;
	private JPanel p1;
	private JButton b1,b2,b3,b4;
	private boolean end_game = false;

	public enum SERVER_STATUS {
		Success, Failure, TimedOut;
	}

	public ThreadClient() 
	{
		t1.setEditable(false);
		cp = new ClientPanel();
		p1 = new JPanel();
		b1 = new JButton("1");
		b2 = new JButton("2");
		b3 = new JButton("3");
		b4 = new JButton("4");
		p1.add(b1);
		p1.add(b2);
		p1.add(b3);
		p1.add(b4);
		frame.add(cp, BorderLayout.NORTH);
		frame.add(t1, BorderLayout.CENTER);
		frame.add(p1, BorderLayout.SOUTH);
		
		cp.getEndGame().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				end_game = true;
				cp.stopTimer();
				t1.setText("Your score is: " + cp.getScore());
				try 
				{
					out.writeInt(0);
					out.flush();
				} catch (Exception e1) {}
			}
		});
		
		b1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!end_game) 
					{
						try 
						{
							out.writeInt(1);
							out.flush();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!end_game) 
				{
					try 
					{
						out.writeInt(2);
						out.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		b3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!end_game) 
				{
					try 
					{
						out.writeInt(3);
						out.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		b4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!end_game) 
				{
					try 
					{
						out.writeInt(4);
						out.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	public void run() 
	{
		System.out.println("Starting client");
		while (!end_game) 
			startClientListening();
	}

	public void connectToServer() throws IOException 
	{
		String host = "localhost";

		Socket socket = new Socket(host, 6666);

		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		this.start();
	}

	private int waitForServerResponse() 
	{
		try 
		{
			while (in.available() == 0 && !end_game) 
			{
				Thread.sleep(300);
			}
			if (!end_game) 
			{
				int ans = in.readInt();
				if (ans == 0) //correct answer send
					cp.changeScore(10);
				if(ans == 2)//answer not send
					cp.changeScore(-5);
				return ans;
			}
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e){
			e.printStackTrace();
		}
		return -1;
	}

	private Question waitForQestion() {
		Question response = null;
		try 
		{
			response = (Question) in.readObject();
			if (response == null) 
				System.exit(0);
		} catch (IOException e1) {
		} catch (ClassNotFoundException e1) {
		}
		return response;
	}

	private int startClientListening() {
		Question q = waitForQestion();
		t1.setText(q.toString());
		cp.startTimer();
		return waitForServerResponse();
	}

	public static void main(String[] args) throws Exception {
		ThreadClient client = new ThreadClient();
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.pack();
		client.frame.setVisible(true);
		client.connectToServer();
	}
}

