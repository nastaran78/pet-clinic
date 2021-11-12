package org.springframework.samples.petclinic.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.petclinic.model.priceCalculators.CustomerDependentPriceCalculator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CustomerDependentPriceCalculatorTest {

	@Mock
	PetType cat;
	double baseCharge = 10;
	double basePricePerPet = 100;
	@InjectMocks
	CustomerDependentPriceCalculator customerDependentPriceCalculator;

	@Before
	public void setUp() {
		this.customerDependentPriceCalculator = new CustomerDependentPriceCalculator();
		MockitoAnnotations.initMocks(this);
	}

	private Pet createOnePet(int year) {
		Pet pet1 = new Pet();
		pet1.setId(1);
		pet1.setType(cat);
		pet1.setBirthDate(java.sql.Date.valueOf(LocalDate.of(LocalDate.now().getYear() - year, 1, 1)));
		return pet1;
	}


	@Test
	public void testHaveFiveRareInfantPetAndNewUser() {
		List<Pet> ratePets = Arrays.asList(createOnePet(1), createOnePet(1), createOnePet(1), createOnePet(1), createOnePet(1), createOnePet(1), createOnePet(1));
		UserType userType = UserType.NEW;
		Mockito.when(cat.getRare()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
		double price = customerDependentPriceCalculator.calcPrice(ratePets, baseCharge, basePricePerPet, userType);
		Assert.assertEquals(944.8, price, 0.0);
	}

	@Test
	public void testHaveTwoRarePetsAndNewUser() {
		Pet rateOldPet = createOnePet(3);
		Pet rateInfantPet = createOnePet(1);
		List<Pet> ratePets = Arrays.asList(rateInfantPet, rateOldPet);
		UserType userType = UserType.NEW;
		Mockito.when(cat.getRare()).thenReturn(true);
		double price = customerDependentPriceCalculator.calcPrice(ratePets, baseCharge, basePricePerPet, userType);
		Assert.assertEquals(288.0, price, 0.0);
	}

	@Test
	public void testHaveOneCommonOldPetWithGoldUser() {
		List<Pet> pets = Collections.singletonList(createOnePet(3));
		UserType userType = UserType.GOLD;
		Mockito.when(cat.getRare()).thenReturn(false);
		double price = customerDependentPriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, userType);
		Assert.assertEquals(90.0, price, 0.0);
	}

	@Test
	public void testHaveFiveRareInfantPetAndSilverUser() {
		List<Pet> ratePets = Arrays.asList(createOnePet(1), createOnePet(1), createOnePet(1), createOnePet(1), createOnePet(1));
		UserType userType = UserType.SILVER;
		Mockito.when(cat.getRare()).thenReturn(true);
		double price = customerDependentPriceCalculator.calcPrice(ratePets, baseCharge, basePricePerPet, userType);
		Assert.assertEquals(765.0, price, 0.0);
	}


}
