package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CartApiTest extends ApiBaseTest {

    private String userToken;
    private int userId;
    private int testProductId;

    @BeforeClass
    public void setupCartData() {
        // 1. Create a customer user for testing the cart
        String email = generateUniqueEmail();
        String password = "password123";
        registerUser(email, password, "CUSTOMER");
        
        Response loginRes = loginUserAndGetResponse(email, password);
        userToken = loginRes.path("token");
        userId = loginRes.path("userId");

        // 2. Fetch/create a product to add to cart
        Response prodRes = given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/products")
        .then()
            .statusCode(200)
            .extract().response();

        Integer totalProducts = prodRes.path("content.size()");
        if (totalProducts != null && totalProducts > 0) {
            testProductId = prodRes.path("content[0].id");
        } else {
            // No products exist, let's login as seller and create one
            given().contentType(ContentType.JSON).post("/api/auth/create-seller");
            String sellerToken = loginAndGetToken("seller@example.com", "seller123");
            String productPayload = """
                {
                    "name": "Cart Test Product",
                    "description": "Temp product for cart tests",
                    "price": 19.99,
                    "stockQuantity": 100,
                    "category": "Home",
                    "enabled": true
                }
                """;
            Response createRes = givenAuth(sellerToken)
                .body(productPayload)
            .when()
                .post("/api/products")
            .then()
                .statusCode(201)
                .extract().response();
            testProductId = createRes.path("id");
        }
    }

    @Test(priority = 1)
    public void testGetCartEmpty() {
        givenAuth(userToken)
            .queryParam("userId", userId)
        .when()
            .get("/api/cart")
        .then()
            .statusCode(200)
            .body("size()", is(0));
    }

    @Test(priority = 2)
    public void testAddToCart() {
        givenAuth(userToken)
            .queryParam("userId", userId)
            .queryParam("productId", testProductId)
            .queryParam("quantity", 3)
        .when()
            .post("/api/cart/add")
        .then()
            .statusCode(200)
            .body(notNullValue());
    }

    @Test(priority = 3)
    public void testGetCartWithItems() {
        givenAuth(userToken)
            .queryParam("userId", userId)
        .when()
            .get("/api/cart")
        .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].productId", equalTo(testProductId))
            .body("[0].quantity", equalTo(3));
    }

    @Test(priority = 4)
    public void testUpdateCartQuantity() {
        givenAuth(userToken)
            .queryParam("userId", userId)
            .queryParam("productId", testProductId)
            .queryParam("quantity", 5)
        .when()
            .put("/api/cart/update")
        .then()
            .statusCode(200);

        // Verify updated quantity
        givenAuth(userToken)
            .queryParam("userId", userId)
        .when()
            .get("/api/cart")
        .then()
            .statusCode(200)
            .body("[0].quantity", equalTo(5));
    }

    @Test(priority = 5)
    public void testRemoveFromCart() {
        givenAuth(userToken)
            .queryParam("userId", userId)
            .queryParam("productId", testProductId)
        .when()
            .delete("/api/cart/remove")
        .then()
            .statusCode(200);

        // Verify empty cart
        givenAuth(userToken)
            .queryParam("userId", userId)
        .when()
            .get("/api/cart")
        .then()
            .statusCode(200)
            .body("size()", is(0));
    }

    @Test(priority = 6)
    public void testClearCart() {
        // Add item again
        givenAuth(userToken)
            .queryParam("userId", userId)
            .queryParam("productId", testProductId)
            .queryParam("quantity", 2)
        .when()
            .post("/api/cart/add")
        .then()
            .statusCode(200);

        // Clear cart
        givenAuth(userToken)
            .queryParam("userId", userId)
        .when()
            .delete("/api/cart/clear")
        .then()
            .statusCode(200);

        // Verify empty
        givenAuth(userToken)
            .queryParam("userId", userId)
        .when()
            .get("/api/cart")
        .then()
            .statusCode(200)
            .body("size()", is(0));
    }
}
