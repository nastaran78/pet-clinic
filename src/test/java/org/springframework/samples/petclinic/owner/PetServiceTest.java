package org.springframework.samples.petclinic.owner;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;


@SpringBootTest
//@RunWith(Parameterized.class)
public class PetServiceTest {

	public static Pet pet1 , pet2 , pet;
	public Owner owner1 , owner2;
	public PetType petType1 ,petType2;


	@Autowired
	protected PetService petService;

	@Autowired
	protected OwnerRepository ownerRepository;

	@Autowired
	protected PetTypeRepository petTypeRepository;

	@BeforeEach
	public void setUp(){
		petType1 = new PetType();
		petType1.setName("cat");
		petTypeRepository.save(petType1);
		owner1 = new Owner();
		owner1.setFirstName("Ali");
		owner1.setLastName("hamidi");
		owner1.setAddress("tehran");
		owner1.setCity("tehran");
		owner1.setTelephone("09121212121");
		ownerRepository.save(owner1);
		pet1 = new Pet();
		pet1.setId(1);
		pet1.setType(petType1);
		petService.savePet(pet1 , owner1);

		petType2 = new PetType();
		petType2.setName("dog");
		petTypeRepository.save(petType2);
		owner2 = new Owner();
		owner2.setFirstName("Sara");
		owner2.setLastName("ahmdi");
		owner2.setAddress("tehran");
		owner2.setCity("tehran");
		owner2.setTelephone("09126488465");
		ownerRepository.save(owner2);
		pet2 = new Pet();
		pet2.setId(2);
		pet2.setType(petType2);
		petService.savePet(pet2 , owner2);

	}

	@ParameterizedTest
	@MethodSource("localParameters")
	public void findPetTest(int id , String expectedType , String expectedOwnerFirstName){

		pet = petService.findPet(id);

		assert pet.getType().getName().equals(expectedType);
		assert pet.getOwner().getFirstName().equals(expectedOwnerFirstName);
	}

	static Stream<Arguments> localParameters() {
		return Stream.of(
			Arguments.of(1, "cat" , "Ali"), //cache miss
			Arguments.of(2, "dog" , "Sara"), //cache miss
			Arguments.of(1, "cat" , "Ali"), //cache hit
			Arguments.of(2, "dog" , "Sara") //cache hit
		);
	}

}
