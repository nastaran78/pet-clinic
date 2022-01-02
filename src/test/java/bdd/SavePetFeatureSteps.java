package bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class SavePetFeatureSteps {

	@Autowired
	PetService petService;

	@Autowired
	OwnerRepository ownerRepository;

	@Autowired
	PetRepository petRepository;

	@Autowired
	PetTypeRepository petTypeRepository;

	private Owner george;
	private PetType petType;


	@Given("There is a pet owner called {string} {string}")
	public void thereIsAPetOwnerCalled(String firstName, String lastName) {
		george = new Owner();
		george.setFirstName(firstName);
		george.setLastName(lastName);
		george.setAddress("Najibie - Kooche shahid abbas alavi");
		george.setCity("Tehran");
		george.setTelephone("09191919223");
		ownerRepository.save(george);
	}

	@When("He performs save pet service to add a pet to his list")
	public void hePerformsSavePetService() {
		Pet pet = new Pet();
		pet.setType(petType);
		petService.savePet(pet, george);
	}

	@Then("The pet is saved successfully")
	public void petIsSaved() {
		assertNotNull(petService.findPet(petType.getId()));
	}

	@Given("There is some predefined pet types like {string}")
	public void thereIsSomePredefinedPetTypesLike(String petTypeName) {
		petType = new PetType();
		petType.setName(petTypeName);
		petTypeRepository.save(petType);
	}
}
