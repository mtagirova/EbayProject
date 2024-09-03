Feature: Search Functionality

  Scenario: Searching for "veradigm" and verifying results
    Given I am on the search page
    When I insert "veradigm" in the search box
    And I click on the search box
    Then I should see the search link
    And I should see results related to "veradigm"


