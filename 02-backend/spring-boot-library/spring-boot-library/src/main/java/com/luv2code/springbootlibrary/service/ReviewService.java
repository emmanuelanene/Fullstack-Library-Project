package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.dao.BookRepository;
import com.luv2code.springbootlibrary.dao.ReviewRepository;
import com.luv2code.springbootlibrary.entity.Review;
import com.luv2code.springbootlibrary.requestmodels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@Transactional
public class ReviewService {

    /*
        NOTICE HOW THE "SEVICE" PAGE IS using the "Repository, Entity and Models" together?? Its insane

        MODELS: Models, like ReviewRequest, are data holders for the information that comes from the user input (or other layers). They represent the data passed to or from the client (e.g., frontend, API requests) but aren’t directly tied to the database.

        ENTITY: Entities are like blueprints for your database tables. They represent the actual data you’re working with in the database. In your case, the Review entity is the representation of a review in the database.

        REPOSITORIES: Repositories are like messengers that handle communication between your service and the database. In your case, you have the ReviewRepository, which helps the service talk to the database to fetch and save review data.
     */

    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    //  ReviewRequest reviewRequest  ->> This has no values so how do we create values to parse into the method?? Since the ReviewRequest model has no constructors, Spring automatically create a no argument constructor for the model. In order to add values to the ReviewRequest model, we'll need to use Setters to explicitly add values to the ReviewRequest model. It'll look like this

    //  reviewRequest.setRating(4.5); // Set the rating
    //  reviewRequest.setBookId(101L); // Set the book ID
    //  reviewRequest.setReviewDescription(Optional.of("This book was fantastic!"));

        // PURPOSE OF FUNCTION
        // This function is designed to post a review for a specific book by a user, but it checks first to ensure that the user hasn’t already reviewed the book. It ensures that no duplicate reviews are posted by the same user for the same book.
        public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception {
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, reviewRequest.getBookId());
        if (validateReview != null) {
            throw new Exception("Review already created");
        }

        Review review = new Review();
        review.setBookId(reviewRequest.getBookId());
        review.setRating(reviewRequest.getRating());
        review.setUserEmail(userEmail);
        if (reviewRequest.getReviewDescription().isPresent()) {
            review.setReviewDescription(reviewRequest.getReviewDescription().map(
                    Object::toString
            ).orElse(null));
        }

        // Get Today's Date
        review.setDate(Date.valueOf(LocalDate.now()));
        // Save the review
        reviewRepository.save(review);
    }


    // PURPOSE OF FUNCTION
    //  This function checks if a user has already posted a review for a specific book. It’s like a quick lookup tool to see whether a user has previously reviewed the book, returning true or false based on the result.
    public Boolean userReviewListed(String userEmail, Long bookId) {
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (validateReview != null) {
            return true;
        }

        else {
            return false;
        }
    }

}









