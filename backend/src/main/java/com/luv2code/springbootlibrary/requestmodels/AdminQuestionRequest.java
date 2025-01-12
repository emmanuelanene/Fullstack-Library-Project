package com.luv2code.springbootlibrary.requestmodels;

import lombok.Data;

@Data
public class AdminQuestionRequest {
    // So here w will enter the ID of the message the Admin wants to respond to and then enter the resonse by the message the Admin
    private Long id;

    private String response;
}
