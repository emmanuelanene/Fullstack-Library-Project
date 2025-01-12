package com.luv2code.springbootlibrary.responsemodels;

import com.luv2code.springbootlibrary.entity.Book;
import lombok.Data;


/*
    RESPONSE MODELS
    The response models are the data structures used to send information back to the frontend. They organize data in a way that the frontend can understand and display.

    ShelfCurrentLoansResponse: This model is used to send information about books currently on loan to the frontend. It includes:
    Book book: The specific book that’s on loan.
    int daysLeft: The number of days remaining for the loan.

    By splitting request and response models, you keep input (request) and output (response) clearly separated. This makes it easier to manage and modify the data structure on either side without affecting the other.
 */
@Data
public class ShelfCurrentLoansResponse {

    public ShelfCurrentLoansResponse(Book book, int daysLeft) {
        this.book = book;
        this.daysLeft = daysLeft;
    }

    private Book book;

    private int daysLeft;
}
