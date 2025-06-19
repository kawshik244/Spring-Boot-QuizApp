package com.kawshik.quizApp.service;


import com.kawshik.quizApp.model.Question;
import com.kawshik.quizApp.dao.QuestionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;
    public ResponseEntity<List<Question>> getAllQuestions() {
        try{
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<List<Question>> getQuestionByCategory(String category) {
        try{
            return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionDao.save(question);
            return new ResponseEntity<>("Question added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ResponseEntity<>("Failed to add question", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public ResponseEntity<String> deleteQuestion(int id) {
        try {
            if(!questionDao.existsById(id)){
                return new ResponseEntity<>("Question Not Found",HttpStatus.NOT_FOUND);
            }

            questionDao.deleteById(id);
            return new ResponseEntity<>("Question deleted successfully", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ResponseEntity<>("Failed to delete question", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public  ResponseEntity<String> updateQuestion(Question question, int id) {
        try {
            if(!questionDao.existsById(id)){
                return new ResponseEntity<>("Question Not Found",HttpStatus.NOT_FOUND);
            }

            question.setId(id);
            questionDao.save(question);
            return new ResponseEntity<>("Question updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ResponseEntity<>("Failed to delete question", HttpStatus.INTERNAL_SERVER_ERROR);


    }
}
