package com.luv2code.springbootlibrary.dao;

import com.luv2code.springbootlibrary.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // using bookId here and not email because Reviews are associated with the book. So if a user views a book page, I want to show all the reviews associated with that bookID
    Page<Review> findByBookId(@RequestParam("book_id") Long bookId,
                              Pageable pageable);

    // Here we are returnign a single book review that was left by a single user
    Review findByUserEmailAndBookId(String userEmail, Long bookId);

    @Modifying
    @Transactional
    @Query("delete from Review where book_id in :book_id")
    void deleteAllByBookId(@Param("book_id") Long bookId);
}
