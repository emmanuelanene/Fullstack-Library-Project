package com.luv2code.springbootlibrary.dao;

import com.luv2code.springbootlibrary.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;


/*
    This line declares an interface called HistoryRepository that extends JpaRepository.

    JpaRepository<History, Long>:
    History: The entity (or table) this repository will handle.
    Long: The type of the primary key (id) of the History entity.
    By extending JpaRepository, this interface inherits basic CRUD methods (like save, find, delete) for the History entity without extra code.
 */
public interface HistoryRepository extends JpaRepository<History, Long> {
    /*
    @RequestParam("email") String userEmail: This specifies that the method will look for the userEmail parameter from an HTTP request and use it to find matching records in the database.

     Pageable pageable: This parameter is used for pagination. It tells Spring how many records to fetch, what page number to fetch, and possibly sorting information.
     */
    Page<History> findBooksByUserEmail(@RequestParam("email") String userEmail, Pageable pageable);
}
