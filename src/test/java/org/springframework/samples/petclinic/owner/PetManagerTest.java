package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PetManagerTest {


	@Mock
	PetTimedCache pets;
	@Mock
	OwnerRepository owners;
	@Mock
	Logger log;

	@InjectMocks
	private PetManager petManager;

	private Owner gorge;

	private Pet cat;

	@BeforeEach
	void setup() {
		gorge = new Owner();
		gorge.setId(1);
		gorge.setFirstName("Gorge");
		gorge.setLastName("Marlin");
		cat = new Pet();
		PetType cateType = new PetType();
		cateType.setName("catType");
		cat.setType(cateType);

		MockitoAnnotations.initMocks(this);
	}

	// Behavior
	@Test
	public void testFindOwnerValidValueBehavior() {
		int ownerId = 1;
		petManager.findOwner(ownerId);
		verify(log, Mockito.times(1)).info("find owner {}", ownerId);
		ArgumentCaptor<Integer> ownerIdCaptor = ArgumentCaptor.forClass(Integer.class);
		verify(owners, Mockito.times(1)).findById(ownerIdCaptor.capture());
		assertEquals(1, ownerIdCaptor.getValue());
	}

	//state - Mock - Mockito
	@Test
	public void testFindOwnerInvalidValueState() {
		int ownerId = 1;
		Owner realOwner = petManager.findOwner(ownerId);
		assertNull(realOwner);
	}

	//state - Classical
	@Test
	public void testFindOwnerValidValueState() {
		int ownerId = 1;
		when(this.owners.findById(anyInt())).thenReturn(this.gorge);
		Owner realOwner = petManager.findOwner(ownerId);
		assertEquals( realOwner.toString(), this.gorge.toString());
	}

	// Behavior - Mockito
	@Test
	public void testNewPetValidValue() {
		Owner owner = mock(Owner.class);
		petManager.newPet(owner);
		verify(log, Mockito.times(1)).info("add pet for owner {}", owner.getId());
		ArgumentCaptor<Pet> pet = ArgumentCaptor.forClass(Pet.class);
		verify(owner, Mockito.times(1)).addPet(pet.capture());
		assertTrue(pet.getValue().isNew());
	}

	// State - Mockito
	@Test
	public void testNewPetValidValueState() {
		Owner owner = spy(Owner.class);
		petManager.newPet(owner);
		assertEquals(1, owner.getPets().size());
	}

	// Behavior - Mockito
	@Test
	public void testFindPetsValidValue() {
		int petId = 1;
		petManager.findPet(petId);
		verify(log, Mockito.times(1)).info("find pet by id {}", petId);
		verify(pets, Mockito.times(1)).get(petId);

	}


	// State
	@Test
	public void testFindPetInValidValue() {
		int petId = 1;
		Pet pet = petManager.findPet(petId);
		assertNull(pet);
	}

	//State
	@Test
	public void testFindPetValidValue() {
		when(this.pets.get(anyInt())).thenReturn(this.cat);
		int petId = 1;
		Pet pet = petManager.findPet(petId);
		assertEquals(this.cat.getType().getName(), pet.getType().getName());
	}

	// Behavior - Classical - Mock
	@Test
	public void testSavePetValidValue() {
		Owner owner = mock(Owner.class);
		petManager.savePet(this.cat, owner);
		verify(owner, Mockito.times(1)).addPet(this.cat);
		verify(this.log, Mockito.times(1)).info("save pet {}", this.cat.getId());
		verify(this.pets, Mockito.times(1)).save(this.cat);
		assertNull(owner.getPet(this.cat.getName()));
	}

	// State - Classical - Spy
	@Test
	public void testSavePetValidValueState() {
		Owner owner = spy(Owner.class);
		petManager.savePet(this.cat, owner);
		assertEquals(this.cat.getOwner().toString(), owner.toString());
		assertNotNull(owner.getPets());
	}

















}
