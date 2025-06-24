package com.kawshik.quizApp.service;

import com.kawshik.quizApp.model.QuestionWrapper;
import com.kawshik.quizApp.model.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuizService {
    ResponseEntity<String> createQuiz(String category, int numQ, String title);
    ResponseEntity<List<QuestionWrapper>> getQuiz(Integer id);
    ResponseEntity<Integer> calculateResult(Integer id, List<UserResponse> responses);
}
