package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.InceptionRecord;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class InceptionRecordServiceTest extends AbstractTest {

	@Autowired
	private InceptionRecordService inceptionRecordService;

	/*
	 * Total coverage of all tests
	 * 
	 * 
	 * Coverage of the total project (%): 19.0
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 5455
	 * 
	 * ################################################################
	 * 
	 * Total coverage by exclusively executing this test class
	 * 
	 * 
	 * Coverage of the total project (%): 5.2
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 1485
	 */

	/*
	 * ####################### TEST SAVE LEGAL RECORDS #######################
	 */

	@Test
	public void driverEditLegalRecord() {
		Object testingData[][] = {

				/* 1. Correct Testing */

				/* Test 1.1 ----------------------------------------------- */
				{ "brotherhood2", "inceptionRecord1", "title", "description",
						"www.photos.com", null
				/*
				 * 
				 * There is no error expected here, a brotherhood with an
				 * history edits a Inception Record with valid data
				 */
				},

				/* 2. Incorrect Testing */

				/* Test 2.1 ----------------------------------------------- */
				{ "brotherhood2", "inceptionRecord1", "", "description",
						"www.photos.com", ConstraintViolationException.class
				/*
				 * 
				 * In this case, we are expecting a Constraint Violation
				 * Exception because we are trying to edit a Inception Record
				 * record with a blank title
				 */
				},

				/* Test 2.2 ----------------------------------------------- */
				{ "brotherhood2", "inceptionRecord1", "title", "",
						"www.photos.com", ConstraintViolationException.class
				/*
				 * 
				 * In this case, we expect a Constraint Violation Exception
				 * because we are trying to edit a Inception Record record with
				 * a blank description
				 */
				},

				/* Test 2.3 ----------------------------------------------- */
				{ "brotherhood2", "inceptionRecord1", "title", "description",
						"", ConstraintViolationException.class
				/*
				 * 
				 * In this case, we expect a Constraint Violation Exception
				 * because we are trying to edit a Inception Record record with
				 * a blank photos
				 */
				},

				/* Test 2.4 ----------------------------------------------- */
				{ "brotherhood3", "inceptionRecord1", "title", "description",
						"www.photos.com", IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect Illegal Argument Exception is
				 * expected because a brotherhood is trying to edit a Inception
				 * record that it does not own.
				 */
				},

				/* Test 2.5 ----------------------------------------------- */
				{ "member1", "inceptionRecord1", "title", "description",
						"www.photos.com", ClassCastException.class
				/*
				 * 
				 * In this case, we expect Class Cast Exception because an user
				 * that it is logged in as an 'member' is trying to edit a
				 * inception record. This is because the 'save()' in
				 * LegalRecordService casts the user that it is currently logged
				 * in to a Brotherhood.
				 */
				},

				/* Test 2.6 ----------------------------------------------- */
				{ null, "inceptionRecord1", "title", "description",
						"www.photos.com", IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect Illegal Argument Exception because a
				 * user that it is not logged in is trying to edit an inception
				 * record.
				 */
				}, };

		for (int i = 0; i < testingData.length; i++)
			this.templateEditInceptionRecord((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(String) testingData[i][2], (String) testingData[i][3],
					(String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void templateEditInceptionRecord(String username,
			int inceptionid, String title, String description, String photos,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			InceptionRecord inceptionRecord = this.inceptionRecordService
					.findOne(inceptionid);

			inceptionRecord.setTitle(title);
			inceptionRecord.setDescription(description);
			inceptionRecord.setPhotos(photos);

			this.inceptionRecordService.save(inceptionRecord);

			this.inceptionRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * ####### TEST DELETE INCEPTION RECORDS #######
	 */

	@Test
	public void driverDeleteLegalRecord() {
		Object testingData[][] = {

		/* 1. Incorrect deletion */

		/* Test 1.1 ----------------------------------------------- */
		{ "brotherhood2", "inceptionRecord3", IllegalArgumentException.class
		/*
		 * 
		 * In this case, we are expecting a Illegal Argument Exception because a
		 * brotherhood that does not own the record is trying to delete it
		 */
		},

		/* Test 1.2 ----------------------------------------------- */
		{ "member1", "inceptionRecord1", ClassCastException.class
		/*
		 * 
		 * In this case, we are expecting a Illegal Argument Exception because a
		 * member is trying to delete a legal record
		 */
		},

		/* Test 1.3 ----------------------------------------------- */
		{ null, "legalRecord1", IllegalArgumentException.class
		/*
		 * 
		 * In this case, we expect a Illegal Argument Exception because an user
		 * that it is not logged in is trying to edit a Legal record
		 */
		},

		/* 2. Correct deletion */

		/* Test 2.1 ----------------------------------------------- */
		{ "brotherhood2", "inceptionRecord1", null

		/*
		 * 
		 * There is no error expected here, a brotherhood with an history
		 * deletes a Legal Record that it owns
		 */
		}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteInceptionRecord((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(Class<?>) testingData[i][2]);
	}

	protected void templateDeleteInceptionRecord(String username, int legalid,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			InceptionRecord inceptionRecord = this.inceptionRecordService
					.findOne(legalid);

			this.inceptionRecordService.delete(inceptionRecord);

			this.inceptionRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
