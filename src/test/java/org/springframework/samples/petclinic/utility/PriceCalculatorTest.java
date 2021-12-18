package org.springframework.samples.petclinic.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PriceCalculatorTest {

	private static int INFANT_YEARS = 2;
	private static double RARE_INFANCY_COEF = 1.4;
	private static double BASE_RARE_COEF = 1.2;
	private static int DISCOUNT_MIN_SCORE = 10;
	private static int DISCOUNT_PRE_VISIT = 2;

	private PriceCalculator priceCalculator;
	private Pet pet;
	private PetType petType;
	private double baseCharge, basePricePerPet;
	private List<Pet> pets;

	@BeforeEach
	public void setUp() {
		priceCalculator = new PriceCalculator();
		pet = new Pet();
		petType = mock(PetType.class);
		baseCharge= 10.0;
		basePricePerPet = 2.0;
		pet.setType(petType);

		pets = new ArrayList<Pet>();
		pets.add(pet);
	}

	@Test
	public void ageLessThanInfantYear(){
		pet.setBirthDate(LocalDate.of(2020, 3,12));
		double price = basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF;
		assertEquals(price, priceCalculator.calcPrice(pets, baseCharge, basePricePerPet));
	}

	@Test
	public void ageMoreThanInfantYear(){
		pet.setBirthDate(LocalDate.of(2015, 3,12));
		double price = basePricePerPet * BASE_RARE_COEF;
		assertEquals(price, priceCalculator.calcPrice(pets, baseCharge, basePricePerPet));
	}

	@Test
	public void havingDiscount(){
		pet.setBirthDate(LocalDate.of(2020, 3,12));
		pets.add(pet);
		pets.add(pet);
		pets.add(pet);
		pets.add(pet);
		double pricePerPet = basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF;
		double price = pricePerPet * 4 * DISCOUNT_PRE_VISIT + baseCharge + pricePerPet;
		assertEquals(price, priceCalculator.calcPrice(pets, baseCharge, basePricePerPet));
	}

	@Test
	public void havingDiscountWithLastVisit(){
		Pet mockPet = mock(Pet.class);

		List<Visit> visits = new ArrayList<>();
		Visit visit = new Visit();
		visit.setDate(LocalDate.of(2021,4,12));
		visits.add(visit);

		given(mockPet.getBirthDate()).willReturn(LocalDate.of(2020, 3,12));
		given(mockPet.getVisitsUntilAge(1)).willReturn(visits);

		pet.setBirthDate(LocalDate.of(2020, 3,12));
		pets.add(pet);
		pets.add(pet);
		pets.add(pet);
		pets.add(mockPet);
		double pricePerPet = basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF;
		double price = (pricePerPet * 4 + baseCharge) * 3 + pricePerPet;
		assertEquals(price, priceCalculator.calcPrice(pets, baseCharge, basePricePerPet));
	}

}
