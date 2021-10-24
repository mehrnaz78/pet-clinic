package org.springframework.samples.petclinic.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

// todo
@SpringBootTest
class PetTimedCacheTest {
	@MockBean
	private PetRepository repository;
	private PetTimedCache petTimedCache;

	@BeforeEach
	public void setUp() {
		petTimedCache = new PetTimedCache(repository);
	}

	// state verification
	@Test
	public void PetTimedCache_get_pet_from_repository_correctly() {
		Pet pet = new Pet();
		pet.setId(10);
		given(repository.findById(10)).willReturn(pet);
		Pet findPet = petTimedCache.get(10);
		assertEquals(findPet.getId(), 10);
	}

	// behavior verification
	@Test
	public void PetTimedCache_get_pet_from_actualMap_correctly() {
		Pet pet = new Pet();
		pet.setId(10);
		given(repository.findById(10)).willReturn(pet);
		petTimedCache.get(10);
		petTimedCache.get(10);
		verify(repository, times(1)).findById(10);
	}

}
