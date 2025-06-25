package com.kawshik.quizApp.dao;


import com.kawshik.quizApp.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class QuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;


    @Test
    void shouldFindByCategory(){
        Question q1 = new Question();
        q1.setCategory("Programming");
        q1.setDifficultylevel("Easy");
        q1.setQuestionTitle("What is Java?");
        q1.setOption1("Language");
        q1.setOption2("Island");
        q1.setOption3("Coffee");
        q1.setOption4("Framework");
        q1.setRightAnswer(1);

        Question q2 = new Question();
        q2.setCategory("Programming");
        q2.setDifficultylevel("Medium");
        q2.setQuestionTitle("What is Spring?");
        q2.setOption1("Plant");
        q2.setOption2("Framework");
        q2.setOption3("Season");
        q2.setOption4("Tool");
        q2.setRightAnswer(2);

        questionDao.save(q1);
        questionDao.save(q2);


        List<Question> results = questionDao.findByCategory("Programming");


        assertEquals(2,results.size());
        assertEquals("Programming", results.get(0).getCategory());
        assertEquals("Programming", results.get(1).getCategory());

    }
}
