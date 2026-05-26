@Checkout @Orders
Feature: Checkout and Orders
  As an EcoMart customer
  I want my checkout pages to be secure
  And I want to be able to cancel my pending orders

  Scenario: Prevent empty cart checkout access
    Given I register and log in a new customer account
    And I navigate to the cart page
    Then I should see the empty cart message
    When I directly navigate to the "/checkout" URL
    Then I should be redirected to the cart page

  Scenario: Place and cancel a pending order
    Given I register and log in a new customer account
    When I search for the product "Logitech MX Master 3S Wireless Mouse"
    And I open the product page and add it to my cart
    And I navigate to the cart page and proceed to checkout
    And I enter my shipping address name "John Doe", phone "9876543210", pincode "110001", city "New Delhi", state "Delhi", address "Connaught Place B-12"
    And I select Cash on Delivery and submit the order
    Then I should see the order success confirmation screen
    When I click the View Orders button
    Then I should see the order in PENDING status
    When I cancel the order with reason "Order placed by mistake"
    Then I should see the order status change to "CANCELLED"
