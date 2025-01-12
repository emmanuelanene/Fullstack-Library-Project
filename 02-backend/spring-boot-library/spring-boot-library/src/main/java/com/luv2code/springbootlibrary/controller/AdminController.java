package com.luv2code.springbootlibrary.controller;

import com.luv2code.springbootlibrary.requestmodels.AddBookRequest;
import com.luv2code.springbootlibrary.service.AdminService;
import com.luv2code.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Allows requests to this controller from a frontend running on http://localhost:3000 (e.g., a React app). Browsers block cross-origin requests by default for security reasons. This annotation bypasses that for the specified domain.
@CrossOrigin("http://localhost:3000")

// Marks this class as a Spring REST controller. It tells Spring that this class handles HTTP requests and responses, and the return values from methods will be converted to JSON or another appropriate format.
@RestController

// Defines the base URL path for all the endpoints in this controller.
// http://localhost:8080/api/admin/secure/increase/book/quantity
@RequestMapping("/api/admin")
public class AdminController {

    // AdminService contains the actual business logic for managing books. This controller delegates tasks to adminService.
    // The AdminService object is automatically created and provided by Spring using the @Autowired annotation.
    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/secure/increase/book/quantity")
    public void increaseBookQuantity(
            /*
            @RequestHeader extracts a value from the HTTP headers of the request. In this case the value we want to extract is the the value for "Authorization". There are numerous HTTP Headers in our file and they look like this:
                    Authorization: Bearer some.jwt.token
                    Content-Type: application/json

            Normally, I'm the one who's supposed to write the code to generate a JWT token BUT Okta already handles that so there's no use for me to do so. The value for the Authorization token is created by Okta. So when a user gets logged into the app, Okta generates this Authorization value/token.

            But how is SPringboot able to generate this token value? - When you go th=o the "application.properties" file, these values enable the token/authorization value to be generated when a user logs into the app:
                    okta.oauth2.client-id=0xxxxxxxxxxxxx
                    okta.oauth2.issuer=https://dev-XXXXXXX.okta.com/oauth2/default


            String token:
            The String token parameter in the @RequestHeader annotation will automatically capture the value of the Authorization header from the incoming HTTP request. You, as the developer, don’t need to manually set or input a value here when the method is called.
             */
            @RequestHeader(value="Authorization") String token,

            /*
            @RequestParam extracts a value from the query string of the URL.

            PUT /api/admin/secure/increase/book/quantity?bookId=123
            Here, bookId=123 is a query parameter.

            @RequestParam Long bookId
                bookId: This matches the query parameter name (?bookId=123).
                The value is automatically parsed into the Long bookId variable.

            Why no value="..."?
                If the name of the query parameter in the URL (bookId) matches the variable name in the method (bookId), Spring Boot can figure it out automatically.
                If they don’t match, you can specify the parameter name explicitly:

                @RequestParam(value="bookId") Long id
             */
            @RequestParam Long bookId
    ) throws Exception {
        // Extracts the userType field from the JWT token. To ensure only users with the "admin" role can access this functionality.

        /*
        ExtractJWT: This is the name of the class containing the static method payloadJWTExtraction.
        payloadJWTExtraction(...): This is a method that extracts information from the payload of the JWT token.


        token: The token parameter contains the JWT/Okta token passed to the method.


        "\"userType\""
            This is the second parameter passed to the payloadJWTExtraction method.
            It represents the key (or claim name) in the JWT payload that we want to extract.
            The userType claim is likely part of the JWT payload, which may look like this after decoding:

            Okta (or any OAuth 2.0 / OpenID Connect provider) generates the JWT token, not your Spring Boot application. Here's what it looks like:
            {
                "sub": "john.doe@example.com",
                "userType": "admin",
                "iat": 1609459200,
                "exp": 1609462800
            }

            Here, "userType" has the value "admin". The method will extract "admin" from the payload.
         */
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");

        // This verifies that the user is an admin. If not, an exception is thrown, blocking the operation.
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("Administration page only");
        }

        // Calls a method in the AdminService to perform the actual task of increasing the book quantity.
        adminService.increaseBookQuantity(bookId);
    }

    @PutMapping("/secure/decrease/book/quantity")
    public void decreaseBookQuantity(
            @RequestHeader(value="Authorization") String token,
            @RequestParam Long bookId
    ) throws Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("Administration page only");
        }
        adminService.decreaseBookQuantity(bookId);
    }



    @PostMapping("/secure/add/book")
    public void postBook(
            @RequestHeader(value="Authorization") String token,
            // Notice we used @REQUESTBODY not @REQUESTPARAM
            // This is a deserialization annotation. It means the incoming request body (which should contain JSON data) will be automatically converted into an AddBookRequest object. The fields in the request body should match the fields in the AddBookRequest class (title, author, description, etc.). This is how the backend gets all the details needed to add a new book.
            @RequestBody AddBookRequest addBookRequest
    ) throws Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("Administration page only");
        }
        adminService.postBook(addBookRequest);
    }

    @DeleteMapping("/secure/delete/book")
    public void deleteBook(
            @RequestHeader(value="Authorization") String token,
            @RequestParam Long bookId
    ) throws Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("Administration page only");
        }
        adminService.deleteBook(bookId);
    }

}












