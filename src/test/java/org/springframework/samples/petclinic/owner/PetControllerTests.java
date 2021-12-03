package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
class PetControllerTests {
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_PET_ID = 1;
	private final Pet max = new Pet();
	@MockBean
	private PetRepository pets;
	@MockBean
	private OwnerRepository owners;
	@MockBean
	private PetService petService;
	@Autowired
	private MockMvc mockMvc;

	private Owner george;

	@BeforeEach
	public void setUp() {
		george = new Owner();
		george.setId(TEST_OWNER_ID);
		george.setFirstName("George");
		george.setLastName("Franklin");
		george.setAddress("110 W. Liberty St.");
		george.setCity("Madison");
		george.setTelephone("6085551023");

		PetType dog = new PetType();
		dog.setName("dog");
		max.setId(1);
		max.setType(dog);
		max.setName("Max");
		max.setBirthDate(LocalDate.now());
		Set<Pet> petsInternal = new HashSet<Pet>();
		petsInternal.add(max);
		george.setPetsInternal(petsInternal);
		owners.save(george);
		given(this.petService.findOwner(anyInt())).willReturn(george);
	}


	@Test
	public void testInitCreationFormSuccess() throws Exception {
		given(petService.newPet(any())).willReturn(new Pet());
		mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("pet"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}


	@Test
	public void testProcessCreationFormSuccess() throws Exception {
		Pet newPet = _createValidPet();
		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
			.flashAttr("pet", newPet))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}


	@Test
	public void testProcessCreationFormHasError() throws Exception {
		Pet newPet = _createPetExistsAlready();
		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
			.flashAttr("pet", newPet))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("pet"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	private Pet _createPetExistsAlready() {
		Pet newPet = new Pet();
		newPet.setName(george.getPetsInternal().iterator().next().getName());
		return newPet;
	}

	@Test
	public void testInitUpdateFormSuccess() throws Exception {
		given(petService.findPet(anyInt())).willReturn(max);
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("pet"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}


	@Test
	public void testProcessUpdateFormSuccess() throws Exception {
		Pet newPet = _createValidPet();
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
			.flashAttr("pet", newPet))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	private Pet _createValidPet() {
		Pet newPet = new Pet();
		newPet.setName("newPet");
		PetType petType = new PetType();
		petType.setName("cat");
		newPet.setName("newPet");
		newPet.setBirthDate(LocalDate.now());
		newPet.setType(petType);
		return newPet;
	}


	@Test
	public void testProcessUpdateFormHasError() throws Exception {
		Pet newPet = _createPetNotCompletedInfo();
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
			.flashAttr("pet", newPet))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("pet"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	private Pet _createPetNotCompletedInfo() {
		Pet newPet = new Pet();
		newPet.setName("newPet");
		PetType petType = new PetType();
		petType.setName("cat");
		return newPet;
	}


}
