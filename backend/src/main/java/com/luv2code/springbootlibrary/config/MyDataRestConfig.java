package com.luv2code.springbootlibrary.config;

import com.luv2code.springbootlibrary.entity.Book;
import com.luv2code.springbootlibrary.entity.Message;
import com.luv2code.springbootlibrary.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

// When you mark a class with @Configuration, Spring knows that this class contains configuration information, like how beans (objects managed by Spring) should be created, how they should interact with each other, and how the application should behave.
@Configuration
// "RepositoryRestConfigurer" is a class which provides instructions for configuring Spring Data REST, such as how to handle entities and endpoints.

/*
    The difference between "implements" and "extends" is that when you extend, you want to inherit the method and peroperties of the extended class. But when you "implemnts" you are saying that you want to declare the nethods from the interface. FOr example, the interface may have a function that hasn't been implemented - so once we implemrnt that interface here, we can declare the function:

    INTERFACE WITH NON-IMPLEMENTED FUNCTION
    interface Animal {
        void speak();  // Method signature, no implementation
    }

    FUNCTION GETS IMPLEMENTED BY "implements" interface
    class Dog implements Animal {
        public void speak() {
            System.out.println("Dog barks");
        }
    }

 */

public class MyDataRestConfig implements RepositoryRestConfigurer {
    // This defines a string variable called theAllowedOrigins and sets it to "http://localhost:3000".  It specifies which domain (e.g., a frontend running locally) is allowed to interact with your API
    private String theAllowedOrigins = "http://localhost:3000";


