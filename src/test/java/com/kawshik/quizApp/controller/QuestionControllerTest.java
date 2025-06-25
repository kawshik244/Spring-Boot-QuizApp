package com.kawshik.quizApp.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kawshik.quizApp.dto.QuestionRequestDTO;
import com.kawshik.quizApp.dto.QuestionResponseDTO;
import com.kawshik.quizApp.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    private QuestionRequestDTO questionRequestDTO;
    private QuestionResponseDTO questionResponseDTO;
    private List<QuestionResponseDTO> questionList;

    @BeforeEach
    void setUp() {
        questionRequestDTO = new QuestionRequestDTO();
        questionRequestDTO.setQuestionTitle("What is Java?");
        questionRequestDTO.setCategory("Programming");
        questionRequestDTO.setDifficultylevel("Easy");
        questionRequestDTO.setOption1("A programming language");
        questionRequestDTO.setOption2("An island");
        questionRequestDTO.setOption3("A coffee");
        questionRequestDTO.setOption4("A framework");
        questionRequestDTO.setRightAnswer(1);

        questionResponseDTO = new QuestionResponseDTO();
        questionResponseDTO.setQuestionTitle("What is Java?");
        questionResponseDTO.setCategory("Programming");
        questionResponseDTO.setOption1("A programming language");
        questionResponseDTO.setOption2("An island");
        questionResponseDTO.setOption3("A coffee");
        questionResponseDTO.setOption4("A framework");


        QuestionResponseDTO question2 = new QuestionResponseDTO();
        question2.setQuestionTitle("What is Spring Boot?");
        question2.setCategory("Framework");
        question2.setOption1("A web framework");
        question2.setOption2("A shoe");
        question2.setOption3("A season");
        question2.setOption4("A database");

        questionList = Arrays.asList(questionResponseDTO, question2);

    }

    @Test
    void shouldReturnAllQuestionsSuccessfully() throws Exception {
        when(questionService.getAllQuestions())
                .thenReturn(new ResponseEntity<>(questionList, HttpStatus.OK));

        mockMvc.perform(get("/api/v1/questions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].questionTitle").value("What is Java?"))
                .andExpect(jsonPath("$[0].category").value("Programming"))
                .andExpect(jsonPath("$[1].questionTitle").value("What is Spring Boot?"))
                .andExpect(jsonPath("$[1].category").value("Framework"));

        verify(questionService, times(1)).getAllQuestions();

    }

    @Test
    void shouldReturnQuestionByCategorySuccessfully() throws Exception {

        String category = "Programming";
        List<QuestionResponseDTO> programmingQuestions = Arrays.asList(questionResponseDTO);

        when(questionService.getQuestionByCategory(category))
                .thenReturn(new ResponseEntity<>(programmingQuestions, HttpStatus.OK));

        mockMvc.perform(get("/api/v1/questions/category/{category}", category))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].questionTitle").value("What is Java?"))
                .andExpect(jsonPath("$[0].category").value("Programming"));


        verify(questionService, times(1)).getQuestionByCategory(category);
    }

    @Test
    void shouldCreateQuestionSuccessfully() throws Exception {
        when(questionService.addQuestion(any(QuestionRequestDTO.class)))
                .thenReturn(new ResponseEntity<>(questionResponseDTO, HttpStatus.CREATED));

        mockMvc.perform(post("/api/v1/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionRequestDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.questionTitle").value("What is Java?"))
                .andExpect(jsonPath("$.category").value("Programming"))
                .andExpect(jsonPath("$.option1").value("A programming language"))
                .andExpect(jsonPath("$.option2").value("An island"))
                .andExpect(jsonPath("$.option3").value("A coffee"))
                .andExpect(jsonPath("$.option4").value("A framework"));
        verify(questionService, times(1)).addQuestion(questionRequestDTO);


    }

    @Test
    void shouldDeleteQuestionSuccessfully() throws Exception {
        int questionId = 1;
        String successMessage = "Question deleted successfully";

        when(questionService.deleteQuestion(questionId))
                .thenReturn(new ResponseEntity<>(successMessage, HttpStatus.OK));
        mockMvc.perform(delete("/api/v1/questions/{id}", questionId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));

        verify(questionService, times(1)).deleteQuestion(questionId);
    }

    @Test
    void shouldUpdateQuestionSuccessfully() throws Exception {

        int questionId = 1;
        String successMessage = "Question updated successfully";

        when(questionService.updateQuestion(any(QuestionRequestDTO.class), eq(questionId)))
                .thenReturn(new ResponseEntity<>(successMessage, HttpStatus.OK));


        mockMvc.perform(put("/api/v1/questions/{id}", questionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));

        // VERIFY
        verify(questionService, times(1)).updateQuestion(any(QuestionRequestDTO.class), eq(questionId));
    }

    @Test
    void shouldReturn400WhenCreatingQuestionWithInvalidData() throws Exception {
        questionRequestDTO.setQuestionTitle("");
        questionRequestDTO.setCategory("");

        mockMvc.perform(post("/api/v1/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionRequestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(questionService, never()).addQuestion(any(QuestionRequestDTO.class));
    }


}
