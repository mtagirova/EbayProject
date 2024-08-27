Feature: Search Functionality

  As a user
  I want to be able to search for relevant information
  So that I can find what I’m looking for quickly

  Background:
    Given I am on the search page

  Scenario: Successful search for a term
    When I insert "Healthcare" in the search box
    And I click on the search box
    Then I should see results related to "Healthcare"

  Scenario: No results for an invalid search term
    When I insert "NonexistentTerm123" in the search box
    And I click on the search box
    Then I should see results related to "No results found"


