/*  trivia
 *  question Class 
 *  Natali Boniel, 201122140 */

package Q1;

import java.io.Serializable;

public class Question implements Serializable{

	private String question;
	private String[] answers = new String[4];
	private int correctAnswer;
	
	public Question(String question, String[] answers, int correctAnswer) {
		this.question = question;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
	}

	public String getQuestion() 
	{
		return question;
	}

	public void setQuestion(String question) 
	{
		this.question = question;
	}

	public String[] getanswers() 
	{
		return answers;
	}

	public void setanswers(String[] answers) 
	{
		this.answers = answers;
	}

	public int getcorrectAnswer() 
	{
		return correctAnswer;
	}

	public void setcorrectAnswer(int correctAnswer) 
	{
		this.correctAnswer = correctAnswer;
	}

	public String toString() 
	{
		String s = this.getQuestion();
		s += "\n";
		for (int i = 0; i < this.getanswers().length; i++) {
			s += (i + 1) + ". " + this.getanswers()[i] + "\n";
		}
		return s;
	}

}
