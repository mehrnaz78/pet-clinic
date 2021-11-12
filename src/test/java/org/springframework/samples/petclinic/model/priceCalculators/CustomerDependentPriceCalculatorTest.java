package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.Before;
import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class CustomerDependentPriceCalculatorTest {

	private CustomerDependentPriceCalculator customerDependentPriceCalculator;
	private Pet pet;
	private PetType petType;
	private double baseCharge, basePricePerPet;

	@Before
	public void setUp() {
		customerDependentPriceCalculator = new CustomerDependentPriceCalculator();
		pet = new Pet();
		petType = mock(PetType.class);
		baseCharge= 10.0;
		basePricePerPet = 2.0;
		pet.setType(petType);
	}

	@Test
	public void Price_of_infant_rare_pet_for_new_user_is_calculated_correctly() {
		given(petType.getRare()).willReturn(true);
		pet.setBirthDate(new GregorianCalendar(2020, Calendar.FEBRUARY, 11).getTime());
		UserType userType = UserType.NEW;
		double result = 1.4*(basePricePerPet*1.2);
		assertThat(customerDependentPriceCalculator.calcPrice(List.of(pet), baseCharge, basePricePerPet, userType)).isEqualTo(result);
	}

	@Test
	public void Price_of_rare_pet_for_gold_user_is_calculated_correctly() {
		given(petType.getRare()).willReturn(true);
		pet.setBirthDate(new GregorianCalendar(2010, Calendar.FEBRUARY, 11).getTime());
		UserType userType = UserType.GOLD;
		double result = (basePricePerPet*1.2)*userType.discountRate + baseCharge;
		assertThat(customerDependentPriceCalculator.calcPrice(List.of(pet), baseCharge, basePricePerPet, userType)).isEqualTo(result);
	}

	@Test
	public void Price_of_infant_usual_pet_for_new_user_is_calculated_correctly() {
		given(petType.getRare()).willReturn(false);
		pet.setBirthDate(new GregorianCalendar(2020, Calendar.FEBRUARY, 11).getTime());
		UserType userType = UserType.NEW;
		double result = basePricePerPet*1.2;
		assertThat(customerDependentPriceCalculator.calcPrice(List.of(pet), baseCharge, basePricePerPet, userType)).isEqualTo(result);
	}

	@Test
	public void Price_of_infant_usual_pets_for_new_user_is_calculated_correctly() {
		given(petType.getRare()).willReturn(true);
		List<Pet> pets = generateListOfPets();
		UserType userType = UserType.NEW;
		double result = 0;
		for(int i=0; i<5; i++)
			result = result + (basePricePerPet*1.2*1.4);
		result = (result * userType.discountRate) + baseCharge;
		assertThat(customerDependentPriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, userType)).isEqualTo(result);
	}

	@Test
	public void Price_of_infant_usual_pets_for_silver_user_is_calculated_correctly() {
		given(petType.getRare()).willReturn(true);
		List<Pet> pets = generateListOfPets();
		UserType userType = UserType.SILVER;
		double result = 0;
		for(int i=0; i<5; i++)
			result = result + (basePricePerPet*1.2*1.4);
		result = (result + baseCharge) * userType.discountRate;
		assertThat(customerDependentPriceCalculator.calcPrice(pets, baseCharge, basePricePerPet, userType)).isEqualTo(result);
	}


	private List<Pet> generateListOfPets() {
		List<Pet> pets = new ArrayList<>();
		for (int i=0; i<5; i++) {
			Pet pet = new Pet();
			pet.setType(petType);
			pet.setBirthDate(new GregorianCalendar(2020, Calendar.FEBRUARY, 11).getTime());
			pets.add(pet);
		}
		return pets;
	}



}
