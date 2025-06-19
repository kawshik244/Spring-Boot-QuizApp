package com.kawshik.quizApp.service;

import com.kawshik.quizApp.dao.QuestionDao;
import com.kawshik.quizApp.dao.QuizDao;
import com.kawshik.quizApp.model.Question;
import com.kawshik.quizApp.model.QuestionWrapper;
import com.kawshik.quizApp.model.Quiz;
import com.kawshik.quizApp.model.UserResponse;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Question> questions = questionDao.findRandomQuestionByCategory(category,numQ);
        Quiz quiz= new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);

        quizDao.save(quiz);

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuiz(Integer id) {
        Optional<Quiz> quiz= quizDao.findById(id);
        List<Question> questionFromDB = quiz.get().getQuestions();
        List<QuestionWrapper> questionWrappers = new ArrayList<>();
        for (Question question : questionFromDB) {
            QuestionWrapper qw= new QuestionWrapper(question.getId(),question.getQuestionTitle(),question.getOption1(),question.getOption2(),question.getOption3(),question.getOption4());
            questionWrappers.add(qw);
        }
        return new ResponseEntity<>(questionWrappers,HttpStatus.OK);

    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<UserResponse> responses) {
        Optional<Quiz> quiz= quizDao.findById(id);
        List<Question> questionFromDB = quiz.get().getQuestions();
        int right=0;
        int i=0;
        for(UserResponse response : responses) {
            if(response.getUserresponse().equals(questionFromDB.get(i).getRightAnswer())){
                right++;
            }
            i++;
        }
        return new ResponseEntity<>(right,HttpStatus.OK);

    }
}
