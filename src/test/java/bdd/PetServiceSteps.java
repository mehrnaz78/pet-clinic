package bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


public class PetServiceSteps {

	PetService petService;

	private OwnerRepository owners = mock(OwnerRepository.class);
	private PetTimedCache pets = mock(PetTimedCache.class);
	private Logger log = mock(Logger.class);

	private Owner owner;
	private Pet pet;

	@Given("Pet service is ready")
	public void setup() {
		petService = new PetService(pets, owners, log);
	}

	private Owner createOwner(int id) {
		Owner owner = new Owner();
		owner.setId(id);
		return owner;
	}

	@Given("There is an owner with id = 1.")
	public void thereAreSomeOwners() {
		given(owners.findById(1)).willReturn(createOwner(1));
		given(owners.findById(3)).willReturn(null);
	}

	@When("Owner is searched by id = {int}.")
	public void searchOwner(int id) {
		owner = petService.findOwner(id);
	}

	@Then("The owner with id = 1 found successfully.")
	public void assertFoundOwner() {
		Assertions.assertEquals(1, owner.getId());
	}

	@Then("The owner with id = 3 not found.")
	public void assertOwnerIsNull() {
		assertNull(owner);
	}

	@Given("There is an owner.")
	public void thereIsOwner() {
		owner = createOwner(1);
	}

	@When("New pet is added to owner.")
	public void addNewPetToOwner() {
		petService.newPet(owner);
	}

	@Then("Owner has that new pet.")
	public void petIsSavedInOwner() {
		assertEquals(1, owner.getPets().size());
	}

	private Pet createPet(int id) {
		Pet pet = new Pet();
		pet.setId(id);
		return pet;
	}

	@Given("There is a pet with id = 1.")
	public void thereAreSomePets() {
		given(pets.get(1)).willReturn(createPet(1));
		given(pets.get(3)).willReturn(null);
	}

	@When("Pet is searched by id = {int}.")
	public void searchPet(int id) {
		pet = petService.findPet(id);
	}

	@Then("The pet with id = 1 found successfully.")
	public void assertFoundPet() {
		Assertions.assertEquals(1, pet.getId());
	}

	@Then("The pet with id = 3 not found.")
	public void assertPetIsNull() {
		assertNull(pet);
	}

	@Given("There is an owner and a pet.")
	public void thereAreOwnerAndPet() {
		owner = createOwner(1);
		pet = new Pet();
	}

	@When("Pet is added to owner.")
	public void addPetToOwner() {
		petService.savePet(pet, owner);
	}

	@Then("Pet is saved.")
	public void petIsSaved() {
		assertEquals(1, owner.getPets().size());
		verify(pets, times(1)).save(pet);
	}

}
