package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.MiscellaneousRecord;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MiscellaneousRecordServiceTest extends AbstractTest {

	@Autowired
	private MiscellaneousRecordService miscellaneousRecordService;

	// TODO Documentar coverage
	/*
	 * ####################### TEST CREATE LEGAL RECORDS #######################
	 */

	@Test
	public void driverCreateMiscellaneousRecord() {
		Object testingData[][] = {

		/* 1. Attribute 'VAT' Testing */

		/* Test 1.1 ----------------------------------------------- */
		{ "brotherhood1", "title", "description", null
		/*
		 * 
		 * There is no error expected here, a brotherhood with an history
		 * creates a Legal Record with valid data
		 */
		},

		/* Test 1.2 ----------------------------------------------- */
		{ "brotherhood1", "", "description", ConstraintViolationException.class
		/*
		 * 
		 * In this case, we are expecting a Illegal Argument Exception because
		 * we are trying to create a Legal Record record with a null VAT
		 */
		},

		/* Test 1.3 ----------------------------------------------- */
		{ "brotherhood1", "title", "", ConstraintViolationException.class
		/*
		 * 
		 * In this case, we are expecting a Constraint Violation Exception
		 * because we are trying to create a Legal Record record with an out of
		 * bounds VAT
		 */
		},

		/* 2. Attribute 'brotherhood' Testing */

		/* Test 2.1 ----------------------------------------------- */
		{ null, "title", "description", IllegalArgumentException.class
		/*
		 * 
		 * In this case, we expect a Illegal Argument Exception because an user
		 * that it is not logged in is trying to create a Legal record
		 */
		},

		/* Test 2.2 ----------------------------------------------- */
		{ "member1", "title", "description", ClassCastException.class
		/*
		 * 
		 * In this case, we expect Illegal Argument Exception is expected
		 * because an user that it is logged in as a 'member' is trying to
		 * create a Legal record
		 */
		},

		/* 3. Attribute 'title' Testing */

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateMiscellaneousRecord((String) testingData[i][0],
					(String) testingData[i][1], (String) testingData[i][2],
					(Class<?>) testingData[i][3]);
	}

	protected void templateCreateMiscellaneousRecord(String username,
			String title, String description, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService
					.create();

			miscellaneousRecord.setTitle(title);
			miscellaneousRecord.setDescription(description);

			this.miscellaneousRecordService.save(miscellaneousRecord);

			this.miscellaneousRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST SAVE LEGAL RECORDS #######################
	 */

	@Test
	public void driverEditMiscellaneousRecord() {
		Object testingData[][] = {

				/* 1. Attribute 'VAT' Testing */

				/* Test 1.1 ----------------------------------------------- */
				{ "brotherhood2", "miscellaneousRecord1", "title",
						"description", null
				/*
				 * 
				 * There is no error expected here, a brotherhood with an
				 * history edits a Legal Record with valid data
				 */
				},

				/* Test 1.3 ----------------------------------------------- */
				{ "brotherhood2", "miscellaneousRecord1", "", "description",
						ConstraintViolationException.class
				/*
				 * 
				 * In this case, we are expecting a Constraint Violation
				 * Exception because we are trying to edit a Legal Record record
				 * with an out of bounds VAT
				 */
				},

				/* 2. Actor Testing */

				/* Test 2.1 ----------------------------------------------- */
				{ null, "miscellaneousRecord1", "title", "description",
						IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * an user that it is not logged in is trying to edit a Legal
				 * record
				 */
				},

				/* Test 2.2 ----------------------------------------------- */
				{ "brotherhood3", "miscellaneousRecord1", "title",
						"description", IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * a brotherhood that it is not the owner of the record is
				 * trying to edit a Legal record
				 */
				},

				/* Test 2.3 ----------------------------------------------- */
				{ "member1", "miscellaneousRecord1", "title", "description",
						ClassCastException.class
				/*
				 * 
				 * In this case, we expect Illegal Argument Exception is
				 * expected because an user that it is logged in as a 'member'
				 * is trying to create a Legal record.This is because the
				 * 'save()' in LegalRecordService casts the user that it is
				 * currently logged in to a Brotherhood.
				 */
				},

				/* 3. Attribute 'title' Testing */

				/* Test 3.1 ----------------------------------------------- */
				{ "brotherhood2", "miscellaneousRecord1", "title", "",
						ConstraintViolationException.class
				/*
				 * 
				 * In this case, we expect Constraint Violation Exception
				 * because we are trying to edit a Legal Record record with a
				 * blank title
				 */
				} };

		for (int i = 0; i < testingData.length; i++)
			this.templateEditMiscellaneousRecord((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(String) testingData[i][2], (String) testingData[i][3],
					(Class<?>) testingData[i][4]);
	}

	protected void templateEditMiscellaneousRecord(String username,
			int legalid, String title, String description, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService
					.findOne(legalid);

			miscellaneousRecord.setTitle(title);
			miscellaneousRecord.setDescription(description);

			this.miscellaneousRecordService.save(miscellaneousRecord);

			this.miscellaneousRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * ####################### TEST DELETE LEGAL RECORDS #######################
	 */

	@Test
	public void driverDeleteMiscellaneousRecord() {
		Object testingData[][] = {

				/* 1. Correct deletion */

				/* 2. Incorrect deletion */

				/* Test 2.1 ----------------------------------------------- */
				{ "brotherhood1", "miscellaneousRecord1",
						IllegalArgumentException.class
				/*
				 * 
				 * In this case, we are expecting a Illegal Argument Exception
				 * because a brotherhood that does not own the record is trying
				 * to delete it
				 */
				},

				/* Test 2.2 ----------------------------------------------- */
				{ "member1", "miscellaneousRecord1", ClassCastException.class
				/*
				 * 
				 * In this case, we are expecting a Illegal Argument Exception
				 * because a member is trying to delete a legal record
				 */
				},

				/* Test 2.3 ----------------------------------------------- */
				{ null, "miscellaneousRecord1", IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * an user that it is not logged in is trying to edit a Legal
				 * record
				 */
				}, /* Test 1.1 ----------------------------------------------- */
				{ "brotherhood2", "miscellaneousRecord1", null
				/*
				 * 
				 * There is no error expected here, a brotherhood with an
				 * history deletes a Legal Record that it owns
				 */
				} };

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteMiscellaneousRecord((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(Class<?>) testingData[i][2]);
	}

	protected void templateDeleteMiscellaneousRecord(String username,
			int legalid, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			MiscellaneousRecord miscellaneousRecord = this.miscellaneousRecordService
					.findOne(legalid);

			this.miscellaneousRecordService.delete(miscellaneousRecord);

			this.miscellaneousRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
