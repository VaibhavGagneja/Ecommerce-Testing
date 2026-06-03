@Database
Feature: Database Direct Verification
  As a QA engineer
  I want to verify database states directly and E2E synchronization
  So that database logic behaves correctly and mirrors UI actions

  Scenario: Verify database unique constraint for emails
    When I attempt to insert a duplicate email user "adarsht072@gmail.com" directly into database
    Then The database should reject the insert with a unique constraint error

  Scenario: Verify database unique constraint for phone number
    When I attempt to insert a duplicate phone user with phone "9999999999" directly into database
    Then The database should reject the insert with a unique constraint error

  Scenario: Verify database foreign key constraint
    When I attempt to insert a cart item referencing a non-existent product ID "99999" directly into database
    Then The database should reject the insert with a foreign key constraint error

  Scenario: Verify database product retrieval and details
    When I query database for product name "Logitech MX Master 3S Wireless Mouse"
    Then The database should return the product price "9495" and stock greater than "0"

  Scenario: Verify database address table changes
    Given A unique user email exists in the database
    When I insert a test address with label "DatabaseHome", city "Mumbai", state "Maharashtra", pincode "400001"
    Then The address should exist in the database with label "DatabaseHome"
    When I update the address label to "DatabaseOffice"
    Then The address should exist in the database with label "DatabaseOffice"
    When I delete the test address
    Then The address should not exist in the database

  Scenario: Verify database OTP hash updates and verification flow
    When I register a new customer in the database with email "newcustomer@example.com" and phone "9876543210"
    Then I should see the user record in the database with email "newcustomer@example.com" and status "Not Verified"
    When I simulate generating an email OTP for "newcustomer@example.com"
    Then The email OTP hash and expiration time should be populated in the database
    When I clean up the customer "newcustomer@example.com" from the database

  Scenario: Verify cart synchronization between UI and database
    Given I register and log in a new customer account
    When I search for the product "Logitech MX Master 3S Wireless Mouse"
    And I open the product page and add it to my cart
    Then I should see in the database that this customer's cart contains "1" unit of "Logitech MX Master 3S Wireless Mouse"
    When I clean up the customer cart and account from the database

  Scenario: Verify order checkout database records and stock decrement
    Given I register and log in a new customer account
    And I record the current database stock of "Logitech MX Master 3S Wireless Mouse"
    When I search for the product "Logitech MX Master 3S Wireless Mouse"
    And I open the product page and add it to my cart
    And I navigate to the cart page and proceed to checkout
    And I enter my shipping address name "John Doe", phone "9876543210", pincode "110001", city "New Delhi", state "Delhi", address "Connaught Place B-12"
    And I select Cash on Delivery and submit the order
    Then I should see the order success confirmation screen
    And I should see the placed order in the database with status "PENDING"
    And I should see the order item count as "1" referencing "Logitech MX Master 3S Wireless Mouse" in the database
    And I should verify in the database that the product stock of "Logitech MX Master 3S Wireless Mouse" is decremented by 1
    When I clean up the customer cart and account from the database
