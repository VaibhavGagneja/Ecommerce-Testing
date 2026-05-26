@Product @Search @Sorting
Feature: Product Details, Search and Sorting
  As an EcoMart user
  I want to search products, sort results, and check product details
  So that I can find the best deals easily

  Scenario: Validate discount calculations on product details page
    Given I am on the home page
    When I search for "iPhone 15 Pro Max (256 GB, Natural Titanium)"
    And I open the product page
    Then The product details should display a price of "139900"
    And The discount should show "13% off"

  Scenario: Search results sorting by price
    Given I am on the home page
    When I search for "Mouse"
    And I sort the search results by "Price: Low to High"
    Then The search results should be sorted by price in ascending order
    When I sort the search results by "Price: High to Low"
    Then The search results should be sorted by price in descending order

  Scenario: Apply price range filters
    Given I am on the home page
    When I search for "Cream"
    And I apply the price filter "Under Rs 1,000"
    Then All search results should have prices under "1000"
