@Coverage @Search @Cart
Feature: Search and Cart Edge Cases
  As a user
  I want to search safely and interact with the cart
  So that edge cases are handled gracefully

  Scenario: Search for a non-existent product
    Given I am on the home page
    When I search for "NonExistentProductXYZ123"
    Then I should see the no products found message

  Scenario: Search with special characters
    Given I am on the home page
    When I search for "%$#@!"
    Then I should see the no products found message

  Scenario: Unauthenticated add to cart trigger
    Given I am on a product page
    When I attempt to add the product to my cart
    Then I should receive an alert saying "Please login to add items to cart"
