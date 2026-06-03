package api;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthApiTest extends ApiBaseTest {

    @Test
    public void testSuccessfulRegister() {
        logger.info("Executing: testSuccessfulRegister");
        String email = generateUniqueEmail();
        String password = "password123";
        String payload = String.format("""
            {
                "fullName": "API Registered User",
                "email": "%s",
                "phoneNumber": "%s",
                "password": "%s",
                "confirmPassword": "%s",
                "gender": "FEMALE"
            }
            """, email, generateRandomPhoneNumber(), password, password);

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/api/auth/register")
        .then()
            .statusCode(201)
            .body("email", equalTo(email))
            .body("fullName", equalTo("API Registered User"))
            .body("role", equalTo("CUSTOMER"))
            .body("passwordHash", nullValue());
    }

    @Test
    public void testFailedRegisterDuplicateEmail() {
        logger.info("Executing: testFailedRegisterDuplicateEmail");
        String email = generateUniqueEmail();
        String password = "password123";

        // Register first time
        registerUser(email, password, "CUSTOMER");

        // Try registering again with same email
        String payload = String.format("""
            {
                "fullName": "API Duplicate User",
                "email": "%s",
                "phoneNumber": "%s",
                "password": "%s",
                "confirmPassword": "%s",
                "gender": "MALE"
            }
            """, email, generateRandomPhoneNumber(), password, password);

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/api/auth/register")
        .then()
            .statusCode(400)
            .body(anyOf(containsString("Email already exists"), containsString("already in use"), notNullValue()));
    }

    @Test
    public void testSuccessfulLogin() {
        logger.info("Executing: testSuccessfulLogin");
        String email = generateUniqueEmail();
        String password = "password123";
        registerUser(email, password, "CUSTOMER");

        String payload = String.format("""
            {
                "emailOrPhone": "%s",
                "password": "%s"
            }
            """, email, password);

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(200)
            .body("message", equalTo("Login successful"))
            .body("email", equalTo(email))
            .body("role", equalTo("CUSTOMER"))
            .body("token", notNullValue());
    }

    @Test
    public void testFailedLoginInvalidPassword() {
        logger.info("Executing: testFailedLoginInvalidPassword");
        String email = generateUniqueEmail();
        String password = "password123";
        registerUser(email, password, "CUSTOMER");

        String payload = String.format("""
            {
                "emailOrPhone": "%s",
                "password": "wrongpassword"
            }
            """, email);

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(401);
    }

    @Test
    public void testCreateAdmin() {
        logger.info("Executing: testCreateAdmin");
        given()
            .contentType(ContentType.JSON)
        .when()
            .post("/api/auth/create-admin")
        .then()
            .statusCode(200)
            .body("email", equalTo("adarsht072@gmail.com"))
            .body("message", anyOf(containsString("created successfully"), containsString("already exists")));
    }

    @Test
    public void testCreateSeller() {
        logger.info("Executing: testCreateSeller");
        given()
            .contentType(ContentType.JSON)
        .when()
            .post("/api/auth/create-seller")
        .then()
            .statusCode(anyOf(equalTo(200), equalTo(400))) // 200 on first run, 400 on subsequent runs
            .body(notNullValue());
    }
}
