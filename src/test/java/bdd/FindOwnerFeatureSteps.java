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


public class FindOwnerFeatureSteps {
	Owner george;
	@Autowired
	PetService petService;
	@Autowired
	PetTimedCache petTimedCache;
	@Autowired
	OwnerRepository ownerRepository;
	@Autowired
	Logger logger;

	Owner foundedOwner;


	@Given("There is a pet owner with id {int}")
	public void there_is_a_pet_owner_with_id(int id) {
		george = new Owner();
		george.setId(id);
		george.setFirstName("George");
		george.setLastName("Hamilton");
		george.setAddress("Los Angeles");
		george.setCity("California");
		george.setTelephone("83433532");
		ownerRepository.save(george);
	}
	@When("We perform find owner pet service to get owner as return value")
	public void we_perform_find_owner_pet_service_to_get_owner_as_return_value() {
		foundedOwner = petService.findOwner(george.getId());
	}
	@Then("The owner is returned successfully")
	public void the_owner_is_returned_successfully() {
		assertEquals(foundedOwner.getFirstName(), george.getFirstName());
		assertEquals(foundedOwner.getLastName(), george.getLastName());
		assertEquals(foundedOwner.getId(), george.getId());
	}

}
