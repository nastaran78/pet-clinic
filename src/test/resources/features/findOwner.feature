@sample_annotation
Feature: Find Owner

  Scenario: First Scenario Name
    Given There is a pet owner with id 1
    When We perform find owner pet service to get owner as return value
    Then The owner is returned successfully
