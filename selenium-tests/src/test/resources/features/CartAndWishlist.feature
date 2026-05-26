@Cart @Wishlist
Feature: Cart and Wishlist Management
  As an EcoMart customer
  I want to manage my cart items and wishlisted products
  So that I can easily plan my purchase

  Scenario: Add and update item quantities in cart
    Given I register and log in a new customer account
    When I search for the product "Logitech MX Master 3S Wireless Mouse"
    And I open the product page
    And I add the product to my cart
    Then The cart badge count should be "1"
    When I navigate to the cart page
    Then I should see the product "Logitech MX Master 3S Wireless Mouse" in the cart with quantity "1"
    When I increment the quantity of "Logitech MX Master 3S Wireless Mouse" in the cart
    Then I should see the product "Logitech MX Master 3S Wireless Mouse" in the cart with quantity "2"
    When I decrement the quantity of "Logitech MX Master 3S Wireless Mouse" in the cart
    Then I should see the product "Logitech MX Master 3S Wireless Mouse" in the cart with quantity "1"
    When I decrement the quantity of "Logitech MX Master 3S Wireless Mouse" in the cart
    Then I should see the empty cart message

  Scenario: Restrict Admin user from adding items to cart
    Given I navigate to the login page
    And I submit username "adarsht072@gmail.com" and password "Adarsh@123"
    When I search for the product "Logitech MX Master 3S Wireless Mouse"
    And I open the product page
    Then I should see the admin preview mode message "Admin preview mode: checkout, wishlist and cart actions are hidden."

  Scenario: Toggle wishlist item and view on wishlist page
    Given I register and log in a new customer account
    When I search for the product "Logitech MX Master 3S Wireless Mouse"
    And I click the wishlist heart button on the product card
    And I click the Wishlist link in the navbar
    Then I should see "Logitech MX Master 3S Wireless Mouse" in the wishlist list
    When I click the wishlist heart button on the wishlist page product card
    Then I should see the empty wishlist message

  Scenario: Redirect Guest user attempting to access wishlist
    Given I navigate to the home page as a guest
    When I directly navigate to the "/wishlist" URL
    Then I should be redirected to the login page
