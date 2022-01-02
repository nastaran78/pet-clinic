Feature: add new pet

  Scenario: add new pet
    Given There is a pet owner called "Nastaran" "Alipour"
    When she performs new pet service to get a pet to his own
    Then The pet is saved for owner
