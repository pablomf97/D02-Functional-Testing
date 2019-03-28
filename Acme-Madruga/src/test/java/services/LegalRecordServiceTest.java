package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import services.LegalRecordService;
import utilities.AbstractTest;
import domain.LegalRecord;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class LegalRecordServiceTest extends AbstractTest {

	@Autowired
	private LegalRecordService legalRecordService;

	// TODO Documentar coverage
	/*
	 * ####################### TEST CREATE LEGAL RECORDS #######################
	 */

	@Test
	public void driverCreateLegalRecord() {
		Object testingData[][] = {

				/* 1. Attribute 'VAT' Testing */

				/* Test 1.1 ----------------------------------------------- */
				{ "brotherhood1", "title", "description", "name", .21,
						"legalaw", null
				/*
				 * 
				 * There is no error expected here, a brotherhood with an
				 * history creates a Legal Record with valid data
				 */
				},

				/* Test 1.2 ----------------------------------------------- */
				{ "brotherhood1", "title", "description", "name", null,
						"legalaw", IllegalArgumentException.class
				/*
				 * 
				 * In this case, we are expecting a Illegal Argument Exception
				 * because we are trying to create a Legal Record record with a
				 * null VAT
				 */
				},

				/* Test 1.3 ----------------------------------------------- */
				{ "brotherhood1", "title", "description", "name", 2.,
						"legalaw", ConstraintViolationException.class
				/*
				 * 
				 * In this case, we are expecting a Constraint Violation
				 * Exception because we are trying to create a Legal Record
				 * record with an out of bounds VAT
				 */
				},

				/* 2. Attribute 'brotherhood' Testing */

				/* Test 2.1 ----------------------------------------------- */
				{ null, "title", "description", "name", .21, "legalaw",
						IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * an user that it is not logged in is trying to create a Legal
				 * record
				 */
				},

				/* Test 2.2 ----------------------------------------------- */
				{ "member1", "title", "description", "name", .21, "legalaw",
						IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect Illegal Argument Exception is
				 * expected because an user that it is logged in as a 'member'
				 * is trying to create a Legal record
				 */
				},

				/* Test 2.3 ----------------------------------------------- */
				{ "admin", "title", "description", "name", .21, "legalaw",
						IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect Illegal Argument Exception because an
				 * user that it is logged in as an 'admin' is trying to create a
				 * Legal record
				 */
				},

				/* 3. Attribute 'title' Testing */

				/* Test 3.1 ----------------------------------------------- */
				{ "brotherhood1", "", "description", "name", .21, "legalaw",
						ConstraintViolationException.class
				/*
				 * 
				 * 
				 * In this case, we expect Constraint Violation Exception
				 * because we are trying to create a Legal Record record with a
				 * blank title
				 */
				},

				/* 4. Attribute 'description' Testing */

				/* Test 4.1 ----------------------------------------------- */
				{ "brotherhood1", "title", "", "name", .21, "legalaw",
						ConstraintViolationException.class
				/*
				 * 
				 * In this case, we expect a Constraint Violation Exception
				 * because we are trying to create a Legal Record record with a
				 * blank description
				 */
				},

				/* 5. Attribute 'name' Testing */

				/* Test 5.1 ----------------------------------------------- */
				{ "brotherhood1", "title", "description", "", .21, "legalaw",
						ConstraintViolationException.class
				/*
				 * 
				 * In this case, we expect a Constraint Violation Exception
				 * because we are trying to create a Legal Record record with a
				 * blank name
				 */
				},

				/* 6. Attribute 'laws' Testing */

				/* Test 6.1 ----------------------------------------------- */
				{ "brotherhood1", "title", "description", "name", .21, "",
						ConstraintViolationException.class
				/*
				 * 
				 * In this case, we expect a Constraint Violation Exception
				 * because we are trying to create a Legal Record record with a
				 * blank law
				 */
				} };

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateLegalRecord((String) testingData[i][0],
					(String) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (Double) testingData[i][4],
					(String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	protected void templateCreateLegalRecord(String username, String title,
			String description, String name, Double VAT, String laws,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			LegalRecord legalRecord = this.legalRecordService.create();

			legalRecord.setTitle(title);
			legalRecord.setDescription(description);
			legalRecord.setName(name);
			legalRecord.setVAT(VAT);
			legalRecord.setLaws(laws);

			this.legalRecordService.save(legalRecord);

			this.legalRecordService.flush();

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
	public void driverEditLegalRecord() {
		Object testingData[][] = {

				/* 1. Attribute 'VAT' Testing */

				/* Test 1.1 ----------------------------------------------- */
				{ "brotherhood2", "legalRecord1", "title", "description",
						"name", .21, "legalaw", null
				/*
				 * 
				 * There is no error expected here, a brotherhood with an
				 * history edits a Legal Record with valid data
				 */
				},

				/* Test 1.2 ----------------------------------------------- */
				{ "brotherhood2", "legalRecord1", "title", "description",
						"name", null, "legalaw", IllegalArgumentException.class
				/*
				 * 
				 * In this case, we are expecting a Illegal Argument Exception
				 * because we are trying to edit a Legal Record record with a
				 * null VAT
				 */
				},

				/* Test 1.3 ----------------------------------------------- */
				{ "brotherhood2", "legalRecord1", "title", "description",
						"name", 2., "legalaw",
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
				{ null, "legalRecord1", "title", "description", "name", .21,
						"legalaw", IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * an user that it is not logged in is trying to edit a Legal
				 * record
				 */
				},

				/* Test 2.2 ----------------------------------------------- */
				{ "brotherhood1", "legalRecord1", "title", "description",
						"name", .21, "legalaw", IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * a brotherhood that it is not the owner of the record is
				 * trying to edit a Legal record
				 */
				},

				/* Test 2.3 ----------------------------------------------- */
				{ "member1", "legalRecord1", "title", "description", "name",
						.21, "legalaw", ClassCastException.class
				/*
				 * 
				 * In this case, we expect Illegal Argument Exception is
				 * expected because an user that it is logged in as a 'member'
				 * is trying to create a Legal record.This is because the
				 * 'save()' in LegalRecordService casts the user that it is
				 * currently logged in to a Brotherhood.
				 */
				},

				/* Test 2.4 ----------------------------------------------- */
				{ "admin", "legalRecord1", "title", "description", "name", .21,
						"legalaw", ClassCastException.class
				/*
				 * 
				 * In this case, we expect Class Cast Exception because an user
				 * that it is logged in as an 'admin' is trying to create a
				 * Legal record. This is because the 'save()' in
				 * LegalRecordService casts the user that it is currently logged
				 * in to a Brotherhood.
				 */
				},

				/* 3. Attribute 'title' Testing */

				/* Test 3.1 ----------------------------------------------- */
				{ "brotherhood2", "legalRecord1", "", "description", "name",
						.21, "legalaw", ConstraintViolationException.class
				/*
				 * 
				 * In this case, we expect Constraint Violation Exception
				 * because we are trying to edit a Legal Record record with a
				 * blank title
				 */
				},

				/* 4. Attribute 'description' Testing */

				/* Test 4.1 ----------------------------------------------- */
				{ "brotherhood2", "legalRecord1", "title", "", "name", .21,
						"legalaw", ConstraintViolationException.class
				/*
				 * 
				 * In this case, we expect a Constraint Violation Exception
				 * because we are trying to edit a Legal Record record with a
				 * blank description
				 */
				},

				/* 5. Attribute 'name' Testing */

				/* Test 5.1 ----------------------------------------------- */
				{ "brotherhood2", "legalRecord1", "title", "description", "",
						.21, "legalaw", ConstraintViolationException.class
				/*
				 * 
				 * In this case, we expect a Constraint Violation Exception
				 * because we are trying to edit a Legal Record record with a
				 * blank name
				 */
				},

				/* 6. Attribute 'laws' Testing */

				/* Test 6.1 ----------------------------------------------- */
				{ "brotherhood2", "legalRecord1", "title", "description",
						"name", .21, "", ConstraintViolationException.class
				/*
				 * 
				 * In this case, we expect a Constraint Violation Exception
				 * because we are trying to edit a Legal Record record with a
				 * blank law
				 */
				} };

		for (int i = 0; i < testingData.length; i++)
			this.templateEditLegalRecord((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(String) testingData[i][2], (String) testingData[i][3],
					(String) testingData[i][4], (Double) testingData[i][5],
					(String) testingData[i][6], (Class<?>) testingData[i][7]);
	}

	protected void templateEditLegalRecord(String username, int legalid,
			String title, String description, String name, Double VAT,
			String laws, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			LegalRecord legalRecord = this.legalRecordService.findOne(legalid);

			legalRecord.setTitle(title);
			legalRecord.setDescription(description);
			legalRecord.setName(name);
			legalRecord.setVAT(VAT);
			legalRecord.setLaws(laws);

			this.legalRecordService.save(legalRecord);

			this.legalRecordService.flush();

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
	public void driverDeleteLegalRecord() {
		Object testingData[][] = {

		/* 1. Incorrect deletion */

		/* Test 2.1 ----------------------------------------------- */
		{ "brotherhood1", "legalRecord1", IllegalArgumentException.class
		/*
		 * 
		 * In this case, we are expecting a Illegal Argument Exception because a
		 * brotherhood that does not own the record is trying to delete it
		 */
		},

		/* Test 2.2 ----------------------------------------------- */
		{ "member1", "legalRecord1", ClassCastException.class
		/*
		 * 
		 * In this case, we are expecting a Illegal Argument Exception because a
		 * member is trying to delete a legal record
		 */
		},

		/* Test 2.3 ----------------------------------------------- */
		{ null, "legalRecord1", IllegalArgumentException.class
		/*
		 * 
		 * In this case, we expect a Illegal Argument Exception because an user
		 * that it is not logged in is trying to edit a Legal record
		 */
		},/* 1. Correct deletion */

		/* Test 1.1 ----------------------------------------------- */
		{ "brotherhood2", "legalRecord1", null
		/*
		 * 
		 * There is no error expected here, a brotherhood with an history
		 * deletes a Legal Record that it owns
		 */
		} };

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteLegalRecord((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(Class<?>) testingData[i][2]);
	}

	protected void templateDeleteLegalRecord(String username, int legalid,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			LegalRecord legalRecord = this.legalRecordService.findOne(legalid);

			this.legalRecordService.delete(legalRecord);

			this.legalRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
