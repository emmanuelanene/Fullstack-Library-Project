package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.dao.BookRepository;
import com.luv2code.springbootlibrary.dao.CheckoutRepository;
import com.luv2code.springbootlibrary.dao.ReviewRepository;
import com.luv2code.springbootlibrary.entity.Book;
import com.luv2code.springbootlibrary.requestmodels.AddBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*
    NOTICE HOW THE "SEVICE" PAGE IS using the "Repository, Entity and Models" together?? Its insane

    MODELS: Models, like ReviewRequest, are data holders for the information that comes from the user input (or other layers). They represent the data passed to or from the client (e.g., frontend, API requests) but aren’t directly tied to the database.

    ENTITY: Entities are like blueprints for your database tables. They represent the actual data you’re working with in the database. In your case, the Review entity is the representation of a review in the database.

    REPOSITORIES: Repositories are like messengers that handle communication between your service and the database. In your case, you have the ReviewRepository, which helps the service talk to the database to fetch and save review data.
 */

/*
    @Service - tells Spring boot that this class is a "Service" meaning that it'll handle business logics (e.g. posting data, reviewing data, saving data, etc.)

    @Transactional - tells Spring that if any errors occur while updating or deleting data in the DB, the error/change should be rolled back/reversed to prevent the DB from being left in a broken state.
 */
@Service
@Transactional
public class AdminService {
    // These are repositories ( BookRepository, ReviewRepository, CheckoutRepository ) that allow this service (AdminService) to interact (i.e. make changes to values in the DB) with the Book, Review, and Checkout tables in the database. The service will use these to perform actions like finding, saving, or deleting records.
    // the DAO REPOSITORY contains the methods that we need to allow SERVICE make edits to ENTITY
    private BookRepository bookRepository;
    private ReviewRepository reviewRepository;
    private CheckoutRepository checkoutRepository;


    /*
        @Autowired injects the needed repositories (dependencies) into the AdminService class.
        Spring manages the creation of AdminService because of @Service, but @Autowired is for injecting the dependencies into the class, not creating the AdminService itself.

        When the application starts, Spring will first create instances of the repositories (BookRepository, ReviewRepository, CheckoutRepository) as they are defined elsewhere in the application.

        Then, when Spring creates an instance of the AdminService class, it injects the bookRepository, reviewRepository, and checkoutRepository into the constructor (since it sees the @Autowired annotation).
           ---------------------------------------------------------
           ---------------------------------------------------------
        @AdminService basically helps us to prevent writing extra code. Normally, after we have defined the variables and constructor, when we want to call the "AdminService" class in another page, we'll need to create object instances of each repository before we can parse them into the AdminService class. The code would look like this:
                BookRepository bookRepository = new BookRepository();
                ReviewRepository reviewRepository = new ReviewRepository();
                CheckoutRepository checkoutRepository = new CheckoutRepository();

                AdminService adminService = new AdminService(bookRepository, reviewRepository, checkoutRepository);

        But when we use @Autowired, we don't have to deal with the creation of object instances. We just create a variable for the "AdminService" like this:
                    private final AdminService adminService;

         ...and Spring would automatically inject everything for you. Hence the code will look like this:
                    private final AdminService adminService;

                    @Autowired
                    public AdminController(AdminService adminService) {
                        this.adminService = adminService;
                    }


                    N.B: To see this, see the "AdminController.java" file
     */
    @Autowired
    public AdminService (BookRepository bookRepository,
                         ReviewRepository reviewRepository,
                         CheckoutRepository checkoutRepository) {
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.checkoutRepository = checkoutRepository;
    }















    /*
            The goal of this function is to increase the number of books in the library (both total copies and available copies)
     */
    public void increaseBookQuantity(Long bookId) throws Exception {
        // Find the book by its ID in the database using bookRepository.findById(bookId).
        /*
              Optional<> returns a null value or a single value at most
              PAge<> returns a LIST of objectsalong with pagination

              In Java, Optional is a container object which may or may not contain a value. It is used to avoid null pointer exceptions by explicitly checking whether a value is present before accessing it.

              When you call a method like findById(bookId) from BookRepository, it returns an Optional<Book>. This means it could return a Book object if the book exists, or it could be empty if the book does not exist.

              NOTE: You’re not creating a new Book object. Instead, you are asking the bookRepository (which connects to the database) to find an existing Book with a specific bookId. If it finds the book, it wraps it in an Optional<Book>; if not, the Optional will be empty.
         */
        Optional<Book> book = bookRepository.findById(bookId);

        // Check if the book exists: If it doesn't, it throws an exception.
        if (!book.isPresent()) {
            throw new Exception("Book not found");
        }

        /*
            "book" is able to access these methods (.get, .setCopiesAvailable...) because it has a dataType of "Book"

            .get() - is a method that enables us to extract the value from Optional<Book>. The .get() method will return the Book object itself if the Optional contains a value. However, you should be careful because if the Optional is empty, calling .get() will throw a NoSuchElementException.

            The book is an Optional<Book>. By calling book.get(), you are retrieving the actual Book object from inside the Optional.

            Now because we have extracted the Book object using .get(), we can call the method ".setCopiesAvailable()"

            .setCpoiesAvailable() --> Here we'll update the current value for Number of Book Copies Available
         */
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        book.get().setCopies(book.get().getCopies() + 1);

        // Save the updated book back to the database using bookRepository.save().
        bookRepository.save(book.get());
    }














    public void decreaseBookQuantity(Long bookId) throws Exception {
        // NOTE: You’re not creating a new Book object. Instead, you are asking the bookRepository (which connects to the database) to find an existing Book with a specific bookId. If it finds the book, it wraps it in an Optional<Book>; if not, the Optional will be empty.
        Optional<Book> book = bookRepository.findById(bookId);

        // If the book is not available or its quantity is 0, throw an exception.
        if (!book.isPresent() || book.get().getCopiesAvailable() <= 0 || book.get().getCopies() <= 0) {
            throw new Exception("Book not found or quantity locked");
        }


        // Decrease the number of available copies and total copies.
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        book.get().setCopies(book.get().getCopies() - 1);

        // Save the updated book back to the database.
        bookRepository.save(book.get());
    }














    // This function will be called whenever a new book is added to the DB/Library
    // So we take the values from "AddBookRequest" - this is the model that holds the data parsed from the frontend - and we use those values to update the backend
    public void postBook(AddBookRequest addBookRequest) {
        // Create a new Book object
        Book book = new Book();

        // Set the book's details (title, author, description, copies, category, image) using the provided AddBookRequest object.
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setDescription(addBookRequest.getDescription());
        book.setCopies(addBookRequest.getCopies());
        book.setCopiesAvailable(addBookRequest.getCopies());
        book.setCategory(addBookRequest.getCategory());
        book.setImg(addBookRequest.getImg());

        // Save the new book to the database using bookRepository.save().
        bookRepository.save(book);
    }














    // Deletes a book and all related data (checkouts and reviews) from the library/DB
    public void deleteBook(Long bookId) throws Exception {

        // Find the book by its ID.
        Optional<Book> book = bookRepository.findById(bookId);

        // If the book doesn't exist, throw an exception.
        if (!book.isPresent()) {
            throw new Exception("Book not found");
        }

        // Delete the book from the bookRepository.
        bookRepository.delete(book.get());

        // Delete all checkouts related to the book from the checkoutRepository.
        checkoutRepository.deleteAllByBookId(bookId);

        // Delete all reviews related to the book from the reviewRepository.
        reviewRepository.deleteAllByBookId(bookId);
    }
}
