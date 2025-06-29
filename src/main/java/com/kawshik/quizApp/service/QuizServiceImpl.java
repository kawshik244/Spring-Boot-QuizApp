package com.kawshik.quizApp.service;

import com.kawshik.quizApp.dao.QuestionDao;
import com.kawshik.quizApp.dao.QuizDao;
import com.kawshik.quizApp.model.Question;
import com.kawshik.quizApp.model.QuestionWrapper;
import com.kawshik.quizApp.model.Quiz;
import com.kawshik.quizApp.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {


    private final QuizDao quizDao;

    private final QuestionDao questionDao;

    public QuizServiceImpl(QuizDao quizDao, QuestionDao questionDao) {
        this.quizDao = quizDao;
        this.questionDao = questionDao;
    }

    @Override
    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Question> questions = questionDao.findAllByCategory(category);
        Collections.shuffle(questions);
        List<Question> quizQuestions = questions.stream().limit(numQ).collect(Collectors.toList());
        Quiz quiz= new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(quizQuestions);

        quizDao.save(quiz);

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @Override
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

    @Override
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
