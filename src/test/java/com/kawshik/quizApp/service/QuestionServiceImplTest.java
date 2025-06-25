package com.kawshik.quizApp.service;


import com.kawshik.quizApp.dao.QuestionDao;
import com.kawshik.quizApp.dto.QuestionRequestDTO;
import com.kawshik.quizApp.dto.QuestionResponseDTO;
import com.kawshik.quizApp.exception.ResourceNotFoundException;
import com.kawshik.quizApp.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceImplTest {
    
    @Mock
    private QuestionDao questionDao;
    @InjectMocks
    private QuestionServiceImpl questionServiceImpl;

    private Question testQuestion;
    private QuestionResponseDTO testResponseDTO;
    private QuestionRequestDTO testRequestDTO;

    @BeforeEach
    void setup()
    {
        testQuestion = new Question();
        testQuestion.setId(1);
        testQuestion.setCategory("Java");
        testQuestion.setDifficultylevel("Easy");
        testQuestion.setQuestionTitle("What is Java?");
        testQuestion.setOption1("Programming Language");
        testQuestion.setOption2("Coffee");
        testQuestion.setOption3("Island");
        testQuestion.setOption4("Framework");
        testQuestion.setRightAnswer(1);

        testRequestDTO = new QuestionRequestDTO();
        testRequestDTO.setCategory("Java");
        testRequestDTO.setDifficultylevel("Easy");
        testRequestDTO.setQuestionTitle("What is Java?");
        testRequestDTO.setOption1("Programming Language");
        testRequestDTO.setOption2("Coffee");
        testRequestDTO.setOption3("Island");
        testRequestDTO.setOption4("Framework");
        testRequestDTO.setRightAnswer(1);

        testResponseDTO = new QuestionResponseDTO();
        testResponseDTO.setCategory("Java");
        testResponseDTO.setQuestionTitle("What is Java?");
        testResponseDTO.setOption1("Programming Language");
        testResponseDTO.setOption2("Coffee");
        testResponseDTO.setOption3("Island");
        testResponseDTO.setOption4("Framework");


    }

    @Test
    void getAllQuestions_ShouldReturnListOfQuestions_WhenQuestionsExist(){
            List<Question> questions= Arrays.asList(testQuestion);

            when(questionDao.findAll()).thenReturn(questions);

            ResponseEntity<List<QuestionResponseDTO>> response = questionServiceImpl.getAllQuestions();

            assertEquals(HttpStatus.OK,response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1,response.getBody().size());
            assertEquals("Java", response.getBody().get(0).getCategory());
            assertEquals("What is Java?",response.getBody().get(0).getQuestionTitle());
            assertEquals("Programming Language",response.getBody().get(0).getOption1());
            assertEquals("Coffee",response.getBody().get(0).getOption2());
            assertEquals("Island",response.getBody().get(0).getOption3());
            assertEquals("Framework",response.getBody().get(0).getOption4());

            verify(questionDao, times(1)).findAll();

    }

    @Test
    void getAllQuestions_ShouldReturnEmptyList_WhenNoQuestionsExist() {

        when(questionDao.findAll()).thenReturn(Collections.emptyList());


        ResponseEntity<List<QuestionResponseDTO>> response = questionServiceImpl.getAllQuestions();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(questionDao, times(1)).findAll();
    }

    @Test
    void getQuestionByCategory_ShouldReturnQuestions_WhenCategoryExists() {

        String category = "Java";
        List<Question> questions = Arrays.asList(testQuestion);
        when(questionDao.findByCategory(category)).thenReturn(questions);


        ResponseEntity<List<QuestionResponseDTO>> response = questionServiceImpl.getQuestionByCategory(category);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(category, response.getBody().get(0).getCategory());
        verify(questionDao, times(1)).findByCategory(category);
    }
    @Test
    void getQuestionByCategory_ShouldThrowException_WhenCategoryIsNull() {

        assertThrows(IllegalArgumentException.class,
                () -> questionServiceImpl.getQuestionByCategory(null));


        verify(questionDao, never()).findByCategory(any());
    }

    @Test
    void getQuestionByCategory_ShouldThrowException_WhenCategoryIsEmpty() {

        assertThrows(IllegalArgumentException.class,
                () -> questionServiceImpl.getQuestionByCategory(""));
        verify(questionDao, never()).findByCategory(any());
    }

    @Test
    void addQuestion_ShouldCreateQuestion_WhenValidDataProvided() {

        when(questionDao.save(any(Question.class))).thenReturn(testQuestion);


        ResponseEntity<QuestionResponseDTO> response = questionServiceImpl.addQuestion(testRequestDTO);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Java", response.getBody().getCategory());
        verify(questionDao, times(1)).save(any(Question.class));
    }

    @Test
    void deleteQuestion_ShouldDeleteQuestion_WhenQuestionExists() {

        int questionId = 1;
        when(questionDao.existsById(questionId)).thenReturn(true);
        doNothing().when(questionDao).deleteById(questionId);

        // Act
        ResponseEntity<String> response = questionServiceImpl.deleteQuestion(questionId);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Question deleted successfully", response.getBody());
        verify(questionDao, times(1)).existsById(questionId);
        verify(questionDao, times(1)).deleteById(questionId);
    }

    @Test
    void deleteQuestion_ShouldThrowException_WhenQuestionNotFound() {
        // Arrange
        int questionId = 999;
        when(questionDao.existsById(questionId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> questionServiceImpl.deleteQuestion(questionId));
        verify(questionDao, times(1)).existsById(questionId);
        verify(questionDao, never()).deleteById(any());
    }

}
