package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Parade;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ParadeServiceTest extends AbstractTest {

	// System under test ---------------------------------------

	@Autowired
	private ParadeService paradeService;

	/*
	 * Total coverage of all tests
	 * 
	 * 
	 * Coverage of the total project (%): 30.4
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 9503
	 * 
	 * ############################################################
	 * 
	 * Total coverage by exclusively executing this test class
	 * 
	 * 
	 * Coverage of the total project (%): 5.5
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 1717
	 */

	// Tests ----------------------------------------------------

	// Testing UC 9.2
	@Test
	public void driver() {
		Object testingData[][] = { { "brotherhood2", "parade3", null },// positive
				{ null, "parade3", IllegalArgumentException.class },// nonAuthenticated
				{ "brotherhood1", "parade3", IllegalArgumentException.class },// Wrong
																				// brotherhood
																				// trying
																				// copy
																				// parade
				{ "brotherhood2", "parade1", IllegalArgumentException.class },// Brotherhood
																				// tryng
																				// copy
																				// wrong
																				// parade
				{ "member1", "parade4", IllegalArgumentException.class } // Wrong
																			// actor
		};

		for (int i = 0; i < testingData.length; i++) {
			template((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(Class<?>) testingData[i][2]);
		}
	}

	protected void template(String username, int paradeId, Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(username);
			paradeService.copyParade(paradeId);
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	// Testing UC 8.2 ----------------------------------------------

	@Test
	public void driver2() {
		Object testingData[][] = {
				{ "chapter1", "parade3", "accept", null },// positive
				{ "chapter1", "parade4", "accept",
						IllegalArgumentException.class },// intenta aceptar una
															// parade cuyo
															// estado no es
															// submitted, peta
				{ "chapter1", "parade5", "reject", null },// positive
				{ "chapter1", "parade4", "reject",
						IllegalArgumentException.class } // intenta rechazar una
															// parade cuyo
															// estado no es
															// submitted, peta
		};

		for (int i = 0; i < testingData.length; i++) {
			template2((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(String) testingData[i][2], (Class<?>) testingData[i][3]);

		}
	}

	protected void template2(String username, int paradeId,
			String currentOperation, Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			if (currentOperation.equals("accept")) {
				authenticate(username);
				Parade parade = paradeService.findOne(paradeId);

				paradeService.accept(parade);
				paradeService.flush();
				unauthenticate();

			} else if (currentOperation.equals("reject")) {
				authenticate(username);
				Parade parade = paradeService.findOne(paradeId);

				paradeService.reject(parade);
				paradeService.flush();
				unauthenticate();
			}
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

}
