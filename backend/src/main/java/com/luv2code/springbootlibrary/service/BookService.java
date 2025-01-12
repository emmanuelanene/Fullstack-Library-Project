package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.dao.BookRepository;
import com.luv2code.springbootlibrary.dao.CheckoutRepository;
import com.luv2code.springbootlibrary.dao.HistoryRepository;
import com.luv2code.springbootlibrary.entity.Book;
import com.luv2code.springbootlibrary.entity.Checkout;
import com.luv2code.springbootlibrary.entity.History;
import com.luv2code.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/*
    @Service - tells Spring boot that this class is a "Service" meaning that it'll handle business logics

    @Transactional - tells Spring that is any errors occur while updating or deleting data in the DB, the error/change should be rolled back/reversed to prevent the DB from being left in a broken state.
 */
@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;

    private CheckoutRepository checkoutRepository;

    private HistoryRepository historyRepository;

    @Autowired
    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository,
                       HistoryRepository historyRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.historyRepository = historyRepository;
    }


    /*
        PURPOSE: to allow users checkout books
     */
    public Book checkoutBook (String userEmail, Long bookId) throws Exception {
        // Find the book in bookRepository
        Optional<Book> book = bookRepository.findById(bookId);

        // Check if the user has already checked out this book
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        // Validate if the book exists, isn't already checked out, and has available copies
        if (!book.isPresent() || validateCheckout != null || book.get().getCopiesAvailable() <= 0) {
            throw new Exception("Book doesn't exist or already checked out by user");
        }

        // Reduce available copies and save the updated book
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookRepository.save(book.get());

        // // Create a new checkout record for the user
        Checkout checkout = new Checkout(
                userEmail,              // user email
                LocalDate.now().toString(),         // checkolut date
                LocalDate.now().plusDays(7).toString(),         // return date
                book.get().getId()          // book Id
        );

        // // Save the checkout information to the database
        checkoutRepository.save(checkout);

        // Return the updated book object
        return book.get();
    }





    // This method checks if the user has already checked out the book.
    public Boolean checkoutBookByUser(String userEmail, Long bookId) {
        // The method findByUserEmailAndBookId is looking for a checkout record that matches the provided userEmail and bookId.
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        // If validateCheckout is not null: It means a checkout record was found for that user and book, so the code inside the if block will execute. This typically means that the user has already checked out that book.
        if (validateCheckout != null) {
            return true;
        }
        // If validateCheckout is null: It means no checkout record was found, so the code inside the if block will be skipped. This usually indicates that the user has not checked out the book.
        else {
            return false;
        }
    }







    // This method lets a user return a checked-out book.
    public void returnBook (String userEmail, Long bookId) throws Exception {

        // Fetch the book by ID
        Optional<Book> book = bookRepository.findById(bookId);

        // Find the checkout record for the user and book
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        // Checks if book is present
        if (!book.isPresent() || validateCheckout == null) {
            throw new Exception("Book does not exist or not checked out by user");
        }

        // Increase available copies
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);

        // Save
        bookRepository.save(book.get());

        // Delete the checkout record
        checkoutRepository.deleteById(validateCheckout.getId());

        // Log the return in history
        History history = new History(
                userEmail,
                validateCheckout.getCheckoutDate(),
                LocalDate.now().toString(),
                book.get().getTitle(),
                book.get().getAuthor(),
                book.get().getDescription(),
                book.get().getImg()
        );

        historyRepository.save(history);
    }









    // This counts how many books the user has currently checked out by fetching their checkouts from the repository.
    public int currentLoansCount(String userEmail) {
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }








    //The main goal of this function is to generate a list of the books that a user has currently checked out, and for each book, calculate how many days are left until the book is due to be returned.
    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {

        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();

        // Fetch all checkouts for the user
        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);
        List<Long> bookIdList = new ArrayList<>();

        // Get all" book IDs" from the List of checkout objects and add the extracted IDs into the newly created ArrayList
        for (Checkout i: checkoutList) {
            bookIdList.add(i.getBookId());
        }

        // // Fetch all books associated with the checkout list
        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");



        // From the list of books in the array (the array of books we created based on another ID list)
        // You have a list of books that the user has checked out (from bookRepository.findBookByIds(bookIdList)), and you're going through each book one by one.
        for (Book book : books) {

            Optional<Checkout> checkout =
                    // checkoutList: This is a list of all the Checkout objects associated with a user. Every time a user checks out a book, there’s a Checkout record created.
                    checkoutList
                            /*
                                stream(): This converts the list into a stream. A stream is a sequence of elements (in this case, Checkout objects) that you can process one by one. Think of it like a pipeline that processes items from the list.

                                Why stream? Streams let you apply operations (like filtering, mapping, etc.) in a clear, chainable way.
                             */
                            .stream()
                            /*
                                filter(): This method lets you filter the stream, meaning you only want to keep certain Checkout objects that meet a condition.

                                x -> x.getBookId() == book.getId(): This is the condition for filtering.
                                x represents each Checkout object in the stream (think of it like a temporary variable in a loop).
                                x.getBookId() is a method that retrieves the bookId from the Checkout object (x).
                                book.getId() is the ID of the current Book object you’re working with in the outer loop (this is the book that’s currently being processed).

                                The filter is checking: "Does the bookId in this Checkout object match the id of the current book?"

                                If yes, the Checkout passes the filter and is kept. If no, it’s filtered out.
                             */
                            .filter(x -> x.getBookId() == book.getId())
                            /*
                                Once you’ve filtered the stream to only include Checkout objects where the bookId matches the current book’s id, you use .findFirst(). This method looks for the first element in the stream that matches the filter condition and wraps it in an Optional.

                                If a matching Checkout is found, you’ll get an Optional containing that Checkout.
                                If no matching Checkout is found, you’ll get an empty Optional (meaning there is no match).
                             */
                            .findFirst();

            // After you’ve filtered the list, you need to check if a Checkout was found (isPresent()). If there is no corresponding Checkout object, the code inside the if block won’t run.
            if (checkout.isPresent()) {
                // d1: This is the return date of the book, which you get from the Checkout object.
                // d2: This is today’s date, which you calculate using LocalDate.now().
                // Both dates are formatted using SimpleDateFormat ("yyyy-MM-dd") so that they match.
                Date d1 = sdf.parse(checkout.get().getReturnDate());
                Date d2 = sdf.parse(LocalDate.now().toString());


                // Here, you're calculating the difference between the return date (d1) and today’s date (d2), in milliseconds.
                // You then convert that difference into days using TimeUnit.DAYS.
                TimeUnit time = TimeUnit.DAYS;
                long difference_In_Time = time.convert(d1.getTime() - d2.getTime(),
                        TimeUnit.MILLISECONDS);

                // For each book, you create a ShelfCurrentLoansResponse object, which holds the Book and the number of days left before the return date.
                //You then add this response to the shelfCurrentLoansResponses list.
                shelfCurrentLoansResponses.add(
                        new ShelfCurrentLoansResponse(
                                book,
                                (int) difference_In_Time
                        )
                );
            }
        }
        return shelfCurrentLoansResponses;
    }




    public void renewLoan(String userEmail, Long bookId) throws Exception {

        // Fetch the checkout record for the user and book
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        // If no checkout record is found (i.e., validateCheckout == null), the method throws an exception.
        if (validateCheckout == null) {
            throw new Exception("Book does not exist or not checked out by user");
        }

        // This creates a SimpleDateFormat object to parse date strings in the format "yyyy-MM-dd" (e.g., "2024-09-25").
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        /*
            d1: The returnDate from the validateCheckout object is converted from a string to a Date object.
            d2: The current date (today's date) is converted to a string (using LocalDate.now()), then parsed into a Date object.
         */
        Date d1 = sdFormat.parse(validateCheckout.getReturnDate());
        Date d2 = sdFormat.parse(LocalDate.now().toString());

        // Extend the return date if it hasn't passed
        if (d1.compareTo(d2) > 0 || d1.compareTo(d2) == 0) {
            validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(validateCheckout);
        }
    }

}