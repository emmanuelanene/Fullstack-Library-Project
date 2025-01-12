package com.luv2code.springbootlibrary.requestmodels;

import lombok.Data;

/*
    REQUEST MODELS

    The request models are the data structures used when the frontend sends information to the backend. Each model here represents a different type of action the user can take:

    ReviewRequest: Holds data for a book review submission, including the rating, book ID, and an optional description.

    AdminQuestionRequest: Captures an admin’s response to a question, including the question ID and the response text.

    AddBookRequest: Gathers details needed to add a new book, like the title, author, and number of copies.

    By organizing these as request models, the backend can clearly see what information to expect for each specific action, keeping it clean and understandable.
 */

/*
    DIFFERENCE B/W MODELS AND CONSTRUCTORS (Entity constructors is what I'm referring to)

    The models are a way for the frontend of my site to communicatge with the backend. These arguments within the models ar like forms to be filled from the frontend of the app and then it'll be sent to the backend. They do not store the data tho (that's sort of the job of the constructor... kind of).

    These models are specifically created to make it easy for the frontend to send data to the backend. They act as containers that hold just the right information the backend needs for each action.

    When the frontend wants to do something like add a new book, submit a review, or send an admin response, it fills out one of these models with the relevant data and sends it to the backend. The backend then reads from these models and decides what to do with the information, like adding a new record to the database or updating something.

     The constructors in the entity classes are actually used for creating instances of entities in the backend, typically when fetching data from the database or preparing data to save to the database. But they don’t directly interact with the frontend.

     Here’s how it usually works:
        *Frontend sends data to the backend: When the frontend sends data, it uses one of these models (like AddBookRequest) to structure it.

        *Backend receives the model and extracts data: The backend reads data from the model and may then use it to create an entity. This is where constructors in the entity come in—they help to create or update entities with the data that was passed in from the model.

        *Saving to the Database: Once the backend has the entity ready with all the data, it’s saved to the database.

        *So, the models (DTOs) organize and transport the data from the frontend to the backend, while entity constructors help create and handle that data in the backend, especially when it’s going into or coming from the database.
 */
@Data
public class AddBookRequest {

    private String title;

    private String author;

    private String description;

    private int copies;

    private String category;

    private String img;

}
