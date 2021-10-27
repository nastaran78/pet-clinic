package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class VisitControllerTest {

	@InjectMocks
	private VisitController visitController;

	@Mock
	private VisitRepository visits;

	@Mock
	private PetRepository pets;

	private MockMvc mockMvc;

	private Visit visit;
	private BindingResult result;


	@BeforeEach
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		visitController = new VisitController(visits , pets);
		visit = new Visit();
		result = new BeanPropertyBindingResult(visit, "visit");
		mockMvc = MockMvcBuilders.standaloneSetup(visitController).build();
	}

	@Test
	public void testProcessNewVisitForm_StateVerification(){
		assertTrue("redirect:/owners/{ownerId}".equals(visitController.processNewVisitForm(visit ,result)));

		ObjectError error = new ObjectError("visitValidation","visit not valid");
		result.addError(error);

		assertTrue("pets/createOrUpdateVisitForm".equals(visitController.processNewVisitForm(visit ,result)));
	}

	@Test
	public void testProcessNewVisitForm_StateVerification_RestApi() throws Exception {
		Pet pet = new Pet();
		pet.setId(1);
		Owner owner = new Owner();
		owner.setId(2);
		pet.setOwner(owner);
		when(this.pets.findById(pet.getId())).thenReturn(new Pet());
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", owner.getId(), pet.getId())).andExpect(status().isOk());
	}

	@Test
	public void testProcessNewVisitForm_Behavior_ValidInput() {
		visitController.processNewVisitForm(visit, result);
		verify(visits, times(1)).save(visit);
		ArgumentCaptor<Visit> visitCaptor = ArgumentCaptor.forClass(Visit.class);
		verify(visits).save(visitCaptor.capture());
		assertEquals(visitCaptor.getValue().getPetId(), visit.getPetId());
	}

	@Test
	public void testProcessNewVisitForm_Behavior_InvalidInput() {
		ObjectError error = new ObjectError("visitValidation","visit not valid");
		result.addError(error);

		visitController.processNewVisitForm(visit , result);

		verify(visits , times(0)).save(visit);
	}


}
