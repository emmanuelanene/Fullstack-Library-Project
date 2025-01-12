package com.luv2code.springbootlibrary.dao;

import com.luv2code.springbootlibrary.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/*
    JpaRepository<Book, Long>: This means the "BookRepository" will have access to all the default methods/functions provided by the JpaRepository interface for managing Book entities, such as save(), delete(), findAll(), etc.

    <Book, Long>:
    Book: This tells the repository to manage Book entities.
    Long: This is the type of the primary key (id field) for the Book entity.
 */
public interface BookRepository extends JpaRepository<Book, Long> {
    /*
        Purpose: This method searches for books whose title contains a specific string (a substring search).

        @RequestParam("title") String title: This is the input (a string) that the method will look for in the book titles.

        Pageable pageable: This parameter is used to handle pagination (i.e., how many books to show per page, and on which page number).

        Return Value: Page<Book> tells the program that this "Page" will only hold "Book" objects. If it was List<String>, that would mean that we'd only return a List which contains Strings: ["Emma", "Wesley"]
        It returns a Page<Book>, which is a paginated list of books that contain the provided title.

        This si what a List looks like:
            [Alice, Bob]
        THis is what Page looks like:
            Page content: [Alice, Bob]
            Current page: 1
            Total pages: 5
            Total elements: 10
            Page size: 2


        Example Usage: If a user searches for "Harry" in book titles, this method might return books like "Harry Potter", "Harry's Adventure", etc.
     */
    Page<Book> findByTitleContaining(@RequestParam("title") String title, Pageable pageable);

    /*
           Purpose: This method finds books that belong to a specific category.

           @RequestParam("category") String category: This is the category name you want to search for.
            Pageable pageable: Just like the previous method, this helps with pagination.

            PAge<>: In Spring Data JPA, Page<> is used for pagination. It helps you handle large sets of data by breaking them into smaller "pages" that can be loaded one at a time, rather than all at once. This is especially useful when you are dealing with a lot of records in a database, like displaying a long list of results in an application.

            Return Value: It returns a paginated list (Page<Book>) of books that belong to the specified category.

            Example Usage: If you want to find all books in the "Science Fiction" category, you would call this method with "Science Fiction" as the category.
     */
    Page<Book> findByCategory(@RequestParam("category") String category, Pageable pageable);





    /*
        The difference between this function and the one below it is:
             THis function is designed to fetch books based on a single ID and includes support for pagination. The function below is designed to fetch books based on multiple IDs using a custom query with no pagination.

             findById(Long id, Pageable pageable) returns a paginated result (Page<Book>), making it suitable for large data sets where you want to limit the number of records fetched in one query.
             findBookByIds(List<Long> bookIds) returns a simple list (List<Book>) without pagination.
     */
    Page<Book> findById(@RequestParam("id") Long id, Pageable pageable);

    /*
    Purpose: The method is designed for multiple IDs, but you can use it for a single ID by wrapping it in a list.

    Custom Query (@Query): The method uses a custom JPQL (Java Persistence Query Language) query to find books by their IDs.
    @Query("select o from Book o where id in :book_ids"): This means "select all books (o) where their ID is one of the provided IDs (book_ids)".

    @Param("book_ids") List<Long> bookId: This is a list of IDs (Long) that the method will look for in the Book table.

    Return Value: It returns a list of Book objects that match the provided IDs.

    Example Usage: If you want to fetch books with the IDs 1, 5, and 10, you would call this method with those IDs.
     */
    @Query("select o from Book o where o.id in :book_ids")
    List<Book> findBooksByBookIds (@Param("book_ids") List<Long> bookId);

    /*
    IMPORTANT!!!!!!!!

    The method ABOVE is designed for multiple IDs, but you can use it for a single ID by wrapping it in a list.

    If you frequently search by a single ID, you might also consider creating an additional method that accepts a single Long parameter for convenience:

        @Query("select o from Book o where o.id = :bookId")
        Book findBookById(@Param("bookId") Long bookId);

     */
}
