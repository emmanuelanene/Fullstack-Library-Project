package com.luv2code.springbootlibrary.controller;

import com.luv2code.springbootlibrary.entity.Book;
import com.luv2code.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import com.luv2code.springbootlibrary.service.BookService;
import com.luv2code.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(
            @RequestHeader(value = "Authorization") String token
    ) throws Exception {
        // "\"sub\"": This is the claim key to extract from the payload. The sub claim typically contains the subject of the token, which in this case is likely the user's email address.
        // JWTs often include claims like sub, iat, exp, etc. The sub claim usually stores the user’s unique identifier, such as their email.
        // After calling payloadJWTExtraction, the result (userEmail) will contain the email extracted from the token.
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return bookService.currentLoans(userEmail);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(
            @RequestHeader(value = "Authorization") String token
    ) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return bookService.currentLoansCount(userEmail);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean checkoutBookByUser(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam Long bookId
    ) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return bookService.checkoutBookByUser(userEmail, bookId);
    }

    // Add a book to be checked out by the user
    @PutMapping("/secure/checkout")
    public Book checkoutBook (
            @RequestHeader(value = "Authorization") String token,
            @RequestParam Long bookId
    ) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return bookService.checkoutBook(userEmail, bookId);
    }

    @PutMapping("/secure/return")
    public void returnBook(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam Long bookId
    ) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        bookService.returnBook(userEmail, bookId);
    }

    @PutMapping("/secure/renew/loan")
    public void renewLoan(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam Long bookId
    ) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        bookService.renewLoan(userEmail, bookId);
    }

}