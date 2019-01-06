/*  trivia
 *  server Class 
 *  Natali Boniel, 201122140 */

package Q1;

import java.net.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.Timer;
import Q1.ThreadClient.SERVER_STATUS;

public class MultiServer {

	public static void main(String[] args) throws IOException
	{
		ServerSocket serverSocket = null; 
		boolean listening = true;
		int clientNumber = 0;
		try
		{
			serverSocket = new ServerSocket(6666);
		} catch (IOException e){
			System.err.println("Could not listen on port: 3333");
			System.exit(1);
		}
		
		System.out.println("server is ready");
		Socket socket = null;
	
		while (listening)
		{
			try
			{
				socket = serverSocket.accept();
				new EchoThread(socket, clientNumber++).start();
			}catch (IOException e){
				System.err.println("Accept failed");
				System.exit(1);
			}
			
		}
		serverSocket.close();
	}
	
	private static class EchoThread extends Thread {
		private Socket socket;
		private int clientNumber;
		private ObjectOutputStream out;
		private ObjectInputStream in;
		
		public EchoThread(Socket socket, int clientNumber) 
		{
			try
			{
				this.socket = socket;
				this.clientNumber = clientNumber;
				System.out.println("New connection with client# " + clientNumber + " at " + socket);
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
			}catch (IOException e){
				System.out.println("Could not open I/O on connection");
			}
		}
		
		public void run() 
		{
			try 
			{
				boolean endOfGame = false;
				final QuestionFromFile qff = new QuestionFromFile();

				while (!endOfGame) 
				{
					Question q = qff.chooseQuestion();
					out.writeObject(q);
					
					Timer timer = new Timer(10000, new ActionListener(){
						public void actionPerformed(ActionEvent e) 
						{
							qff.setTimeOut(true);
							try 
							{
								out.write(SERVER_STATUS.TimedOut.ordinal());
								out.flush();
							} catch (Exception e1) {}
						}
					});
					
					timer.start();

					while ((in.read() == 0) && (!qff.isTimeOut()) && (!endOfGame)) 
					{
						Thread.sleep(300);
					}
					if (!endOfGame) 
					{
						if (!qff.isTimeOut()) 
						{
							timer.stop();
							int answer = in.read();
							if (answer == 0)
								endOfGame = true;
							else if (q.getcorrectAnswer() == answer)
								out.write(SERVER_STATUS.Success.ordinal());
							else
								out.write(SERVER_STATUS.Failure.ordinal());
							out.flush();
						} 
						else 
						{
							qff.setTimeOut(false);
						}
					}
					timer.stop();
				}
			}catch (IOException e) {
				System.err.println("Error with client no." + clientNumber + ": " + e);
			}catch (InterruptedException e) {
				System.err.println("Error with client no." + clientNumber + ": " + e);
			}finally {
				try 
				{
					socket.close();
				} catch (IOException e) {
					System.err.println("Error with closing socket");
				}
				System.err.println("Connection with client no." + clientNumber + " is close");
			}
		}
	}
}