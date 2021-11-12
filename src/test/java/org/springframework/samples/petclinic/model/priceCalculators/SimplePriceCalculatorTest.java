package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SimplePriceCalculatorTest {

	private SimplePriceCalculator simplePriceCalculator;
	private Pet pet;
	private PetType petType;
	private double baseCharge, basePricePerPet;

	@Before
	public void setUp() {
		simplePriceCalculator = new SimplePriceCalculator();
		pet = new Pet();
		petType = mock(PetType.class);
		baseCharge= 10.0;
		basePricePerPet = 2.0;
		pet.setType(petType);
	}

	@Test
	public void Price_of_rare_pet_for_new_user_is_calculated_correctly() {
		given(petType.getRare()).willReturn(true);
		UserType userType = UserType.NEW;
		double result = (baseCharge+(basePricePerPet*1.2))*(userType.discountRate);
		assertThat(simplePriceCalculator.calcPrice(List.of(pet), baseCharge, basePricePerPet, userType)).isEqualTo(result);
	}

	@Test
	public void Price_of_usual_pet_for_silver_user_is_calculated_correctly() {
		given(petType.getRare()).willReturn(false);
		UserType userType = UserType.SILVER;
		double result = baseCharge+(basePricePerPet);
		assertThat(simplePriceCalculator.calcPrice(List.of(pet), baseCharge, basePricePerPet, userType)).isEqualTo(result);
	}

}
