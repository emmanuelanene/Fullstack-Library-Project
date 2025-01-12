package com.luv2code.springbootlibrary.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "checkout")
@Data
public class Checkout {
    /*
        An alternative to doing this would be to use the Lombok statement:
            @NoArgsConstructor  // Generates an empty constructor
            @AllArgsConstructor // Generates a constructor with all the fields as arguments


        The reasone we need the constructor is to be able to parse data into the Checkout Table in the SQL DB. For the "Book" table, we didn't need to parse in any data to it hence the reasone we didn't add any constructors. But here, we will be adding data to the checkout table. Hence,  the constructor is important
     */
    public Checkout() {}

    public Checkout(String userEmail, String checkoutDate, String returnDate, Long bookId) {
        this.userEmail = userEmail;
        this.checkoutDate = checkoutDate;
        this.returnDate = returnDate;
        this.bookId = bookId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "checkout_date")
    private String checkoutDate;

    @Column(name = "return_date")
    private String returnDate;

    @Column(name = "book_id")
    private Long bookId;

}