package com.kawshik.quizApp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity

public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private String category;
    private String difficultylevel;
    private String questionTitle;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int rightAnswer;
}
