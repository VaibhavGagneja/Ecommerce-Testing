package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductApiTest extends ApiBaseTest {

    private String sellerToken;
    private String adminToken;

    @BeforeClass
    public void setupAuth() {
        // Ensure seller account exists and login
        given().contentType(ContentType.JSON).post("/api/auth/create-seller");
        sellerToken = loginAndGetToken("seller@example.com", "seller123");

        // Ensure admin account exists and login
        given().contentType(ContentType.JSON).post("/api/auth/create-admin");
        adminToken = loginAndGetToken("adarsht072@gmail.com", "Adarsh@123");
    }

    @Test
    public void testGetAllProducts() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/products")
        .then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("totalPages", notNullValue());
    }

    @Test
    public void testGetProductsByCategory() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/products/category/Electronics")
        .then()
            .statusCode(200)
            .body("content", notNullValue());
    }

    @Test
    public void testFilterProducts() {
        given()
            .contentType(ContentType.JSON)
            .queryParam("category", "Electronics")
            .queryParam("minPrice", 10.0)
            .queryParam("maxPrice", 10000.0)
        .when()
            .get("/api/products/filter")
        .then()
            .statusCode(200)
            .body("content", notNullValue());
    }

    @Test
    public void testSearchProducts() {
        given()
            .contentType(ContentType.JSON)
            .queryParam("keyword", "phone")
        .when()
            .get("/api/products/search")
        .then()
            .statusCode(200)
            .body("content", notNullValue());
    }

    @Test
    public void testSellerProductLifecycle() {
        // 1. Create product as seller
        String productPayload = """
            {
                "name": "API Test Phone",
                "description": "Smart phone created via API test",
                "price": 499.99,
                "stockQuantity": 100,
                "imageUrl": "http://example.com/phone.png",
                "category": "Electronics",
                "brand": "APITech",
                "enabled": true
            }
            """;

        Response response = givenAuth(sellerToken)
            .body(productPayload)
        .when()
            .post("/api/products")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", equalTo("API Test Phone"))
            .body("price", equalTo(499.99f))
            .extract().response();

        int productId = response.path("id");

        // 2. Read product publicly by ID
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/products/" + productId)
        .then()
            .statusCode(200)
            .body("id", equalTo(productId))
            .body("description", equalTo("Smart phone created via API test"));

        // 3. Update product as seller
        String updatedPayload = """
            {
                "name": "API Test Phone Pro",
                "description": "Updated description",
                "price": 599.99,
                "stockQuantity": 80,
                "imageUrl": "http://example.com/phone-pro.png",
                "category": "Electronics",
                "brand": "APITech",
                "enabled": true
            }
            """;

        givenAuth(sellerToken)
            .body(updatedPayload)
        .when()
            .put("/api/products/" + productId)
        .then()
            .statusCode(200)
            .body("name", equalTo("API Test Phone Pro"))
            .body("price", equalTo(599.99f))
            .body("stockQuantity", equalTo(80));

        // 4. Delete product as seller
        givenAuth(sellerToken)
        .when()
            .delete("/api/products/" + productId)
        .then()
            .statusCode(200)
            .body(containsString("deleted successfully"));

        // 5. Verify product no longer exists
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/products/" + productId)
        .then()
            .statusCode(404);
    }

    @Test
    public void testAdminProductToggle() {
        // Create product first
        String productPayload = """
            {
                "name": "API Toggle Test Item",
                "description": "Item to test admin toggle",
                "price": 29.99,
                "stockQuantity": 150,
                "category": "Home",
                "enabled": true
            }
            """;

        Response createResponse = givenAuth(sellerToken)
            .body(productPayload)
        .when()
            .post("/api/products")
        .then()
            .statusCode(201)
            .extract().response();

        int productId = createResponse.path("id");

        // Toggle to disabled as Admin
        givenAuth(adminToken)
        .when()
            .patch("/api/products/" + productId + "/toggle")
        .then()
            .statusCode(200)
            .body(containsString("status updated"));

        // Verify product is now disabled (should not return in public get)
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/products/" + productId)
        .then()
            .statusCode(200)
            .body("enabled", equalTo(false));

        // Cleanup: delete product
        givenAuth(adminToken)
        .when()
            .delete("/api/products/" + productId)
        .then()
            .statusCode(200);
    }
}
