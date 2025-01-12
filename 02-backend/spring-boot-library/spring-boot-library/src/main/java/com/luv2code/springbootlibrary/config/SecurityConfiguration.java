package com.luv2code.springbootlibrary.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

// This annotation tells Spring that this class contains configuration settings for your application. Spring will scan this class and use it to configure the security behavior of your app.
@Configuration
public class SecurityConfiguration {
    /*
        @Bean is an annotation that tells Spring to create and manage an instance of the method's return type.

        When Spring sees @Bean, it knows to call that method, create the object, and store it in its internal container (called the "Spring Context").

        An object in programming is an instance of a class, and it can hold data or perform actions. For example:
        Book myBook = new Book("Title", "Author");
         In your code, the object returned by the method has a type of SecurityFilterChain.


        When we say Spring manages the object, we mean:
            Spring Creates the Object
                When the application starts, Spring calls the method annotated with @Bean and creates the object.
                In your example, it will call filterChain(HttpSecurity http) and get an object of type SecurityFilterChain.

            Spring Stores the Object in the Spring Context
                After creating the object, Spring stores it in a container called the "Spring Context."
                Think of the Spring Context as a storage system for all the objects (beans) your app needs.

            Spring Provides the Object When Needed
                Whenever any part of your app needs the SecurityFilterChain, Spring will automatically give the same object without creating a new one.
                This is useful for efficiency and ensuring consistency across your app.

            -----------------------------------------
            -----------------------------------------
            -----------------------------------------
            -----------------------------------------

            Call the Method: Spring calls your method (e.g., filterChain(HttpSecurity http)).

            Create the Object: The method's code will run and create an instance of the specified type (in this case, a SecurityFilterChain object).

            Store the Object: Spring will store this newly created instance in its "Spring Context" so it can be reused throughout the application.


        ---------------------------------------------------------
        ---------------------------------------------------------
        ---------------------------------------------------------
        ---------------------------------------------------------
        Normallly, this is how you create class object instances:
            SecurityFilterChain abc = new SecurityFilterChain(...);

        But in this case, it's different:
        Spring uses the concept of inversion of control (IoC). Instead of you manually creating the object like we did above, Spring creates and manages it for you. When you use @Bean, Spring takes care of this process automatically.



        In your filterChain method, you don’t directly use new because the "SecurityFilterChain" object is being created indirectly via a builder method (http.build()):
                @Bean
                public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                    // Configure HttpSecurity (like enabling/disabling features)
                    http.csrf().disable();

                    // Create an instance of SecurityFilterChain using HttpSecurity's build method
                    return http.build();
                }

        http.build(): This is a method provided by Spring Security’s HttpSecurity class. It creates and returns an instance of SecurityFilterChain.  Think of it as a shortcut or factory method that builds the object for you.

        Spring Context: When you annotate the method with @Bean, Spring takes the object created by http.build() and stores it in the Spring Context. You don’t see new SecurityFilterChain(...) because Spring Security abstracts that part away.


        What If You Didn't Use a Builder?
        If SecurityFilterChain didn’t have a builder, you would use the new keyword yourself:
                @Bean
                public SecurityFilterChain filterChain() {
                    return new SecurityFilterChain(...); // Directly creating the instance
                }
        In this case, Spring still manages the object created by this method because of the @Bean annotation.
     */
    @Bean
    // "HttpSecurity" is a class in Spring Security that allows you to configure security settings for HTTP requests, like authentication, authorization, and other security features.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /*
            Cross-Site Request Forgery (CSRF) is a type of attack where a malicious user tricks another user into performing an action on a website without their consent, often while the user is logged in. For example, it could involve a user being tricked into clicking on a link that triggers an unwanted action (like deleting their account) on a website they are logged into. To prevent this, Spring Security enables CSRF protection by default. It makes sure that requests that modify data (POST, PUT, DELETE, etc.) come from trusted sources by requiring a special token in those requests.

            The line http.csrf().disable(); is part of the configuration for Cross-Site Request Forgery (CSRF) protection in Spring Security.

            When you call http.csrf().disable();, you are turning off CSRF protection for your application. This means: Requests will no longer require CSRF tokens, even for state-changing operations like POST, PUT, DELETE, etc. It makes your application less secure against CSRF attacks, so this is usually done when you're building stateless APIs (like REST APIs) that don't rely on session-based authentication.
         */
        http.csrf().disable();



