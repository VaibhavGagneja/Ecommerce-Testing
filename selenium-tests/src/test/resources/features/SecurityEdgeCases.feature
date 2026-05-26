@Coverage @Security
Feature: Security Edge Cases
  As a security auditor
  I want to verify that routes and inputs are secure
  So that unauthorized access and XSS are prevented

  Scenario: Guest attempting to access admin route
    Given I navigate to the home page as a guest
    When I directly navigate to the "/admin" URL
    Then I should be redirected away from the admin page

  Scenario: Customer attempting to access admin route
    Given I log in as a standard customer
    When I directly navigate to the "/admin" URL
    Then I should be redirected away from the admin page

  Scenario: XSS injection attempt in search bar
    Given I am on the home page
    When I inject an XSS payload into the search bar
    Then The payload should not execute and should be rendered as safe text
