@Coverage @Registration
Feature: Registration Edge Cases and Validations
  As a new customer
  I want to register an account
  So that I can use the application properly and securely

  Scenario: Registration with a short phone number
    Given I navigate to the login page
    When I submit registration details with a 9-digit phone number
    Then I should see a registration error message "Enter a valid 10-digit phone number"

  Scenario: Registration with a short password
    Given I navigate to the login page
    When I submit registration details with a 5-character password
    Then I should see a registration error message "Password must be at least 6 characters"

  Scenario: Registration with mismatched passwords
    Given I navigate to the login page
    When I submit registration details with mismatched passwords
    Then I should see a registration error message "Passwords do not match"
