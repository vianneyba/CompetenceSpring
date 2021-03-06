package fr.dawan.autoquiz3000;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import fr.dawan.autoquiz3000.beans.Quiz;
import fr.dawan.autoquiz3000.beans.QuizQuestion;
import fr.dawan.autoquiz3000.beans.QuizResponse;
import fr.dawan.autoquiz3000.beans.QuizTest;
import fr.dawan.autoquiz3000.beans.User;
import fr.dawan.autoquiz3000.ctrl.CtrlQuizTest;
import fr.dawan.autoquiz3000.dao.QuizDao;
import fr.dawan.autoquiz3000.dao.QuizTestDao;
import fr.dawan.autoquiz3000.dao.QuizToDoDao;
import fr.dawan.autoquiz3000.dao.UserDao;
import fr.dawan.autoquiz3000.tools.StatStudentBo;

@Controller
@RequestMapping("/student")
public class StudentControler {
	
	@Autowired
	private QuizDao qDao;
	
	@Autowired
	private QuizTestDao qtDao;
	
	@Autowired
	private QuizToDoDao qtdDao; 
	
	@Autowired
	private UserDao uDao;

	@GetMapping("/")
	public ModelAndView getDashboard(HttpServletRequest request) {
		return new ModelAndView("student/home");
	}
	
	@GetMapping("/quiz-result")
	public ModelAndView getQuizResult() {
		return new ModelAndView("student/quiz_result");
	}
	
	@GetMapping("/list_quiz")
	public ModelAndView getListQuiz(HttpServletRequest request) {
		HttpSession session= request.getSession();
		User user= (User) session.getAttribute("user");
		List<Quiz> qizs= qtdDao.findByStudent(user.getId(), qDao);
		session.setAttribute("quizs", qizs);
		return new ModelAndView("student/list_quiz");
	}
	
	@GetMapping("/quiz_history")
	public ModelAndView getQuizHistory(HttpServletRequest request) {
		HttpSession session= request.getSession();
		User user= (User) session.getAttribute("user");
		
		session.setAttribute("user", uDao.findById(user.getId()));
		List<QuizTest> qizs= qtDao.findByStudent(user.getId());
		session.setAttribute("hQuizs", qizs);
		
		new StatStudentBo(qizs, request);		
		
		return new ModelAndView("student/list_quiz_history");
	}
	
	@GetMapping("/quiz/history/{id}")
	public ModelAndView getResultQuiz(@PathVariable(value="id") long id, Model model, HttpServletRequest request) {
		HttpSession session= request.getSession();
		User user= (User) session.getAttribute("user");
		
		Quiz quiz= qDao.findById(id);
		QuizTest quizTest= qtDao.findByIdQuizAndIdUser(quiz.getId(), user.getId());
		
		session.setAttribute("quiz", quiz);
		session.setAttribute("quizTest", quizTest);
		return new ModelAndView("redirect:/student/quiz-result");
	}
	
	@GetMapping("/quiz/{id}")
	public ModelAndView getQuiz(@PathVariable(value="id") String id, Model model) {
		try {
			model.addAttribute("quiz", qDao.findById(Long.valueOf(id)));
			return new ModelAndView("/student/viewQuiz");
		} catch (Exception e) {
			// TODO: erreur 404 ou autre!
		}
		return new ModelAndView("fourOFour");
	}
	
	private ModelAndView displayQuestion(long quizId, int orderNum, Model model) {
		QuizQuestion currentQst = qDao.findQuestion(quizId, orderNum);
		model.addAttribute("currentQst",currentQst);
		
		List<QuizResponse> responses = qDao.findReponses(currentQst.getId());
		model.addAttribute("responses", responses);

		return new ModelAndView("/student/viewQuiz");
	}
	
	/* Déroulement d'un quiz */
	@GetMapping("/quiz/{id}/start")
	public ModelAndView startQuiz(@PathVariable(value="id") String id, Model model, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			Quiz quiz = qDao.findById(Long.valueOf(id));
			
			session.setAttribute("quiz", quiz);
			QuizTest quizTest= new QuizTest();
			quizTest.setQuiz(quiz);
			quizTest.setStartDate(new Date());
			quizTest.setUser((User)session.getAttribute("user"));
			
			session.setAttribute("quizName", quiz.getName());
			session.setAttribute("quizId", quiz.getId());
			session.setAttribute("quizTest", quizTest);
			
			session.setAttribute("orderNum", 1);
			session.setAttribute("score", 0);
			session.setAttribute("qstNb", qDao.nbQuestions(quiz.getId()));
			return displayQuestion(quiz.getId(), 1, model);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: erreur 404 ou autre!
		}
		// TODO erreur 404
		return new ModelAndView("fourOFour");
	}
	
	@GetMapping("/quiz/next-question")
	public ModelAndView NextQuestionInQuiz(Model model, HttpServletRequest request) {
		String[] responseStudent= request.getParameterValues("response");
		HttpSession session = request.getSession();
		QuizTest quizTest = (QuizTest)session.getAttribute("quizTest");
		Quiz quiz= (Quiz)session.getAttribute("quiz");
		int orderNum = (Integer)session.getAttribute("orderNum");
		long quizId = (Long)session.getAttribute("quizId");
		long qstNb = (Long)session.getAttribute("qstNb");
		int score = (Integer)session.getAttribute("score");
		
		CtrlQuizTest ctrl= new CtrlQuizTest(qDao, quizTest);
		ctrl.ctrlResponse(responseStudent, orderNum, quizId);
		ctrl.save();
		if(orderNum<qstNb) {
			orderNum++;
			session.setAttribute("orderNum", orderNum);
			return displayQuestion(quizId, orderNum, model);
		}else {
			User user= (User) session.getAttribute("user");
			score= (quizTest.getCorrectResponse()*100)/(quizTest.getCorrectResponse()+quizTest.getErrorResponse());
			quizTest.setScore(score);
			
			if (quiz.getScoreToAcquireSkill() <= quizTest.getScore()) {
				user.getSkills().add(quiz.getSkill());
				uDao.save(user);
			}
			qtDao.save(quizTest);
			qtdDao.DeleteQuizByQuizAndStudent(user.getId(), quiz.getId());
			return new ModelAndView("redirect:/student/quiz-result");
		}
	}
}
