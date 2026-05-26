@Regression
Feature: User Authentication
  As a customer or administrator of EcoMart
  I want to be able to log into the application
  So that I can access personalized features

@Smoke
Scenario: Successful Login with Admin Credentials
  Given I navigate to the login page
  When I submit username and password from configuration
  Then I should be successfully logged in as user "Admin"

@Negative
Scenario: Failed Login with Invalid Credentials
  Given I navigate to the login page
  When I submit username "invalid@user.com" and password "wrongpass"
  Then I should see an error message "Email not registered"

@Negative
Scenario: Failed Login with Invalid Credentials
  Given I navigate to the login page
  When I submit username "" and password ""
  Then I should see an error message "Email not registered"

@Smoke
Scenario: User Logout and Session Termination
  Given I navigate to the login page
  When I submit username and password from configuration
  Then I should be successfully logged in as user "Admin"
  When I click on the logout button
  Then I should be logged out and redirected to the login page
