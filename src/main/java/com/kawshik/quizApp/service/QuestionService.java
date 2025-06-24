package com.kawshik.quizApp.service;

import com.kawshik.quizApp.dto.QuestionRequestDTO;
import com.kawshik.quizApp.dto.QuestionResponseDTO;
import com.kawshik.quizApp.model.Question;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuestionService {

        ResponseEntity<List<QuestionResponseDTO>> getAllQuestions();
        ResponseEntity<List<QuestionResponseDTO>> getQuestionByCategory(String category);
        ResponseEntity<QuestionResponseDTO> addQuestion(QuestionRequestDTO dto);
        ResponseEntity<String> deleteQuestion(int id);
        ResponseEntity<String> updateQuestion(QuestionRequestDTO dto, int id);

}
