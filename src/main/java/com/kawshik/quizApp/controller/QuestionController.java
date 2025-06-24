package com.kawshik.quizApp.controller;


import com.kawshik.quizApp.dto.QuestionRequestDTO;
import com.kawshik.quizApp.dto.QuestionResponseDTO;
import com.kawshik.quizApp.model.Question;
import com.kawshik.quizApp.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {


    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("")
    public ResponseEntity<List<QuestionResponseDTO>> getAllQuestions(){
        return questionService.getAllQuestions();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionByCategory(@PathVariable String category){
        return questionService.getQuestionByCategory(category);
    }

    @PostMapping("")
    public ResponseEntity<QuestionResponseDTO> addQuestion(@Valid @RequestBody QuestionRequestDTO dto){
        return questionService.addQuestion(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable int id){
        return questionService.deleteQuestion(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateQuestion(@Valid @RequestBody QuestionRequestDTO dto, @PathVariable int id){
        return questionService.updateQuestion(dto,id);
    }

}
