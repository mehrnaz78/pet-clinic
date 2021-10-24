package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class OwnerTest {

	// state verification
	@Test
	public void Pet_is_added_correctly() {
		Owner owner = new Owner();
		Pet pet = mock(Pet.class);
		given(pet.isNew()).willReturn(true);
		owner.addPet(pet);
		assertEquals(owner.getPets().size(), 1);
	}

	// behavior verification
	@Test
	public void Pet_is_not_added_but_set_owner() {
		Owner owner = new Owner();
		Pet pet = mock(Pet.class);
		given(pet.isNew()).willReturn(false);
		owner.addPet(pet);
		verify(pet, times(1)).setOwner(owner);
	}
}
