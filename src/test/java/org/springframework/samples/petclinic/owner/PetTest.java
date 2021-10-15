package org.springframework.samples.petclinic.owner;

import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.visit.Visit;


import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.springframework.samples.petclinic.owner.GetObjectField.getPrivateFiled;

@RunWith(Theories.class)
public class PetTest {

	public static Pet pet = new Pet();

	@BeforeClass
	public static void setUp(){
		pet = new Pet();
	}

	@DataPoints
	public static Set[] datasets = { new HashSet(Arrays.asList (LocalDate.parse("2019-01-12") , LocalDate.parse("2017-01-12"))),
									new HashSet(Arrays.asList (LocalDate.parse("2019-01-12") , LocalDate.parse("2019-02-12"))),
									new HashSet(Arrays.asList (LocalDate.parse("2019-01-12") , LocalDate.parse("2019-02-18") , LocalDate.parse("2019-02-15")))};

	@Theory
	public void isSortingVisitsDateCorrectly(Set<LocalDate> dates) throws IllegalAccessException {
		assumeTrue(dates!=null);
		List<LocalDate> dateslist = new ArrayList<>(dates);
		List<Visit> visits = new ArrayList<>();

		for (LocalDate localDate : dateslist) {
			Visit visit = new Visit();
			Objects.requireNonNull(getPrivateFiled(visit, "date")).set(visit, localDate);
			visits.add(visit);
		}
		pet.setVisitsInternal(visits);
		List<Visit> sortedvisit = pet.getVisits();
		Collections.sort(dateslist , Collections.reverseOrder());

		for(int j=0; j<visits.size() ; j++){
			assertTrue("Sorting visits is not correct",sortedvisit.get(j).getDate().equals(dateslist.get(j)));
		}
	}

}