        // This is where you start configuring authorization (who can access what) in your application. "authorizeRequests()" is a method that allows you to specify which HTTP requests (i.e., URLs) need authentication and which don’t.
        http.authorizeRequests(
                // This part is setting up the request URL patterns you want to secure:
                configurer -> configurer
                        // This method takes in URL patterns (wildcards are allowed) and defines rules about what kind of access these URLs should have. This means any request matching these URL patterns will require authentication.
                        .antMatchers(
                                "/api/books/secure/**",
                                "/api/reviews/secure/**",
                                "/api/messages/secure/**",
                                "/api/admin/secure/**"
                        )
                        // .authenticated() means that the user needs to be authenticated (i.e., logged in) in order to access these URLs. Only users who are authenticated can access these endpoints.
                        .authenticated()
        )
            /*
                OAuth2 is a framework that allows third-party applications (like Google, Facebook, etc.) to access a user’s data without the user needing to share their credentials (username and password).

                Resource means protected data in this case. For example, the books, reviews, messages, etc., in your app.

                A resource server is an application (like your Spring app) that holds resources (data) that the client applications want to access.

                .oauth2ResourceServer():
                When you use .oauth2ResourceServer() in your Spring Security configuration, you’re telling Spring Security that your app is going to act as a resource server. It will accept requests that contain an OAuth2 token and validate it to ensure that the user is authorized to access the resources (like books, reviews, etc.).
             */
            .oauth2ResourceServer()

            /*
                .jwt() tells Spring Security that your app will use JWT (JSON Web Tokens) as the format for the OAuth2 tokens. JWT is a common way to pass OAuth2 tokens between services. The JWT token contains user information (like their username) and is signed to ensure its integrity.
             */
            .jwt();



        /*
        http.cors() is a method in Spring Security that enables Cross-Origin Resource Sharing (CORS) support for your application.

        What is CORS?
        CORS is a security feature implemented by browsers to restrict web pages from making requests to a domain different from the one that served the web page.

        For example, if you have a frontend running on http://localhost:3000 (like a React app) and your backend API is running on http://localhost:8080, the browser will block requests made from localhost:3000 to localhost:8080 unless the backend explicitly allows it.

        When you call http.cors(), you're telling Spring Security to allow certain cross-origin requests based on the CORS policy you define.
         */
        http.cors();




        /*
        Content negotiation is a mechanism that allows a server to decide which content type (such as JSON, XML, HTML, etc.) should be returned in the response, based on the client's request.

        For example, if a client/frontend (like a web browser or mobile app) requests data from the server, the client might specify that it prefers JSON or XML as the response format. The server/backend can then respond with the correct format based on what the client requested.

        "ContentNegotiationStrategy" is an interface in Spring that defines how the server should choose the content type (JSON or XML or HTML, etc.) for a response based on the client’s request. The "ContentNegotiationStrategy" is used by Spring to determine the content type (like application/json, application/xml, etc.) that should be used when sending a response.


        "HeaderContentNegotiationStrategy" determines the response format of the server/backend based on the "Accept" header in the HTTP request. The Accept header tells the server what type of content the client prefers to receive.
        For example, if a client/frontend sends a request with Accept: application/json, the server will return the response in JSON format.



        How Does This Work?
            When a request is made to your application, Spring Security will use the HeaderContentNegotiationStrategy to read the Accept header from the HTTP request.
            If the header is something like Accept: application/json, Spring will return the response in JSON format.
            If the header is Accept: application/xml, Spring will return the response in XML format.
         */
        http.setSharedObject(
                ContentNegotiationStrategy.class,
                new HeaderContentNegotiationStrategy()
        );



        /*
            This line of code is configuring how Spring Security should handle 401 Unauthorized responses in the context of an OAuth2 resource server.

            401 Unauthorized is an HTTP status code that indicates the client is not authorized to access a particular resource. This typically happens when the client does not provide the correct credentials (like an OAuth2 token) or the credentials provided are invalid.

            Okta.configureResourceServer401ResponseBody(http) is a method provided by the Okta Spring Security library.
            It configures how your application should respond when a client makes an unauthenticated request (i.e., the server returns a 401 Unauthorized status).
         */
        Okta.configureResourceServer401ResponseBody(http);

        /*
            In the Spring Security configuration, the http.build() method is used to finalize and build the configuration you've set up for HTTP security.
            It returns a fully constructed SecurityFilterChain that contains all the security settings (like authentication, authorization, CORS, etc.) you've configured.
            This SecurityFilterChain object is then used by Spring Security to control access to the resources in your application.
         */
        return http.build();
    }

}









