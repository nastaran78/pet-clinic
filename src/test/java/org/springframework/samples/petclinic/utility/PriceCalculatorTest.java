package org.springframework.samples.petclinic.utility;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.samples.petclinic.owner.Pet;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import org.springframework.samples.petclinic.visit.Visit;
import java.util.*;


public class PriceCalculatorTest {

	List<Pet> pets ;

	public double baseCharge;
	public double basePricePerPet;

	public PriceCalculator priceCalculator;

	@BeforeEach
	void setUp(){
		baseCharge = 0;
		basePricePerPet = 1;

		priceCalculator = new PriceCalculator();
		pets = new ArrayList<>();
	}


	/*
	visits.size() = 0
	(age=0) <= (INFANT_YEARS=2)
	(discountCounter=2) < (DISCOUNT_MIN_SCOR=10)
	*/
	@Test
	void petVisitsIsEmpty1(){
		Pet pet = new Pet();
		LocalDate date = LocalDate.parse("2021-11-08");
		pet.setBirthDate(date);
		pets.add(pet);
		double result = priceCalculator.calcPrice(pets , baseCharge , basePricePerPet);
		assertTrue(result == 1.68);
	}

	/*
	visits.size() = 0
	(age=2) = (INFANT_YEARS=2)
	(discountCounter=2) < (DISCOUNT_MIN_SCOR=10)
	*/
	@Test
	void petVisitsIsEmpty2(){
		Pet pet = new Pet();
		LocalDate date = LocalDate.parse("2019-11-08");
		pet.setBirthDate(date);
		pets.add(pet);
		double result = priceCalculator.calcPrice(pets , baseCharge , basePricePerPet);
		assertTrue(result == 1.68);
	}

	/*
	visits.size() = 0
	(age=10) > (INFANT_YEARS=2)
	(discountCounter=1) < (DISCOUNT_MIN_SCOR=10)
	*/
	@Test
	void petVisitsIsEmpty3(){
		Pet pet = new Pet();
		LocalDate date = LocalDate.parse("2011-11-08");
		pet.setBirthDate(date);
		pets.add(pet);
		double result = priceCalculator.calcPrice(pets , baseCharge , basePricePerPet);
		assertTrue(result == 1.2);
	}

	/*
	visits.size() = 0
	(age=0) <= (INFANT_YEARS=2)
	(discountCounter=10) >= (DISCOUNT_MIN_SCOR=10)
	(daysFromLastVisit) < 100
	*/
	@Test
	void petVisitsIsEmpty4(){
		Pet pet1 = new Pet();
		Pet pet2 = new Pet();
		Pet pet3 = new Pet();
		Pet pet4 = new Pet();
		Pet pet5 = new Pet();

		LocalDate date = LocalDate.parse("2021-11-08");

		pet1.setBirthDate(date);
		pets.add(pet1);
		pet2.setBirthDate(date);
		pets.add(pet2);
		pet3.setBirthDate(date);
		pets.add(pet3);
		pet4.setBirthDate(date);
		pets.add(pet4);
		pet5.setBirthDate(date);
		pets.add(pet5);

		double result = PriceCalculator.calcPrice(pets , baseCharge , basePricePerPet);
		assertTrue(result == 15.12);
	}

	/*
	visits.size() = 0
	(age=0) <= (INFANT_YEARS=2)
	(discountCounter=12) >= (DISCOUNT_MIN_SCOR=10)
	(daysFromLastVisit) < 100
	*/
	@Test
	void petVisitsIsEmpty5(){
		Pet pet1 = new Pet();
		Pet pet2 = new Pet();
		Pet pet3 = new Pet();
		Pet pet4 = new Pet();
		Pet pet5 = new Pet();
		Pet pet6 = new Pet();

		LocalDate date = LocalDate.parse("2021-11-08");

		pet1.setBirthDate(date);
		pets.add(pet1);
		pet2.setBirthDate(date);
		pets.add(pet2);
		pet3.setBirthDate(date);
		pets.add(pet3);
		pet4.setBirthDate(date);
		pets.add(pet4);
		pet5.setBirthDate(date);
		pets.add(pet5);
		pet6.setBirthDate(date);
		pets.add(pet6);

		double result = PriceCalculator.calcPrice(pets , baseCharge , basePricePerPet);
		assertTrue(result == 31.919999999999998);
	}

