package com.kawshik.quizApp.service;


import com.kawshik.quizApp.dto.QuestionRequestDTO;
import com.kawshik.quizApp.dto.QuestionResponseDTO;
import com.kawshik.quizApp.exception.ResourceNotFoundException;
import com.kawshik.quizApp.model.Question;
import com.kawshik.quizApp.dao.QuestionDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;

import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);
    private final QuestionDao questionDao;

    public QuestionServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    private Question mapToEntity(QuestionRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("QuestionRequestDTO cannot be null");
        }

        Question q = new Question();
        q.setCategory(dto.getCategory());
        q.setDifficultylevel(dto.getDifficultylevel());
        q.setQuestionTitle(dto.getQuestionTitle());
        q.setOption1(dto.getOption1());
        q.setOption2(dto.getOption2());
        q.setOption3(dto.getOption3());
        q.setOption4(dto.getOption4());
        q.setRightAnswer(dto.getRightAnswer());

        return q;
    }

    private QuestionResponseDTO mapToResponse(Question question) {

        if (question == null) {
            throw new IllegalArgumentException("Question entity cannot be null");
        }
        QuestionResponseDTO dto = new QuestionResponseDTO();

        dto.setCategory(question.getCategory());
        dto.setQuestionTitle(question.getQuestionTitle());
        dto.setOption1(question.getOption1());
        dto.setOption2(question.getOption2());
        dto.setOption3(question.getOption3());
        dto.setOption4(question.getOption4());
        return dto;
    }

    @Override
    public ResponseEntity<List<QuestionResponseDTO>> getAllQuestions() {
        List<Question> questions = questionDao.findAll();
        if (questions.isEmpty()) {
            logger.warn("No questions found in the database");
        }

        List<QuestionResponseDTO> questionList = questions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(questionList, HttpStatus.OK);

    }
    @Override
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionByCategory(String category) {

        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }


        List<Question> questions = questionDao.findByCategory(category);
        if (questions.isEmpty()) {
            logger.warn("No questions found for category: {}", category);
        }

        List<QuestionResponseDTO> questionList = questions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new ResponseEntity<>(questionList, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<QuestionResponseDTO> addQuestion(QuestionRequestDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("Question data cannot be null");
        }
        Question question = mapToEntity(dto);
        Question savedQuestion = questionDao.save(question);
        return new ResponseEntity<>(mapToResponse(savedQuestion), HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<String> deleteQuestion(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Question ID must be positive");
        }
        if (!questionDao.existsById(id)) {
            throw  new  ResourceNotFoundException("Question not found with id: "+ id);
        }
        questionDao.deleteById(id);
        return new ResponseEntity<>("Question deleted successfully", HttpStatus.ACCEPTED);

    }
    @Override
    public ResponseEntity<String> updateQuestion(QuestionRequestDTO dto, int id) {
        if (dto == null) {
            throw new IllegalArgumentException("Question data cannot be null");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Question ID must be positive");
        }
        Question q = questionDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question Not Found with id " + id));
        q.setCategory(dto.getCategory());
        q.setDifficultylevel(dto.getDifficultylevel());
        q.setQuestionTitle(dto.getQuestionTitle());
        q.setOption1(dto.getOption1());
        q.setOption2(dto.getOption2());
        q.setOption3(dto.getOption3());
        q.setOption4(dto.getOption4());
        q.setRightAnswer(dto.getRightAnswer());

        questionDao.save(q);
        return new ResponseEntity<>("Question updated successfully", HttpStatus.OK);
    }

}