    /*
        "configureRepositoryRestConfiguration" is the non-implenented function from RepositoryRestConfigurer. Here, we are overriding so we can implement the function
     */
    @Override
    public void configureRepositoryRestConfiguration(
            /*
            RepositoryRestConfiguration - Controls how the API behaves, like exposing entity IDs or enabling/disabling certain HTTP methods (e.g., POST, PUT, DELETE).

            Expose Entity IDs: By default, Spring Data REST hides the IDs of entities (like Book or Review) when sending data in the response. But you might want the ID of an entity to be visible, for example, to use it in a frontend application. The RepositoryRestConfiguration allows you to control whether IDs are exposed in the API response.

            Example:
            config.exposeIdsFor(Book.class);  // Exposes the ID of Book in API response

            REST (Representational State Transfer) is a way for different software systems to communicate with each other over HTTP (like the web).

            APIs (Application Programming Interfaces) are the middlemen that allow one system (like your app) to interact with another system (like a database or server).
             */
            RepositoryRestConfiguration config,

            /*
                CORS stands for Cross-Origin Resource Sharing. It's a security feature built into web browsers that controls how a web page can make requests to a server that is on a different domain (or "origin").

                For example, http://localhost:3000 is the origin for your frontend application running on your local machine.

                If you have a backend API running on a different domain or port (like http://localhost:8080), the browser will block any requests from your frontend (on localhost:3000) to your backend unless you explicitly allow it.

                The "CorsRegistry" helps you configure which external applications (origins) are allowed to access your API. You can specify which domains are allowed to make requests to your backend API.

                For example, if you have a frontend running on http://localhost:3000 and a backend running on http://localhost:8080, you want to make sure the frontend can access the backend. Using CorsRegistry, you can allow requests from http://localhost:3000.
                        cors.addMapping(config.getBasePath() + "/**")
                            .allowedOrigins("http://localhost:3000");

                addMapping(config.getBasePath() + "/**"): This part tells Spring to apply this rule to all endpoints of the API.
                .allowedOrigins("http://localhost:3000"): This part allows requests only from http://localhost:3000 (the origin of your frontend).
             */
            CorsRegistry cors
    ) {
        /*
            This creates an array of HTTP methods that are not allowed for certain entities.
                POST: Creating new resources.
                PATCH: Partially updating a resource.
                DELETE: Deleting a resource.
                PUT: Fully updating a resource.

            HttpMethod is an enum that represents HTTP methods such as GET, POST, PUT, DELETE, etc.
            These methods define the actions you can perform on resources via HTTP requests:
                    GET — Retrieve data.
                    POST — Create data.
                    PUT — Update data.
                    DELETE — Delete data.
                    PATCH — Partially update data.
                    OPTIONS — Get metadata about a resource.
                    HEAD — Get metadata without the body.
         */
        HttpMethod[] theUnsupportedActions = {
                HttpMethod.POST,
                HttpMethod.PATCH,
                HttpMethod.DELETE,
                HttpMethod.PUT
        };

        // exposeIdsFor makes the IDs of specific entities (e.g., Book, Review, Message) visible in API responses. Normally, Spring Data REST hides these IDs for security, but you might expose them if the frontend needs them.
        config.exposeIdsFor(Book.class);
        config.exposeIdsFor(Review.class);
        config.exposeIdsFor(Message.class);

        /*
            This is a custom method you've defined in your configuration class to disable specific HTTP methods for a given entity (like Book, Review, or Message).

            Its purpose is to prevent certain actions (like creating or deleting data) from being performed on the entity via your REST API. For example, if you want to disable the ability to POST or DELETE books in your API, you'd use this method.

            - You're passing "Book.class" to indicate which entity you're modifying (in this case, Book).
            - You're passing "config" to access the Spring Data REST configuration settings (like disabling certain HTTP methods).
            - You're passing "theUnsupportedActions" to specify which HTTP methods (like POST and PUT) should be disabled for the Book entity.
         */
        disableHttpMethods(Book.class, config, theUnsupportedActions);
        disableHttpMethods(Review.class, config, theUnsupportedActions);
        disableHttpMethods(Message.class, config, theUnsupportedActions);

        /* Configure CORS Mapping
            RECALL:

            For example, if you have a frontend running on http://localhost:3000 and a backend running on http://localhost:8080, you want to make sure the frontend can access the backend. Using CorsRegistry, you can allow requests from http://localhost:3000.
                        cors.addMapping(config.getBasePath() + "/**")
                            .allowedOrigins("http://localhost:3000");

                addMapping(config.getBasePath() + "/**"): This part tells Spring to apply this rule to all endpoints of the API.
                .allowedOrigins("http://localhost:3000"): This part allows requests only from http://localhost:3000 (the origin of your frontend).

            config.getBasePath() + "/**": Matches all API endpoints under the base path.
            .allowedOrigins(theAllowedOrigins): Only allows requests from the domain specified in theAllowedOrigins.
        */
        cors
                .addMapping(config.getBasePath() + "/**")
                .allowedOrigins(theAllowedOrigins);
    }

    private void disableHttpMethods(
            /*
            theClass: The entity class (e.g., Book, Review, Message).
            config: The REST configuration object.
            theUnsupportedActions: The HTTP methods to disable.
             */
            Class theClass,
            RepositoryRestConfiguration config,
            HttpMethod[] theUnsupportedActions
    ) {
        config
                // Retrieves the settings that control which parts of an entity are exposed via the API.
                .getExposureConfiguration()
                // Specifies the entity class for which you want to customize the exposure. For example, theClass might be Book.class.
                .forDomainType(theClass)
                // Configures how individual items (e.g., one Book) are exposed. For example, you might disable POST, PUT, or DELETE for a specific Book.
                .withItemExposure(
                        (metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)
                )
                // Configures how collections of items (e.g., all Books) are exposed. For example, you might disable POST or PUT for all Books as a group.
                .withCollectionExposure(
                        (metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)
                );

                /*
                Imagine you’re setting up rules for a library system:

                    Exposure Configuration is like deciding who gets access to certain library sections.
                    For Domain Type (theClass) is like saying, "These rules apply to books."
                    With Item Exposure is like saying, "Here are the rules for checking out one book."
                    With Collection Exposure is like saying, "Here are the rules for browsing or modifying the entire library collection."
                 */
    }
}
