/*
    GENERAL INFORMATION:

    Note that all the entities created here must match the names of the tables in your DB. They must match exactly
 */


package com.luv2code.springbootlibrary.entity;

import lombok.Data;
import javax.persistence.*;

/*
    @Entity - tells Spring and Hibernate (used to translate Java objects into SQL Queries) that this Java class (i.e. Book) represents a table in our DB. It allows Hibernate to understand that the Book class is linked to the book table in the database.

    @Table - this specifies the name of the table we want to link this class to -> Links Book (class) to book (table in DB)

    @Data - This is from Lombok, a tool that automatically generates common methods for you (like getters, setters, toString(), etc.).
 */
@Entity
@Table(name = "book")
@Data
public class Book {
    /*
        id, title, author, description, ... -->> these are names of columns in our DB table (i.e. book)

        @Id - tells the database the primary key
        @GenerationVAlue(...) - tells the DB that the values for this column will be automatically generated
        @Column(name...) - Maps this id field in the class to the id column in the table.

     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "description")
    private String description;

    @Column(name = "copies")
    private int copies;

    @Column(name = "copies_available")
    private int copiesAvailable;

    @Column(name = "category")
    private String category;

    @Column(name = "img")
    private String img;

}
