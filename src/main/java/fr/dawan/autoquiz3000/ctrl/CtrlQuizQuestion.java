package fr.dawan.autoquiz3000.ctrl;

import javax.servlet.http.HttpServletRequest;

import fr.dawan.autoquiz3000.beans.Quiz;
import fr.dawan.autoquiz3000.beans.QuizQuestion;
import fr.dawan.autoquiz3000.beans.QuizResponse;

public class CtrlQuizQuestion extends Ctrl {
	private final int LENGTH_TITLE_QUESTION_MIN= 10;
	private final int LENGTH_TITLE_QUESTION_MAX= 3000;
	
	private QuizQuestion question;
	private Quiz quiz;
	private int nbrChoix;
	private String msgTitleQuestion;
	private String msgQuestion;
	private String msgTiltleResponse;

	public CtrlQuizQuestion(HttpServletRequest request, Quiz quiz) {
		question= new QuizQuestion();
		this.quiz= quiz;
		ctrlTitleQuestion(request.getParameter("titleQuestion"));
		question.setText(request.getParameter("titleQuestion"));
		ctrlChoix(request);
		int nbQestion= quiz.countQuestion();
		question.setOrderNum(nbQestion+1);
		if(!error) {
			this.quiz.setQuizQuestion(question);
		}
	}
	
	private void ctrlChoix(HttpServletRequest request) {
		boolean goodResponse= false;
		int nbResponse= 0;
		
		ctrlNbrChoix(request.getParameter("nbrChoix"));
		if(!error) {
			for (int i = 1; i < nbrChoix+1; i++) {
				QuizResponse res= new QuizResponse();
				String titleResponse= request.getParameter("choix"+i);
				if(titleResponse.length() < 1 || titleResponse.length() > 100 ) {
					error= true;
					msgTiltleResponse= "une réponse ne peut être vide!";
				} else {
					System.out.println("============== titleResponse.length()= "+titleResponse);
					res.setText(request.getParameter("choix"+i));
					nbResponse++;
				}
	
				if(request.getParameter("checkChoix"+i) != null) {
					res.setCorrect(true);
					goodResponse= true;
				} else {
					res.setCorrect(false);
				}
				question.setQuizResponse(res);
			} 
			if (nbResponse  < 2) {
				msgTiltleResponse= "il ne peut pas y avoir qu'une réponse!";
				error= true;
			}
		}
		
		if (!goodResponse) {
			msgQuestion= "il doit avoir une bonne reponse dans le questionnaire!";
			error= true;
		}
	}
	
	private void ctrlNbrChoix(String nbr) {
		try {
			nbrChoix= Integer.valueOf(nbr);
		} catch (Exception e) {
			error= true;
		}
	}
	
	private void ctrlTitleQuestion(String titleQuestion) {
		if (titleQuestion.length() < LENGTH_TITLE_QUESTION_MIN || titleQuestion.length() > LENGTH_TITLE_QUESTION_MAX) {
			msgTitleQuestion= "doit avoir entre "+LENGTH_TITLE_QUESTION_MIN+" et "+LENGTH_TITLE_QUESTION_MAX+" caractéres";
			error= true;
		}
	}

	//********************Getters / Setters******************
	public Quiz getQuiz() {
		return quiz;
	}
	public QuizQuestion getQuestion() {
		return question;
	}
	public int getNbrChoix() {
		return nbrChoix;
	}
	public String getMsgTitleQuestion() {
		return msgTitleQuestion;
	}
	public String getMsgQuestion() {
		return msgQuestion;
	}
	public String getMsgTiltleResponse() {
		return msgTiltleResponse;
	}

	//********************Override***************************
	@Override
	public String toString() {
		return "CtrlQuizQuestion [msgTitleQuestion=" + msgTitleQuestion + ", msgQuestion=" + msgQuestion + ", isError()=" + isError()
				+ "]";
	}
}
