@Admin @ProductCRUD
Feature: Admin Product CRUD
  As an administrator
  I want to create, read, update, and delete products
  So that the store catalog is kept up to date

  Background:
    Given I navigate to the login page
    And I submit username "adarsht072@gmail.com" and password "Adarsh@123"
    And I navigate to the Admin Dashboard

  Scenario: Admin successfully creates and uploads a new product
    When I click the Add Product button
    And I fill the basic product details: name "Belkin Charger", brand "Belkin", price "2499", stock "50", category "Electronics", description "Belkin charger adapter"
    And I upload the product image
    And I navigate to the Manufacturer tab
    And I submit the product form
    Then I should see the alert "Product added successfully!"
    And I should see the product "Belkin Charger" listed in the Admin products table

  Scenario: Admin successfully deletes a product
    When I locate the product "Belkin Charger" in the Admin products table
    And I click the delete button for "Belkin Charger" and accept the confirmation alert
    Then I should not see "Belkin Charger" in the Admin products table
