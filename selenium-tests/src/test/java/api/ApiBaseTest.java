package api;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class ApiBaseTest {

    protected static final String BASE_URL = "http://localhost:8080";
    protected static final Logger logger = LoggerFactory.getLogger(ApiBaseTest.class);

    @BeforeSuite
    public void setupSuite() {
        RestAssured.baseURI = BASE_URL;
        // Register global request and response logging filters
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        logger.info("Initializing REST Assured Test Suite targeting: {}", BASE_URL);
    }

    /**
     * Helper to generate a unique email address for test isolation.
     */
    protected String generateUniqueEmail() {
        return "testuser_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    /**
     * Dynamically registers a new user with standard details.
     * Returns the registered user email.
     */
    protected String registerUser(String email, String password, String role) {
        String payload = String.format("""
            {
                "fullName": "Test User",
                "email": "%s",
                "phoneNumber": "%s",
                "password": "%s",
                "confirmPassword": "%s",
                "gender": "MALE"
            }
            """, email, generateRandomPhoneNumber(), password, password);

        String registerEndpoint = "/api/auth/register";
        
        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post(registerEndpoint)
        .then()
            .statusCode(201);

        return email;
    }

    /**
     * Authenticates the user and returns the JWT token.
     */
    protected String loginAndGetToken(String email, String password) {
        return loginUserAndGetResponse(email, password).path("token");
    }

    /**
     * Authenticates the user and returns the full Response object.
     */
    protected Response loginUserAndGetResponse(String email, String password) {
        String payload = String.format("""
            {
                "emailOrPhone": "%s",
                "password": "%s"
            }
            """, email, password);

        return given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(200)
            .extract().response();
    }

    /**
     * Helper to get a RequestSpecification loaded with the Authorization Bearer token header.
     */
    protected RequestSpecification givenAuth(String token) {
        return given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON);
    }

    /**
     * Generates a random 10-digit phone number.
     */
    protected String generateRandomPhoneNumber() {
        long number = (long) (Math.random() * 9000000000L) + 1000000000L;
        return String.valueOf(number);
    }
}