	/*
	visits.size() != 0
	(age=1) <= (INFANT_YEARS=2)
	(discountCounter=10) >= (DISCOUNT_MIN_SCOR=10)
	(daysFromLastVisit) > 100
	*/
	@Test
	void petVisitsIsNotEmpty1(){
		Pet pet1 = new Pet();
		Pet pet2 = new Pet();
		Pet pet3 = new Pet();
		Pet pet4 = new Pet();
		Pet pet5 = new Pet();

		LocalDate date = LocalDate.parse("2020-01-08");

		Visit visit = new Visit();
		visit.setDate(LocalDate.parse("2020-03-11"));

		pet1.addVisit(visit);
		pet2.addVisit(visit);
		pet3.addVisit(visit);
		pet4.addVisit(visit);
		pet5.addVisit(visit);

		pet1.setBirthDate(date);
		pets.add(pet1);
		pet2.setBirthDate(date);
		pets.add(pet2);
		pet3.setBirthDate(date);
		pets.add(pet3);
		pet4.setBirthDate(date);
		pets.add(pet4);
		pet5.setBirthDate(date);
		pets.add(pet5);

		double result = PriceCalculator.calcPrice(pets , baseCharge , basePricePerPet);
		assertTrue(result == 48.72);
	}

	/*
	visits.size() != 0
	(age=1) <= (INFANT_YEARS=2)
	(discountCounter=10) >= (DISCOUNT_MIN_SCOR=10)
	(daysFromLastVisit) < 100
	*/
	@Test
	void petVisitsIsNotEmpty2(){
		Pet pet1 = new Pet();
		Pet pet2 = new Pet();
		Pet pet3 = new Pet();
		Pet pet4 = new Pet();
		Pet pet5 = new Pet();

		LocalDate date = LocalDate.parse("2020-01-08");

		Visit visit = new Visit();
		visit.setDate(LocalDate.parse("2021-11-11"));

		pet1.addVisit(visit);
		pet2.addVisit(visit);
		pet3.addVisit(visit);
		pet4.addVisit(visit);
		pet5.addVisit(visit);

		pet1.setBirthDate(date);
		pets.add(pet1);
		pet2.setBirthDate(date);
		pets.add(pet2);
		pet3.setBirthDate(date);
		pets.add(pet3);
		pet4.setBirthDate(date);
		pets.add(pet4);
		pet5.setBirthDate(date);
		pets.add(pet5);

		double result = PriceCalculator.calcPrice(pets , baseCharge , basePricePerPet);
		assertEquals(15.12, result);
	}

	/*
	visits.size() = 0
	(age=0) <= (INFANT_YEARS=2)
	(discountCounter=2) < (DISCOUNT_MIN_SCOR=10)
	*/
	@Test
	void resultBaseChargeAndBasePricePerPet1(){
		baseCharge = 1;
		basePricePerPet = 1;
		Pet pet = new Pet();
		LocalDate date = LocalDate.parse("2021-11-08");
		pet.setBirthDate(date);
		pets.add(pet);
		double result = PriceCalculator.calcPrice(pets , baseCharge , basePricePerPet);
		assertEquals(1.68, result);
	}

	/*
	visits.size() != 0
	(age=1) <= (INFANT_YEARS=2)
	(discountCounter=10) >= (DISCOUNT_MIN_SCOR=10)
	(daysFromLastVisit) > 100
	*/
	@Test
	void resultBaseChargeAndBasePricePerPet2(){
		baseCharge = 1;
		basePricePerPet = 1;
		Pet pet1 = new Pet();
		Pet pet2 = new Pet();
		Pet pet3 = new Pet();
		Pet pet4 = new Pet();
		Pet pet5 = new Pet();

		LocalDate date = LocalDate.parse("2020-01-08");

		Visit visit = new Visit();
		visit.setDate(LocalDate.parse("2020-03-11"));

		pet1.addVisit(visit);
		pet2.addVisit(visit);
		pet3.addVisit(visit);
		pet4.addVisit(visit);
		pet5.addVisit(visit);

		pet1.setBirthDate(date);
		pets.add(pet1);
		pet2.setBirthDate(date);
		pets.add(pet2);
		pet3.setBirthDate(date);
		pets.add(pet3);
		pet4.setBirthDate(date);
		pets.add(pet4);
		pet5.setBirthDate(date);
		pets.add(pet5);

		double result = PriceCalculator.calcPrice(pets , baseCharge , basePricePerPet);
		assertEquals(55.72, result);
	}

