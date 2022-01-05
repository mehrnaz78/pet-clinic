@PetService
Feature: PetService

  Background: Pet Service is working correctly
    Given Pet service is ready

  Scenario: Find existing owner
    Given There is an owner with id = 1.
    When Owner is searched by id = 1.
    Then The owner with id = 1 found successfully.


  Scenario: Find non existing owner
    Given There is an owner with id = 1.
    When Owner is searched by id = 3.
    Then The owner with id = 3 not found.


  Scenario: Add new pet to owner
    Given There is an owner.
    When New pet is added to owner.
    Then Owner has that new pet.


  Scenario: Find existing pet
    Given There is a pet with id = 1.
    When Pet is searched by id = 1.
    Then The pet with id = 1 found successfully.


  Scenario: Find non existing pet
    Given There is a pet with id = 1.
    When Pet is searched by id = 3.
    Then The pet with id = 3 not found.
    

  Scenario: Save pet
    Given There is an owner and a pet.
    When Pet is added to owner.
    Then Pet is saved.
