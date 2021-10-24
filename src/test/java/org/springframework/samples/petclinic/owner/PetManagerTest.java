package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class PetManagerTest {

	@MockBean
	private OwnerRepository owners;
	@MockBean
	private PetTimedCache pets;
	@MockBean
	private Logger log;

	private PetManager petManager;

	@BeforeEach
	public void setUp() {
		//	We used Mock for all PetManager's fields. So, we control them in all test functions.
		petManager = new PetManager(pets, owners, log);
	}

	//	Mockisty, State Verification
	@Test
	public void Unreal_owner_dont_find() {
		given(owners.findById(10)).willReturn(null);
		assertNull(petManager.findOwner(10));
//		System.out.println(petManager.findOwner(10));
	}

	//	Mockisty, State Verification
	@Test
	public void Exiting_owner_find_correctly() {
		//	Dummy object
		Owner owner = new Owner();
		owner.setId(10);
		given(owners.findById(10)).willReturn(owner);
		assertEquals(petManager.findOwner(10).getId(), owner.getId());
	}

	//	Mockisty, Behavior Verification
	@Test
	public void New_pet_is_added_correctly() {
		//	Mock object
		Owner owner = mock(Owner.class);
		given(owner.getId()).willReturn(10);
		Pet pet = petManager.newPet(owner);
		verify(log, times(1)).info("add pet for owner {}", 10);
		verify(owner, times(1)).addPet(pet);
	}

	//	Mockisty, State Verification
	@Test
	public void Exiting_pet_find_correctly() {
		//	Dummy object
		Pet pet = new Pet();
		pet.setId(20);
		given(pets.get(20)).willReturn(pet);
		assertEquals(petManager.findPet(20).getId(), pet.getId());
	}

	//	Mockisty, State Verification
	@Test
	public void Unreal_pet_dont_find() {
		given(pets.get(20)).willReturn(null);
		assertNull(petManager.findPet(20));
	}

	//	Mockisty, Behavior Verification
	@Test
	public void Pet_is_saved_correctly() {
		//	Mock object
		Pet pet = mock(Pet.class);
		Owner owner = mock(Owner.class);
		given(pet.getId()).willReturn(20);
		petManager.savePet(pet, owner);
		verify(log, times(1)).info("save pet {}", 20);
		verify(owner, times(1)).addPet(pet);
		verify(pets, times(1)).save(pet);
	}

	//	Mockisty, State Verification
	@Test
	public void Pets_of_owner_is_returned_correctly() {
		// Dummy object
		Pet pet = new Pet();
		pet.setId(20);
		List ownerPets = new ArrayList();
		ownerPets.add(pet);
		//	Mock object
		Owner owner = mock(Owner.class);
		given(owners.findById(10)).willReturn(owner);
		given(owner.getPets()).willReturn(ownerPets);
		assertEquals(petManager.getOwnerPets(10), ownerPets);
	}

	//	Mockisty, State Verification
	@Test
	public void PetTypes_of_owner_is_returned_correctly() {
		// Dummy object
		Pet pet = new Pet();
		PetType petType = new PetType();
		petType.setName("Dog");
		pet.setType(petType);
		//	Mock object
		Owner owner = mock(Owner.class);
		given(owners.findById(10)).willReturn(owner);
		given(owner.getPets()).willReturn(List.of(pet));
		assertEquals(petManager.getOwnerPetTypes(10), Set.of(petType));
	}

	//	Mockisty, State Verification
	@Test
	public void Visits_between_is_returned_correctly() {
		//	Mock object
		Pet pet = mock(Pet.class);
		// Dummy object
		LocalDate s = LocalDate.of(2021, 11, 29);
		LocalDate e = LocalDate.of(2021, 12, 1);
		LocalDate date = LocalDate.of(2021, 11, 30);
		Visit visit = new Visit().setDate(date);
		given(pets.get(20)).willReturn(pet);
		given(pet.getVisitsBetween(s, e)).willReturn(visit);
		assertEquals(petManager.getVisitsBetween(20, s, e).get(0).getDate(), date);
	}
}