	/*
	visits.size() != 0
	(age=1) <= (INFANT_YEARS=2)
	(discountCounter=10) >= (DISCOUNT_MIN_SCOR=10)
	(daysFromLastVisit) > 100
	*/
	@Test
	void resultBaseChargeAndBasePricePerPet3(){
		baseCharge = 1;
		basePricePerPet = 10;
		Pet pet1 = new Pet();
		Pet pet2 = new Pet();
		Pet pet3 = new Pet();
		Pet pet4 = new Pet();
		Pet pet5 = new Pet();

		LocalDate date = LocalDate.parse("2020-01-08");

		Visit visit = new Visit();
		visit.setDate(LocalDate.parse("2020-03-11"));

		pet1.addVisit(visit);
		pet2.addVisit(visit);
		pet3.addVisit(visit);
		pet4.addVisit(visit);
		pet5.addVisit(visit);

		pet1.setBirthDate(date);
		pets.add(pet1);
		pet2.setBirthDate(date);
		pets.add(pet2);
		pet3.setBirthDate(date);
		pets.add(pet3);
		pet4.setBirthDate(date);
		pets.add(pet4);
		pet5.setBirthDate(date);
		pets.add(pet5);

		double result = PriceCalculator.calcPrice(pets , baseCharge , basePricePerPet);
		assertEquals(494.19999999999993, result);
	}


	/*
	visits.size() != 0
	(age=1) <= (INFANT_YEARS=2)
	(discountCounter=10) >= (DISCOUNT_MIN_SCOR=10)
	(daysFromLastVisit) = 100
	*/
	@Test
	void resultBaseChargeAndBasePricePerPet4(){
		baseCharge = 1;
		basePricePerPet = 1;
		Pet pet1 = Mockito.mock(Pet.class);
		Pet pet2 = Mockito.mock(Pet.class);
		Pet pet3 = Mockito.mock(Pet.class);
		Pet pet4 = Mockito.mock(Pet.class);
		Pet pet5 = Mockito.mock(Pet.class);

		LocalDate date = LocalDate.parse("2020-01-08");

		Visit visit = new Visit();
		visit.setDate(LocalDate.now().minusDays(100));
		List<Visit> visits = new ArrayList<>();
		visits.add(visit);
		Mockito.when(pet1.getVisits()).thenReturn(visits);
		Mockito.when(pet2.getVisits()).thenReturn(visits);
		Mockito.when(pet3.getVisits()).thenReturn(visits);
		Mockito.when(pet4.getVisits()).thenReturn(visits);
		Mockito.when(pet5.getVisits()).thenReturn(visits);

		Mockito.when(pet1.getBirthDate()).thenReturn(date);
		Mockito.when(pet2.getBirthDate()).thenReturn(date);
		Mockito.when(pet3.getBirthDate()).thenReturn(date);
		Mockito.when(pet4.getBirthDate()).thenReturn(date);
		Mockito.when(pet5.getBirthDate()).thenReturn(date);

		Mockito.when(pet1.getVisitsUntilAge(anyInt())).thenReturn(visits);
		Mockito.when(pet2.getVisitsUntilAge(anyInt())).thenReturn(visits);
		Mockito.when(pet3.getVisitsUntilAge(anyInt())).thenReturn(visits);
		Mockito.when(pet4.getVisitsUntilAge(anyInt())).thenReturn(visits);
		Mockito.when(pet5.getVisitsUntilAge(anyInt())).thenReturn(visits);
		pets.add(pet1);
		pets.add(pet2);
		pets.add(pet3);
		pets.add(pet4);
		pets.add(pet5);

		double result = PriceCalculator.calcPrice(pets , baseCharge , basePricePerPet);
		assertEquals(17.12, result);
	}

}
