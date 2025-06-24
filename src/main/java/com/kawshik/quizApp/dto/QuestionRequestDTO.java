package com.kawshik.quizApp.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionRequestDTO {


    @NotBlank
    private String category;
    @NotBlank
    private String difficultylevel;
    @NotBlank
    private String questionTitle;
    @NotBlank
    private String option1;

    @NotBlank
    private String option2;

    @NotBlank
    private String option3;

    @NotBlank
    private String option4;

    @NotNull
    private int rightAnswer;


}
