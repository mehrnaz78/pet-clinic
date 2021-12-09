package org.springframework.samples.petclinic.owner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTests {

	private static String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	Owner owner;
	Pet pet;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private PetRepository pets;

	@Mock
	private PetService petService;

	@Mock
	private OwnerRepository owners;


	@BeforeEach
	void setup(){
		owner = mock(Owner.class);
		given(petService.findOwner(1)).willReturn(owner);

		pet = new Pet();
		pet.setName("caty");

	}

	@Test
	void initCreationFormTest() throws Exception {
		given(petService.newPet(owner)).willReturn(pet);

		MvcResult result = mockMvc.perform(get("/owners/1/pets/new"))
			.andExpect(status().isOk())
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM))
			.andReturn();
	}

	@Test
	void processCreationFormTest() throws Exception {
		given(owner.getPet("caty", true)).willReturn(null);

		MvcResult result = mockMvc.perform(post("/owners/1/pets/new")
			.param("name", "caty")
			.param("birthDate", "2021-06-21")
			.param("type", "cat"))
			.andExpect(view().name("redirect:/owners/{ownerId}"))
			.andExpect(status().is3xxRedirection())
			.andReturn();
	}

	@Test
	void processCreationFormTestWhenErrorHappens() throws Exception {
		given(owner.getPet("caty", true)).willReturn(pet);


		MvcResult result = mockMvc.perform(post("/owners/1/pets/new")
			.param("name", "caty")
			.param("type", "cat"))
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM))
			.andExpect(status().isOk())
			.andReturn();
	}

	@Test
	void initUpdateFormTest() throws Exception {
		given(petService.findPet(2)).willReturn(pet);

		mockMvc.perform(get("/owners/1/pets/2/edit"))
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM))
			.andExpect(status().isOk());
	}

	@Test
	void processUpdateFormTest() throws Exception {
		given(petService.findPet(2)).willReturn(pet);

		MvcResult result = mockMvc.perform(post("/owners/1/pets/2/edit")
			.param("name", "caty")
			.param("birthDate", "2021-06-21")
			.param("type", "cat"))
			.andExpect(view().name("redirect:/owners/{ownerId}"))
			.andExpect(status().is3xxRedirection())
			.andReturn();

	}

	@Test
	void processUpdateFormTestWhenErrorHappens() throws Exception {

		given(petService.findPet(2)).willReturn(pet);

		MvcResult result = mockMvc.perform(post("/owners/1/pets/2/edit")
			.param("birthDate", "2021-06-21")
			.param("type", "cat"))
			.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM))
			.andExpect(status().isOk())
			.andReturn();

	}

}
