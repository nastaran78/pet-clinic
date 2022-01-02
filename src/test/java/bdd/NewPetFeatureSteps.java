package bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
public class NewPetFeatureSteps {

	@Autowired
	PetService petService;

	@Autowired
	OwnerRepository ownerRepository;

	@Autowired
	PetRepository petRepository;

	@Autowired
	PetTypeRepository petTypeRepository;

	Owner george;

	private Pet newPet;


	@Given("There is a pet owner called {string} {string}")
	public void there_is_a_pet_owner_called(String firstName, String lastName) {
		george = new Owner();
		george.setFirstName(firstName);
		george.setLastName(lastName);
		george.setAddress("Los Angeles");
		george.setCity("California");
		george.setTelephone("83433532");
		ownerRepository.save(george);
	}
	@When("she performs new pet service to get a pet to his own")
	public void she_performs_new_pet_service_to_get_a_pet_to_his_own() {
		newPet = petService.newPet(george);
	}
	@Then("The pet is saved for owner")
	public void the_pet_is_saved_for_owner() {
		assertEquals(newPet.getOwner().getId(), george.getId());
	}

}
