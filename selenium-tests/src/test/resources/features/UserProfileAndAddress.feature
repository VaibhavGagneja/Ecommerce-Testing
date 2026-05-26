@UserProfile @Address
Feature: User Profile and Address Management
  As an authenticated customer
  I want to manage my personal details and delivery addresses
  So that my profile details are accurate and checkouts are fast

  Background:
    Given I register and log in a new customer account
    And I directly navigate to the "/profile" URL

  Scenario: Update User Personal Info
    When I click the profile tab "Personal info"
    And I update my personal details name to "Jane Smith" and mobile number to "9876500000"
    Then I should see the toast notification "Profile updated"

  Scenario: Address Management CRUD
    When I click the profile tab "Addresses"
    And I add a new address with label "Office", name "Jane Smith", phone "9876543210", pincode "110001", line1 "Connaught Place B-12", line2 "Near Metro", city "New Delhi", state "Delhi"
    Then I should see the address "Office" in the saved list
    When I edit the address "Office" with a new pincode "110002"
    Then I should see the address "Office" with pincode "110002" in the saved list
    When I delete the address "Office"
    Then I should not see the address "Office" in the saved list
