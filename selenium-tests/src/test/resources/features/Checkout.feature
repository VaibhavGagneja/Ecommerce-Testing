@Regression
Feature: Product Checkout
  As a customer of EcoMart
  I want to search a product, add it to the cart
  And complete a Cash on Delivery order checkout

  @Smoke @E2E
  Scenario: E2E Checkout Flow with Cash on Delivery
    Given I register and log in a new customer account
    When I search for the product "Logitech MX Master 3S Wireless Mouse"
    And I open the product page and add it to my cart
    And I navigate to the cart page and proceed to checkout
    And I enter my shipping address name "John Doe", phone "9876543210", pincode "110001", city "New Delhi", state "Delhi", address "Connaught Place B-12"
    And I select Cash on Delivery and submit the order
    Then I should see the order success confirmation screen
