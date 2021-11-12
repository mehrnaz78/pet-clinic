package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.Test;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SimplePriceCalculatorTest {

	@Test
	public void Price_for_new_user_is_calculated_correctly() {
		SimplePriceCalculator simplePriceCalculator = new SimplePriceCalculator();
		Pet pet = new Pet();
		PetType petType = mock(PetType.class);
		given(petType.getRare()).willReturn(true);
		pet.setType(petType);
		double baseCharge = 10.0;
		double basePricePerPet = 2.0;
		UserType userType = UserType.NEW;
		double result = (baseCharge+(basePricePerPet*1.2))*(userType.discountRate);
		assertThat(simplePriceCalculator.calcPrice(List.of(pet), baseCharge, basePricePerPet, userType)).isEqualTo(result);
	}

	@Test
	public void Price_for_silver_user_is_calculated_correctly() {
		SimplePriceCalculator simplePriceCalculator = new SimplePriceCalculator();
		Pet pet = new Pet();
		PetType petType = mock(PetType.class);
		given(petType.getRare()).willReturn(false);
		pet.setType(petType);
		double baseCharge = 10.0;
		double basePricePerPet = 2.0;
		UserType userType = UserType.SILVER;
		double result = baseCharge+(basePricePerPet);
		assertThat(simplePriceCalculator.calcPrice(List.of(pet), baseCharge, basePricePerPet, userType)).isEqualTo(result);
	}

}
