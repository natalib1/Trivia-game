/*  trivia
 *  import the question from the file
 *  Natali Boniel, 201122140 */

package Q1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionFromFile {

	private List<Question> questions = new ArrayList<Question>();
	private boolean timeOut = false;

	public QuestionFromFile() 
	{
		loadFromFile();
	}

	//random question
	public Question chooseQuestion() 
	{
		if (questions.size() == 0)
			loadFromFile();
		int index = new Random().nextInt(questions.size());
		Question q = questions.remove(index);
		return q;
	}

	public boolean loadFromFile() 
	{
		try 
		{
			File file = new File(System.getProperty("user.dir") + "\\questions.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			try 
			{
				String line = br.readLine();
				while (line != null) 
				{
					if (line.length() > 0 && line.charAt(0) != '/')
						try 
						{
							String[] value = line.split(";");
							String[] answers = new String[4];
							for (int i = 0; i < 4; i++) 
							{
								answers[i] = value[i + 1];
							}
							Question q = new Question(value[0], answers, Integer.parseInt(value[5]));
							this.questions.add(q);
						} catch (Exception e) {
							System.err.println("Cant read line " + e);
						}
					line = br.readLine();
				}
			} finally {
				br.close();
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean isTimeOut() 
	{
		return timeOut;
	}

	public void setTimeOut(boolean timeOut) 
	{
		this.timeOut = timeOut;
	}
	
}
