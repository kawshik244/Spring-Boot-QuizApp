package com.kawshik.quizApp.dto;


import lombok.Data;

@Data
public class QuestionResponseDTO {

    private String category;
    private String questionTitle;
    private String option1;
    private String option2;
    private String option3;
    private String option4;

}
