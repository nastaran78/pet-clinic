Feature: Save Pet

  Background:
    Given There is some predefined pet types like "dog"

  Scenario: First Scenario Name
    Given There is a pet owner called "Nastaran" "Alipour"
    When He performs save pet service to add a pet to his list
    Then The pet is saved successfully
