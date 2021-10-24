package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
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
		assertEquals(realOwner.toString(), this.gorge.toString());
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

	// Behavior
	@Test
	public void testGetOwnerPetsException() {
		int ownerId = 1;
		try {
			petManager.getOwnerPets(ownerId);
			fail("should throw a NullPointerException");
		} catch (NullPointerException ignored) {
		}
		verify(log, Mockito.times(1)).info("finding the owner's pets by id {}", ownerId);
		// ArgumentCaptor<Owner> owner = ArgumentCaptor.forClass(Owner.class);
	}

	// State
	@Test
	public void testGetOwnerPetsValidValue() {
		int ownerId = 1;
		this.gorge.addPet(this.cat);
		when(this.owners.findById(anyInt())).thenReturn(this.gorge);
		List<Pet> pets = petManager.getOwnerPets(ownerId);
		assertEquals(pets.size(), 1);
		assertEquals(pets.get(0).toString(), this.cat.toString());
	}

	@Test
	public void testGetOwnerPetTypesValidValue() {
		Owner owner = spy(Owner.class);
		int ownerId = 1;
		Pet pet1 = new Pet();
		Pet pet2 = new Pet();
		Pet pet3 = new Pet();
		PetType petType1 = new PetType();
		petType1.setName("petType1");
		PetType petType2 = new PetType();
		petType2.setName("petType2");
		pet1.setType(petType1);
		pet2.setType(petType2);
		pet3.setType(petType2);

		when(this.owners.findById(anyInt())).thenReturn(owner);
		when(owner.getPets()).thenReturn(new ArrayList<>(Arrays.asList(pet1, pet2, pet3)));
		Set<PetType> pets = petManager.getOwnerPetTypes(ownerId);
		assertEquals(2, pets.size());
		assertNotNull(owner.getPets());
		assertTrue(pets.contains(pet1.getType()));
	}

	// Behavior
	@Test
	public void testGetOwnerPetTypesValidValueBehavior() {
		Owner owner = mock(Owner.class);
		int ownerId = 1;
		when(this.owners.findById(anyInt())).thenReturn(owner);
		when(owner.getPets()).thenReturn(new ArrayList<>());
		Set<PetType> petTypes = petManager.getOwnerPetTypes(ownerId);
		verify(log, Mockito.times(1)).info("finding the owner's petTypes by id {}", ownerId);
		verify(owner, Mockito.times(1)).getPets();
		verify(this.owners).findById(ownerId);
		assertEquals(0, petTypes.size());

	}

	@Test
	public void testGetVisitsBetween() {
		int petId = 1;
		LocalDate startDate = LocalDate.parse("2020-01-08");
		LocalDate endDate = LocalDate.parse("2021-09-13");
		when(pets.get(anyInt())).thenReturn(this.cat);
		petManager.getVisitsBetween(petId, startDate, endDate);
		verify(log, Mockito.times(1)).info("get visits for pet {} from {} since {}", petId, startDate, endDate);
		verify(this.pets).get(petId);
	}

	@Test
	public void testGetVisitsBetweenValidValue() {
		int petId = 1;
		Visit visit = new Visit();
		visit.setDate(LocalDate.parse("2021-01-02"));
		this.cat.addVisit(visit);
		LocalDate startDate = LocalDate.parse("2020-01-01");
		LocalDate endDate = LocalDate.parse("2022-01-13");
		when(pets.get(anyInt())).thenReturn(this.cat);
		List<Visit> petVisits = petManager.getVisitsBetween(petId, startDate, endDate);
		assertEquals(1, petVisits.size());
	}


}
