package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class DatabaseSteps {
    private Exception dbException;
    private double fetchedPrice;
    private int fetchedStock;
    private String uniqueEmail;
    private Long uniqueUserId;
    private Long insertedAddressId;

    @When("I attempt to insert a duplicate email user {string} directly into database")
    public void iAttemptToInsertADuplicateEmailUserDirectlyIntoDatabase(String email) {
        dbException = null;
        String query = "INSERT INTO users (email, phone_number, password_hash, full_name, role, enabled, email_verified, phone_verified, dark_mode, marketing_notifications, order_notifications, profile_private, token_version, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseManager.getConnection()) {
            DatabaseManager.executeUpdate(conn, query, email, "9999999999", "pwd", "Duplicate Admin", "ADMIN", true, false, false, false, true, true, false, 1);
        } catch (Exception e) {
            dbException = e;
        }
    }

    @Then("The database should reject the insert with a unique constraint error")
    public void theDatabaseShouldRejectTheInsertWithAUniqueConstraintError() {
        Assert.assertNotNull(dbException, "Expected a database constraint exception but none was thrown.");
        String errMsg = dbException.getMessage().toLowerCase();
        Assert.assertTrue(errMsg.contains("duplicate entry") || errMsg.contains("constraint"), "Expected unique constraint error, but got: " + dbException.getMessage());
    }

    @When("I query database for product name {string}")
    public void iQueryDatabaseForProductName(String productName) throws SQLException {
        String query = "SELECT price, stock_quantity FROM products WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (ResultSet rs = DatabaseManager.executeQuery(conn, query, productName)) {
                if (rs.next()) {
                    fetchedPrice = rs.getDouble("price");
                    fetchedStock = rs.getInt("stock_quantity");
                } else {
                    Assert.fail("Product not found in database: " + productName);
                }
            }
        }
    }

    @Then("The database should return the product price {string} and stock greater than {string}")
    public void theDatabaseShouldReturnTheProductPriceAndStockGreaterThan(String expectedPrice, String minStock) {
        double expPrice = Double.parseDouble(expectedPrice);
        int mStock = Integer.parseInt(minStock);
        Assert.assertEquals(fetchedPrice, expPrice, 0.01, "Product price mismatch in database.");
        Assert.assertTrue(fetchedStock > mStock, "Product stock should be greater than " + minStock + ", but is: " + fetchedStock);
    }

    @Given("A unique user email exists in the database")
    public void aUniqueUserEmailExistsInTheDatabase() throws SQLException {
        Random rand = new Random();
        int randomId = rand.nextInt(1000000);
        uniqueEmail = "dbtest_" + randomId + "@example.com";
        String randomPhone = String.format("%010d", randomId);
        String insertUserQuery = "INSERT INTO users (email, phone_number, password_hash, full_name, role, enabled, email_verified, phone_verified, dark_mode, marketing_notifications, order_notifications, profile_private, token_version, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DatabaseManager.getConnection()) {
            DatabaseManager.executeUpdate(conn, insertUserQuery, uniqueEmail, randomPhone, "pwd", "DB Test User", "CUSTOMER", true, false, false, false, true, true, false, 1);

            String selectUserQuery = "SELECT user_id FROM users WHERE email = ?";
            try (ResultSet rs = DatabaseManager.executeQuery(conn, selectUserQuery, uniqueEmail)) {
                if (rs.next()) {
                    uniqueUserId = rs.getLong("user_id");
                } else {
                    Assert.fail("Failed to retrieve user ID for created user: " + uniqueEmail);
                }
            }
        }
    }

    @When("I insert a test address with label {string}, city {string}, state {string}, pincode {string}")
    public void iInsertATestAddressWithLabelCityStatePincode(String label, String city, String state, String pincode) throws SQLException {
        String insertAddressQuery = "INSERT INTO user_addresses (user_id, label, full_name, phone_number, line1, line2, city, state, pincode, latitude, longitude, default_address, created_at, updated_at) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

        try (Connection conn = DatabaseManager.getConnection()) {
            DatabaseManager.executeUpdate(conn, insertAddressQuery, uniqueUserId, label, "Jane Doe", "9876543210", "123 Main St", "Apt 4B", city, state, pincode, 0.0, 0.0, false);

            String selectAddressQuery = "SELECT address_id FROM user_addresses WHERE user_id = ? AND label = ?";
            try (ResultSet rs = DatabaseManager.executeQuery(conn, selectAddressQuery, uniqueUserId, label)) {
                if (rs.next()) {
                    insertedAddressId = rs.getLong("address_id");
                } else {
                    Assert.fail("Failed to retrieve address ID for label: " + label);
                }
            }
        }
    }

    @Then("The address should exist in the database with label {string}")
    public void theAddressShouldExistInTheDatabaseWithLabel(String expectedLabel) throws SQLException {
        String query = "SELECT label FROM user_addresses WHERE address_id = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (ResultSet rs = DatabaseManager.executeQuery(conn, query, insertedAddressId)) {
                if (rs.next()) {
                    String actualLabel = rs.getString("label");
                    Assert.assertEquals(actualLabel, expectedLabel, "Address label mismatch in database.");
                } else {
                    Assert.fail("Address card not found in database for ID: " + insertedAddressId);
                }
            }
        }
    }

    @When("I update the address label to {string}")
    public void iUpdateTheAddressLabelTo(String newLabel) throws SQLException {
        String query = "UPDATE user_addresses SET label = ?, updated_at = NOW() WHERE address_id = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            DatabaseManager.executeUpdate(conn, query, newLabel, insertedAddressId);
        }
    }

    @When("I delete the test address")
    public void iDeleteTheTestAddress() throws SQLException {
        String query = "DELETE FROM user_addresses WHERE address_id = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            DatabaseManager.executeUpdate(conn, query, insertedAddressId);
        }
    }

    @Then("The address should not exist in the database")
    public void theAddressShouldNotExistInTheDatabase() throws SQLException {
        String query = "SELECT COUNT(*) FROM user_addresses WHERE address_id = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (ResultSet rs = DatabaseManager.executeQuery(conn, query, insertedAddressId)) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    Assert.assertEquals(count, 0, "Address record should have been deleted, but still exists.");
                }
            }
        }

        String cleanupUserQuery = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            DatabaseManager.executeUpdate(conn, cleanupUserQuery, uniqueUserId);
        }
    }

    @When("I attempt to insert a duplicate phone user with phone {string} directly into database")
    public void iAttemptToInsertADuplicatePhoneUserWithPhoneDirectlyIntoDatabase(String phone) {
        dbException = null;
        String query = "INSERT INTO users (email, phone_number, password_hash, full_name, role, enabled, email_verified, phone_verified, dark_mode, marketing_notifications, order_notifications, profile_private, token_version, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseManager.getConnection()) {
            // First clean up if any leftover test users exist
            try {
                String cleanQuery = "DELETE FROM users WHERE email IN ('first_phone_test@example.com', 'second_phone_test@example.com')";
                DatabaseManager.executeUpdate(conn, cleanQuery);
            } catch (SQLException ex) {
                // ignore
            }

            // Insert first user with unique email but matching phone number
            DatabaseManager.executeUpdate(conn, query, "first_phone_test@example.com", phone, "pwd", "Phone User 1", "CUSTOMER", true, false, false, false, true, true, false, 1);

            // Attempt to insert second user with different email but SAME phone number
            DatabaseManager.executeUpdate(conn, query, "second_phone_test@example.com", phone, "pwd", "Phone User 2", "CUSTOMER", true, false, false, false, true, true, false, 1);
        } catch (Exception e) {
            dbException = e;
        } finally {
            // Clean up the first and second user
            try (Connection conn = DatabaseManager.getConnection()) {
                String cleanQuery = "DELETE FROM users WHERE email IN ('first_phone_test@example.com', 'second_phone_test@example.com')";
                DatabaseManager.executeUpdate(conn, cleanQuery);
            } catch (SQLException ex) {
                // ignore
            }
        }
    }

    @When("I attempt to insert a cart item referencing a non-existent product ID {string} directly into database")
    public void iAttemptToInsertACartItemReferencingANonExistentProductIDDirectlyIntoDatabase(String prodId) {
        dbException = null;
        // To trigger a foreign key constraint violation on cart_items, we use a non-existent cart_id (999999L)
        String query = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            DatabaseManager.executeUpdate(conn, query, 999999L, Long.parseLong(prodId), 1);
        } catch (Exception e) {
            dbException = e;
        }
    }

    @Then("The database should reject the insert with a foreign key constraint error")
    public void theDatabaseShouldRejectTheInsertWithAForeignKeyConstraintError() {
        Assert.assertNotNull(dbException, "Expected a database constraint exception but none was thrown.");
        String errMsg = dbException.getMessage().toLowerCase();
        Assert.assertTrue(errMsg.contains("foreign key") || errMsg.contains("cannot add or update a child row"), "Expected foreign key constraint error, but got: " + dbException.getMessage());
    }

    @When("I register a new customer in the database with email {string} and phone {string}")
    public void iRegisterANewCustomerInTheDatabaseWithEmailAndPhone(String email, String phone) throws SQLException {
        String query = "INSERT INTO users (email, phone_number, password_hash, full_name, role, enabled, email_verified, phone_verified, dark_mode, marketing_notifications, order_notifications, profile_private, token_version, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseManager.getConnection()) {
            DatabaseManager.executeUpdate(conn, query, email, phone, "pwd", "New Customer", "CUSTOMER", true, false, false, false, true, true, false, 1);
        }
    }

    @Then("I should see the user record in the database with email {string} and status {string}")
    public void iShouldSeeTheUserRecordInTheDatabaseWithEmailAndStatus(String email, String status) throws SQLException {
        String query = "SELECT email_verified, phone_verified FROM users WHERE email = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (ResultSet rs = DatabaseManager.executeQuery(conn, query, email)) {
                if (rs.next()) {
                    boolean emailVerified = rs.getBoolean("email_verified");
                    boolean phoneVerified = rs.getBoolean("phone_verified");
                    if ("Not Verified".equalsIgnoreCase(status)) {
                        Assert.assertFalse(emailVerified, "Email should be unverified.");
                        Assert.assertFalse(phoneVerified, "Phone should be unverified.");
                    } else {
                        Assert.assertTrue(emailVerified, "Email should be verified.");
                        Assert.assertTrue(phoneVerified, "Phone should be verified.");
                    }
                } else {
                    Assert.fail("User record not found in database: " + email);
                }
            }
        }
    }

    @When("I simulate generating an email OTP for {string}")
    public void iSimulateGeneratingAnEmailOTPFor(String email) throws SQLException {
        String query = "UPDATE users SET email_otp_hash = ?, email_otp_expires_at = TIMESTAMPADD(MINUTE, 10, NOW()) WHERE email = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            DatabaseManager.executeUpdate(conn, query, "dummy_hash_code", email);
        }
    }

    @Then("The email OTP hash and expiration time should be populated in the database")
    public void theEmailOTPHashAndExpirationTimeShouldBePopulatedInTheDatabase() throws SQLException {
        String query = "SELECT email_otp_hash, email_otp_expires_at FROM users WHERE email = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (ResultSet rs = DatabaseManager.executeQuery(conn, query, "newcustomer@example.com")) {
                if (rs.next()) {
                    String hash = rs.getString("email_otp_hash");
                    java.sql.Timestamp expiry = rs.getTimestamp("email_otp_expires_at");
                    Assert.assertNotNull(hash, "OTP hash should not be null.");
                    Assert.assertNotNull(expiry, "OTP expiry time should not be null.");
                } else {
                    Assert.fail("User newcustomer@example.com not found.");
                }
            }
        }
    }

    @When("I clean up the customer {string} from the database")
    public void iCleanUpTheCustomerFromTheDatabase(String email) throws SQLException {
        cleanupUserAndAllData(email);
    }

    @Then("I should see in the database that this customer's cart contains {string} unit of {string}")
    public void iShouldSeeInTheDatabaseThatThisCustomerSCartContainsUnitOf(String quantity, String productName) throws SQLException {
        int expectedQty = Integer.parseInt(quantity);
        String query = "SELECT ci.quantity FROM cart_items ci " + "JOIN cart c ON ci.cart_id = c.id " + "JOIN users u ON c.user_id = u.user_id " + "JOIN products p ON ci.product_id = p.product_id " + "WHERE u.email = ? AND p.name = ?";

        String customerEmail = CheckoutSteps.getLastRegisteredEmail();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (ResultSet rs = DatabaseManager.executeQuery(conn, query, customerEmail, productName)) {
                if (rs.next()) {
                    int actualQty = rs.getInt("quantity");
                    Assert.assertEquals(actualQty, expectedQty, "Cart item quantity mismatch in database.");
                } else {
                    Assert.fail("No cart item found in database for user: " + customerEmail + " and product: " + productName);
                }
            }
        }
    }

    private int initialStock;

    @Given("I record the current database stock of {string}")
    public void iRecordTheCurrentDatabaseStockOf(String productName) throws SQLException {
        String query = "SELECT stock_quantity FROM products WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (ResultSet rs = DatabaseManager.executeQuery(conn, query, productName)) {
                if (rs.next()) {
                    initialStock = rs.getInt("stock_quantity");
                } else {
                    Assert.fail("Product not found to record stock: " + productName);
                }
            }
        }
    }

    @Then("I should see the placed order in the database with status {string}")
    public void iShouldSeeThePlacedOrderInTheDatabaseWithStatus(String expectedStatus) throws SQLException {
        String query = "SELECT status FROM orders WHERE user_id = (SELECT user_id FROM users WHERE email = ?) ORDER BY order_date DESC LIMIT 1";
        String customerEmail = CheckoutSteps.getLastRegisteredEmail();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (ResultSet rs = DatabaseManager.executeQuery(conn, query, customerEmail)) {
                if (rs.next()) {
                    String actualStatus = rs.getString("status");
                    Assert.assertEquals(actualStatus, expectedStatus, "Order status mismatch in database.");
                } else {
                    Assert.fail("No order found in database for user: " + customerEmail);
                }
            }
        }
    }

    @Then("I should see the order item count as {string} referencing {string} in the database")
    public void iShouldSeeTheOrderItemCountAsReferencingInTheDatabase(String expectedCount, String productName) throws SQLException {
        int expCount = Integer.parseInt(expectedCount);
        String query = "SELECT COUNT(*) FROM order_items oi " + "JOIN orders o ON oi.order_id = o.order_id " + "JOIN users u ON o.user_id = u.user_id " + "JOIN products p ON oi.product_id = p.product_id " + "WHERE u.email = ? AND p.name = ?";

        String customerEmail = CheckoutSteps.getLastRegisteredEmail();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (ResultSet rs = DatabaseManager.executeQuery(conn, query, customerEmail, productName)) {
                if (rs.next()) {
                    int actualCount = rs.getInt(1);
                    Assert.assertEquals(actualCount, expCount, "Order item count mismatch in database.");
                }
            }
        }
    }

    @Then("I should verify in the database that the product stock of {string} is decremented by {int}")
    public void iShouldVerifyInTheDatabaseThatTheProductStockOfIsDecrementedBy(String productName, int decrement) throws SQLException {
        String query = "SELECT stock_quantity FROM products WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (ResultSet rs = DatabaseManager.executeQuery(conn, query, productName)) {
                if (rs.next()) {
                    int currentStock = rs.getInt("stock_quantity");
                    Assert.assertEquals(currentStock, initialStock - decrement, "Product stock was not decremented correctly in database.");
                } else {
                    Assert.fail("Product not found to verify stock: " + productName);
                }
            }
        }
    }

    @When("I clean up the customer cart and account from the database")
    public void iCleanUpTheCustomerCartAndAccountFromTheDatabase() throws SQLException {
        String email = CheckoutSteps.getLastRegisteredEmail();
        if (email != null) {
            cleanupUserAndAllData(email);
        }
    }

    private void cleanupUserAndAllData(String email) throws SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            Long userId = null;
            String selectUser = "SELECT user_id FROM users WHERE email = ?";
            try (ResultSet rs = DatabaseManager.executeQuery(conn, selectUser, email)) {
                if (rs.next()) {
                    userId = rs.getLong("user_id");
                }
            }
            if (userId != null) {
                // Delete cart items
                String deleteCartItems = "DELETE FROM cart_items WHERE cart_id IN (SELECT id FROM cart WHERE user_id = ?)";
                DatabaseManager.executeUpdate(conn, deleteCartItems, userId);
                // Delete cart
                String deleteCart = "DELETE FROM cart WHERE user_id = ?";
                DatabaseManager.executeUpdate(conn, deleteCart, userId);
                // Delete order items
                String deleteOrderItems = "DELETE FROM order_items WHERE order_id IN (SELECT order_id FROM orders WHERE user_id = ?)";
                DatabaseManager.executeUpdate(conn, deleteOrderItems, userId);
                // Delete orders
                String deleteOrders = "DELETE FROM orders WHERE user_id = ?";
                DatabaseManager.executeUpdate(conn, deleteOrders, userId);
                // Delete addresses
                String deleteAddresses = "DELETE FROM user_addresses WHERE user_id = ?";
                DatabaseManager.executeUpdate(conn, deleteAddresses, userId);
                // Delete user
                String deleteUser = "DELETE FROM users WHERE user_id = ?";
                DatabaseManager.executeUpdate(conn, deleteUser, userId);
            }
        }
    }
}
