package org.springframework.samples.petclinic.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.petclinic.model.priceCalculators.SimplePriceCalculator;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimplePriceCalculatorTest {
	private SimplePriceCalculator simplePriceCalculator;
	@Mock PetType cat;

	@Before
	public void setUp() {
		this.simplePriceCalculator = new SimplePriceCalculator();
		MockitoAnnotations.initMocks(this);
	}


	private List<Pet> createTwoRarePets() {
		Pet pet1 = new Pet();
		pet1.setId(1);
		pet1.setType(cat);

		Pet pet2 = new Pet();
		pet2.setId(2);
		pet2.setType(cat);
		return Arrays.asList(pet1, pet2);
	}

	@Test
	public void testSimplePriceCalcWithRarePetsAndNewUser() {
		List<Pet> pets = createTwoRarePets();
		double baseCharge = 0;
		double basePricePerPet = 1;
		UserType userType = UserType.NEW;
		when(cat.getRare()).thenReturn(true);
		double price = simplePriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, userType);
		assertThat(price).isEqualTo(2.28);
	}

	@Test
	public void testSimplePriceCalcWithRarePetsAndGoldUser() {
		List<Pet> pets = createTwoRarePets();
		double baseCharge = 0;
		double basePricePerPet = 1;
		UserType userType = UserType.GOLD;
		when(cat.getRare()).thenReturn(true);
		double price = simplePriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, userType);
		assertThat(price).isEqualTo(2.4);
	}

	@Test
	public void testSimplePriceCalcWithNotRarePetsAndNewUser() {
		List<Pet> pets = createTwoRarePets();
		when(cat.getRare()).thenReturn(false);
		double baseCharge = 0;
		double basePricePerPet = 1;
		UserType userType = UserType.NEW;
		double price = simplePriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, userType);
		assertThat(price).isEqualTo(1.9);
	}


}
