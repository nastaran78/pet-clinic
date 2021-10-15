package org.springframework.samples.petclinic.owner;

import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(Theories.class)
public class OwnerTest {

	public static Pet pet;
	public static Owner owner;
	@DataPoints
	public static String[] data = {"RABBIT", "Cat", "dog"};
	@DataPoints
	public static Set[] datasets = {new HashSet(Arrays.asList("cat", "dog")),
		new HashSet(Arrays.asList("cat", "rabbit", "dog")),
		new HashSet(Arrays.asList("fish", "rabbit", "dog", "bird"))};

	@BeforeClass
	public static void setUp() {
		owner = new Owner();
	}

	@Theory
	public void petNameExists(Set<String> dataset, String name) {

		assumeTrue(dataset != null);
		assumeTrue(dataset.contains(name) || dataset.contains(name.toLowerCase()));
		List<String> datalist = new ArrayList<>(dataset);
		List<Pet> pets = new ArrayList<>();

		for (String s : datalist) {
			Pet pet = new Pet();
			pet.setName(s);
			pets.add(pet);
		}
		Set<Pet> petsSet = new HashSet<>(pets);
		owner.setPetsInternal(petsSet);
		pet = owner.getPet(name, false);

		assertNotNull(pet);
	}

	@Theory
	public void petNameNotExists(Set<String> dataset, String name) {

		assumeTrue(dataset != null);
		assumeTrue(!dataset.contains(name) && !dataset.contains(name.toLowerCase()));
		List<String> datalist = new ArrayList<>(dataset);
		List<Pet> pets = new ArrayList<>();

		for (String s : datalist) {
			Pet pet = new Pet();
			pet.setName(s);
			pets.add(pet);
		}
		Set<Pet> petsSet = new HashSet<>(pets);
		owner.setPetsInternal(petsSet);
		pet = owner.getPet(name, false);

		assertNull(pet);
	}

}
