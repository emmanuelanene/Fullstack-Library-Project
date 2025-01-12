package com.luv2code.springbootlibrary.dao;

import com.luv2code.springbootlibrary.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
    This line declares an interface called CheckoutRepository that extends the JpaRepository interface.

    JpaRepository<Checkout, Long> means:
    Checkout: The entity (table) this repository will work with.
    Long: The type of the primary key (or id) for the Checkout entity.

    By extending JpaRepository, this interface will automatically have access to all the methods for CRUD operations (like saving, finding, deleting records) without you needing to write them manually.
 */
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
    /*
    This method finds a specific Checkout record based on the combination of the user's email and the book's ID.

    findByUserEmailAndBookId: The method name is structured to automatically generate a query (called a query method) that fetches a Checkout based on the two fields: userEmail and bookId.

    userEmail: This is the email address of the user who checked out the book.
    bookId: This is the ID of the book being checked out.

    IMPORTANT:
        This method doesn't use a Page<> because we are not using pagination. We are only returning a single record SO there's no need for pagination

        We also dont use @RequestParams because
            @RequestParam is only needed when you're working with URLs in the controller layer.
                Example with URL: If you have a URL like ".../books?id=22", you want to get the id (22 in this case) from the URL and use it to find data in the database. That's when you use @RequestParam. The @RequestParam helps "grab" that id=22 from the URL and pass it to the method, which can then look up the correct book in the database.

                Why @RequestParam? Because the controller needs a way to take the information from the URL (the id in this case) and use it inside the code. @RequestParam does that.
           But in other functions, like in the repository, you're not dealing with URLs or web requests.
                You don’t need @RequestParam in those functions because you're not handling data from the URL or web. You're just calling a method in your code, like findByUserEmailAndBookId, and passing the necessary data (like userEmail and bookId) directly as arguments.
     */
    Checkout findByUserEmailAndBookId(String userEmail, Long bookId);


    // This method finds all books that a user (identified by their email) has checked out. Return a list of Checkout objects based on a userEmail
    List<Checkout> findBooksByUserEmail(String userEmail);


    /*
        This method deletes all Checkout records associated with a particular book (not user) by its ID.

        @Modifying: This annotation tells Spring Data JPA that this method performs a modifying operation (in this case, a delete), not just a read query.
        @Query: This annotation allows you to write a custom query using JPQL (Java Persistence Query Language), which is a bit like SQL but works with objects instead of table rows.
        The query: "delete from Checkout where book_id in :book_id" means "delete all checkout records where the book_id matches the provided value (:book_id)".
        @Param("book_id"): The @Param annotation is used to pass the value of the book_id parameter into the query.

    USAGE:
        checkoutRepository.deleteAllByBookId(101L)  --->>   This will delete all Checkout records where the book_id is 101L, essentially removing all checkouts for that specific book.

     */
    @Modifying
    @Transactional
    @Query("delete from Checkout where book_id in :book_id")
    void deleteAllByBookId(@Param("book_id") Long bookId);

}