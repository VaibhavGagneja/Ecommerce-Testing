package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProfileApiTest extends ApiBaseTest {

    private String userToken;
    private String userEmail;
    private String userPassword = "password123";

    @BeforeClass
    public void setupProfileData() {
        userEmail = generateUniqueEmail();
        registerUser(userEmail, userPassword, "CUSTOMER");
        userToken = loginAndGetToken(userEmail, userPassword);
    }

    @Test(priority = 1)
    public void testGetProfileMe() {
        givenAuth(userToken)
        .when()
            .get("/api/profile/me")
        .then()
            .statusCode(200)
            .body("email", equalTo(userEmail))
            .body("fullName", equalTo("Test User"));
    }

    @Test(priority = 2)
    public void testUpdateProfile() {
        String updatePayload = """
            {
                "fullName": "API Updated Name",
                "darkMode": true,
                "marketingNotifications": false
            }
            """;

        Response res = givenAuth(userToken)
            .body(updatePayload)
        .when()
            .put("/api/profile/me")
        .then()
            .statusCode(200)
            .body("user.fullName", equalTo("API Updated Name"))
            .body("user.darkMode", equalTo(true))
            .body("token", notNullValue())
            .extract().response();

        // Update the token since profile update issues a new token
        userToken = res.path("token");
    }

    @Test(priority = 3)
    public void testChangePassword() {
        String newPassword = "newPassword123";
        String payload = String.format("""
            {
                "currentPassword": "%s",
                "newPassword": "%s"
            }
            """, userPassword, newPassword);

        givenAuth(userToken)
            .body(payload)
        .when()
            .post("/api/profile/change-password")
        .then()
            .statusCode(200)
            .body("message", containsString("successfully"));

        // Verify login works with new password
        String tempToken = loginAndGetToken(userEmail, newPassword);
        assertThat(tempToken, notNullValue());

        // Restore password for other tests
        String restorePayload = String.format("""
            {
                "currentPassword": "%s",
                "newPassword": "%s"
            }
            """, newPassword, userPassword);

        givenAuth(tempToken)
            .body(restorePayload)
        .when()
            .post("/api/profile/change-password")
        .then()
            .statusCode(200);

        // Refresh original userToken as the password change incremented the token version
        userToken = loginAndGetToken(userEmail, userPassword);
    }

    @Test(priority = 4)
    public void testAddressLifecycle() {
        // 1. Create an address
        String addressPayload = """
            {
                "label": "Home Address",
                "fullName": "API Resident",
                "phoneNumber": "1234567890",
                "line1": "456 REST Avenue",
                "city": "Austin",
                "state": "TX",
                "pincode": "787001",
                "defaultAddress": false
            }
            """;

        Response createRes = givenAuth(userToken)
            .body(addressPayload)
        .when()
            .post("/api/profile/addresses")
        .then()
            .statusCode(200)
            .body("addressId", notNullValue())
            .body("label", equalTo("Home Address"))
            .extract().response();

        int addressId = createRes.path("addressId");

        // 2. Fetch list and verify contains newly created address
        givenAuth(userToken)
        .when()
            .get("/api/profile/addresses")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1))
            .body("addressId", hasItem(addressId));

        // 3. Make this address default
        givenAuth(userToken)
        .when()
            .patch("/api/profile/addresses/" + addressId + "/default")
        .then()
            .statusCode(200)
            .body("message", containsString("updated"));

        // 4. Delete address
        givenAuth(userToken)
        .when()
            .delete("/api/profile/addresses/" + addressId)
        .then()
            .statusCode(200)
            .body("message", containsString("deleted"));
    }

    @Test(priority = 5)
    public void testForgotPasswordAndResetFlow() {
        String emailToReset = generateUniqueEmail();
        registerUser(emailToReset, "oldPass123", "CUSTOMER");

        // 1. Request forgot password reset
        Response forgotRes = given()
            .contentType(ContentType.JSON)
            .body(Map.of("emailOrPhone", emailToReset))
        .when()
            .post("/api/profile/forgot-password")
        .then()
            .statusCode(200)
            .body("devResetToken", notNullValue())
            .extract().response();

        String resetToken = forgotRes.path("devResetToken");

        // 2. Use the token to reset the password
        String resetPayload = String.format("""
            {
                "token": "%s",
                "newPassword": "newSecretPass"
            }
            """, resetToken);

        given()
            .contentType(ContentType.JSON)
            .body(resetPayload)
        .when()
            .post("/api/profile/reset-password")
        .then()
            .statusCode(200)
            .body("message", containsString("successfully"));

        // 3. Verify login works with new password
        String newToken = loginAndGetToken(emailToReset, "newSecretPass");
        assertThat(newToken, notNullValue());
    }

    @Test(priority = 6)
    public void testVerificationOtpFlow() {
        // 1. Send OTP
        Response sendRes = givenAuth(userToken)
        .when()
            .post("/api/profile/verification/email/send")
        .then()
            .statusCode(200)
            .body("devOtp", notNullValue())
            .extract().response();

        String otp = sendRes.path("devOtp");

        // 2. Verify OTP
        String verifyPayload = String.format("""
            {
                "otp": "%s"
            }
            """, otp);

        givenAuth(userToken)
            .body(verifyPayload)
        .when()
            .post("/api/profile/verification/email/verify")
        .then()
            .statusCode(200)
            .body("message", containsString("completed"));
    }
}
