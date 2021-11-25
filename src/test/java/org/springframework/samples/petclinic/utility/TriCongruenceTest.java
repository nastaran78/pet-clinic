package org.springframework.samples.petclinic.utility;

import com.github.mryf323.tractatus.*;
import com.github.mryf323.tractatus.experimental.extensions.ReportingExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ReportingExtension.class)
@ClauseDefinition(clause = 'a', def = "t1arr[0] < 0")
@ClauseDefinition(clause = 'b', def = "t1arr[0] + t1arr[1] < t1arr[2]")
@ClauseDefinition(clause = 'm', def = "t1arr[0] != t2arr[0]")
@ClauseDefinition(clause = 'n', def = "t1arr[1] != t2arr[1]")
@ClauseDefinition(clause = 't', def = "t1arr[2] != t2arr[2]")
class TriCongruenceTest {

	private static final Logger log = LoggerFactory.getLogger(TriCongruenceTest.class);

	/**
	 * f = ab + cd
	 * !f = !a!c + !a!d + !b!c + !b!d
	 *
	 * CUTPNFP test requirements: {TTFF, FFTT, TFFF, FTFF, FFTF, FFFT}
	 * UTPC test requirements: ab:{TTFF, TTFT, TTTF}  cd:{FFTT, FTTT, TFTT}  !a!c:{FTFF,FFFT,FTFT}
	 *						  !a!d:{FTFF,FFTF,FTTF}	 !b!c:{TFFF,FFFT,TFFT}   !b!d:{TFFF,FFTF,TFTF}
	 *						  UTPC:{TTFF,TTFT,TTTF,FFTT,FTTT,TFTT,FTFF,FFFT,FTFT,FFTF,FTTF,TFFF,TFFT,TFTF}
	 *
	 *	CUTPNFP has UTPC test requirement for implicants in f only but UTPC requires UTPC test requirements for both f and !f
	 *  So CUTPNFP doesnt subsume UTPC. In the example above UTPC's requirements are not a subset of CUTPNFP's requirements.
	 */
	private static boolean questionTwo(boolean a, boolean b, boolean c, boolean d, boolean e) {
		boolean predicate = false;
		predicate = a&&b || c&&d;
		return predicate;
	}

	public boolean first_predicate(double[] t1arr, double[] t2arr) {
		return t1arr[0] != t2arr[0] || t1arr[1] != t2arr[1] || t1arr[2] != t2arr[2];
	}

	public boolean second_predicate(double[] t1arr) {
		return t1arr[0] < 0 || t1arr[0] + t1arr[1] < t1arr[2];
	}

	@ClauseCoverage(
		predicate = "a + b",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
		}
	)
	@Test
	public void secondPredicateFirstClauseTrueCC() {
		Triangle t1 = new Triangle(3, 4, 5);
		Triangle t2 = new Triangle(3, 4, 5);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		assertTrue(areCongruent);
	}


	@ClauseCoverage(
		predicate = "a + b",
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true),
		}
	)
	@Test
	public void secondPredicateSecondClauseTrueCC() {
		Triangle t1 = new Triangle(1, 3, 7);
		Triangle t2 = new Triangle(1, 3, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		assertFalse(areCongruent);
	}

	// ---------------------------------------- CACC --------------------------------------------

	@CACC(
		predicate = "a + b",
		majorClause = 'a',
		valuations = {
			@Valuation(clause = 'a', valuation = true),
			@Valuation(clause = 'b', valuation = false),
		},
		predicateValue = true
	)
	@Test
	public void firstClauseActiveTrue() {
		// This TR is infeasible
	}

	@CACC(
		predicate = "a + b",
		majorClause = 'a',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
		},
		predicateValue = false
	)
	@CACC(
		predicate = "a + b",
		majorClause = 'b',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = false),
		},
		predicateValue = false
	) // Duplicate
	@Test
	public void firstClauseActiveFalse() {
		Triangle t1 = new Triangle(3, 4, 5);
		Triangle t2 = new Triangle(3, 4, 5);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		assertTrue(areCongruent);

	}


	@CACC(
		predicate = "a + b",
		majorClause = 'b',
		valuations = {
			@Valuation(clause = 'a', valuation = false),
			@Valuation(clause = 'b', valuation = true),
		},
		predicateValue = true
	)
	@Test
	public void secondClauseActiveTrue() {
		Triangle t1 = new Triangle(1, 3, 7);
		Triangle t2 = new Triangle(1, 3, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		assertFalse(areCongruent);
	}

	// ------------------------------------------- CUTPNFP ------------------------------------------------

	@UniqueTruePoint(
		predicate = "m + n + t‌",
		dnf = "m + n + t",
		implicant = "m",
		valuations = {
			@Valuation(clause = 'm', valuation = true),
			@Valuation(clause = 'n', valuation = false),
			@Valuation(clause = 't', valuation = false)
		}
	)
	@Test
	public void firstImplicantTrueUTP() {
		Triangle t1 = new Triangle(1, 3, 7);
		Triangle t2 = new Triangle(2, 3, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		assertFalse(areCongruent);
	}

	@UniqueTruePoint(
		predicate = "m + n + t‌",
		dnf = "m + n + t",
		implicant = "n",
		valuations = {
			@Valuation(clause = 'm', valuation = false),
			@Valuation(clause = 'n', valuation = true),
			@Valuation(clause = 't', valuation = false)
		}
	)
	@Test
	public void secondImplicantTrueUTP() {
		Triangle t1 = new Triangle(1, 3, 7);
		Triangle t2 = new Triangle(1, 4, 7);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		assertFalse(areCongruent);
	}

	@UniqueTruePoint(
		predicate = "m + n + t‌",
		dnf = "m + n + t",
		implicant = "t",
		valuations = {
			@Valuation(clause = 'm', valuation = false),
			@Valuation(clause = 'n', valuation = false),
			@Valuation(clause = 't', valuation = true)
		}
	)
	@Test
	public void thirdImplicantTrueUTP() {
		Triangle t1 = new Triangle(1, 3, 7);
		Triangle t2 = new Triangle(1, 3, 8);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		assertFalse(areCongruent);
	}

	@NearFalsePoint(
		predicate = "m + n + t‌", dnf = "m + n + t", implicant = "m", clause = 'm', valuations = {
		@Valuation(clause = 'm', valuation = false),
		@Valuation(clause = 'n', valuation = false),
		@Valuation(clause = 't', valuation = false)
	})
	@NearFalsePoint(
		predicate = "m + n + t‌", dnf = "m + n + t", implicant = "n", clause = 'n', valuations = {
		@Valuation(clause = 'm', valuation = false),
		@Valuation(clause = 'n', valuation = false),
		@Valuation(clause = 't', valuation = false)
	})
	@NearFalsePoint(
		predicate = "m + n + t‌", dnf = "m + n + t", implicant = "t", clause = 't', valuations = {
		@Valuation(clause = 'm', valuation = false),
		@Valuation(clause = 'n', valuation = false),
		@Valuation(clause = 't', valuation = false)
	})
	@Test
	public void allImplicantsFalseNFP() {
		Triangle t1 = new Triangle(1, 3, 7);
		Triangle t2 = new Triangle(2, 4, 8);
		boolean areCongruent = TriCongruence.areCongruent(t1, t2);
		log.debug("Triangles identified as '{}'.", areCongruent ? "Congruent" : "Not Congruent");
		assertFalse(areCongruent);
	}

}
