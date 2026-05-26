@Coverage @Navbar
Feature: Navbar Role-Based Visibility
  As a system administrator
  I want the navbar to adapt based on user roles
  So that users only see links they have permission to access

  Scenario: Guest navbar visibility
    Given I navigate to the home page as a guest
    Then I should see the "Login" button
    And I should not see the "My Profile" button
    And I should not see the "Admin" link

  Scenario: Customer navbar visibility
    Given I log in as a standard customer
    Then I should not see the "Login" button
    And I should see my user profile menu
    And I should not see the "Admin" link

  Scenario: Admin navbar visibility
    Given I log in as an administrator
    Then I should not see the "Login" button
    And I should see the "Admin" link
