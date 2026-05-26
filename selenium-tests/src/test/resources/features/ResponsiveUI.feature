@Responsive @UI
Feature: Responsive UI Viewports
  As a customer accessing EcoMart from different devices
  I want the interface elements to adjust to my screen size
  So that I have a consistent browsing experience

  Scenario: Mobile viewport renders bottom navigation bar and hides desktop wishlist link
    Given I navigate to the home page as a guest
    When I resize the browser window to mobile width "375" and height "812"
    Then The mobile bottom navigation bar should be visible
    And The desktop wishlist link should not be visible

  Scenario: Desktop viewport hides mobile navigation bar and renders desktop wishlist link
    Given I navigate to the home page as a guest
    When I resize the browser window to desktop width "1920" and height "1080"
    Then The mobile bottom navigation bar should not be visible
    And The desktop wishlist link should be visible
