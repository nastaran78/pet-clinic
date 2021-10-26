package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class VisitControllerTest {

	@InjectMocks
	private VisitController visitController;

	@Mock
	private VisitRepository visits;

	@Mock
	private PetRepository pets;


	private Visit visit;
	private BindingResult result;


	@BeforeEach
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		visitController = new VisitController(visits , pets);
		visit = new Visit();
		result = new BeanPropertyBindingResult(visit, "visit");
	}

	@Test
	public void testProcessNewVisitForm_StateVerification(){
		assertTrue("redirect:/owners/{ownerId}".equals(visitController.processNewVisitForm(visit ,result)));

		ObjectError error = new ObjectError("visitValidation","visit not valid");
		result.addError(error);

		assertTrue("pets/createOrUpdateVisitForm".equals(visitController.processNewVisitForm(visit ,result)));
	}

	@Test
	public void testProcessNewVisitForm_BehaviorVerification(){
		visitController.processNewVisitForm(visit , result);

		ObjectError error = new ObjectError("visitValidation","visit not valid");
		result.addError(error);

		visitController.processNewVisitForm(visit , result);

		verify(visits , times(1)).save(visit);
	}

}
