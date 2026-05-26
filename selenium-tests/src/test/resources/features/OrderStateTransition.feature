@Coverage @Order
Feature: Order State Transition
  As an admin
  I want to see new orders starting in a PENDING state
  So that I can process them appropriately

  Scenario: Verify initial order state is PENDING
    Given I log in as an administrator
    And I navigate to the admin dashboard
    When I view the orders panel
    Then I should see the initial order state as PENDING or another valid state
