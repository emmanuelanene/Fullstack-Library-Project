package com.luv2code.springbootlibrary.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "review")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "date")
    /*
        This Hibernate-specific annotation automatically sets the field to the current timestamp when a new row is inserted into the database.

        Function: The timestamp will be set only once when the entity is first persisted, capturing the creation time.
     */
    @CreationTimestamp
    private Date date;

    @Column(name = "rating")
    private double rating;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "review_description")
    private String reviewDescription;

}
