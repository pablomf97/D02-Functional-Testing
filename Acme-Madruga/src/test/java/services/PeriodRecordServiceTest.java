package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.PeriodRecord;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PeriodRecordServiceTest extends AbstractTest {

	@Autowired
	private PeriodRecordService periodRecordService;

	/*
	 * Total coverage of all tests
	 * 
	 * 
	 * Coverage of the total project (%): 28.5
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 8814
	 * 
	 * 
	 * ################################################################
	 * 
	 * Total coverage by exclusively executing this test class
	 * 
	 * 
	 * Coverage of the total project (%): 7.3
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 2098
	 */

	/*
	 * 
	 * During this test we are going to try:
	 * 
	 * (RF3.1) Manage their history, which includes listing, displaying,
	 * creating, updating, and deleting its records.
	 */

	/*
	 * ###################### TEST CREATE PERIOD RECORDS ######################
	 */

	@Test
	public void driverCreatePeriodRecord() {
		Object testingData[][] = {

				/* 1. Correct Testing */

				/* Test 1.1 ----------------------------------------------- */
				{ "brotherhood1", "title", "description", 1501, 2099,
						"www.photos.com", null
				/*
				 * 
				 * There is no error expected here, a brotherhood with an
				 * history creates a Period Record with valid data
				 */
				},

				/* Incorrect Testing */

				/* Test 2.1----------------------------------------------- */
				{ "brotherhood1", "", "description", 1501, 2099,
						"www.photos.com", ConstraintViolationException.class
				/*
				 * 
				 * In this case, we are expecting a Constraint Violation
				 * Exception because we are trying to create a Period Record
				 * record with an invalid title
				 */
				},

				/* Test 2.2----------------------------------------------- */
				{ "brotherhood1", "title", "", 1501, 2099, "www.photos.com",
						ConstraintViolationException.class
				/*
				 * 
				 * In this case, we are expecting a Constraint Violation
				 * Exception because we are trying to create a Period Record
				 * record with an invalid description
				 */
				},
				/* Test 2.2 ----------------------------------------------- */
				{ null, "title", "description", 1501, 2099, "www.photos.com",
						IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * an user that it is not logged in is trying to create a Period
				 * record
				 */
				},

				/* Test 2.3 ----------------------------------------------- */
				{ "member1", "title", "description", 1501, 2099,
						"www.photos.com", ClassCastException.class
				/*
				 * 
				 * In this case, we expect Class Cast Exception is expected
				 * because an user that it is logged in as a 'member' is trying
				 * to create a Period record
				 */
				},

				/* Test 2.4 ----------------------------------------------- */
				{ "brotherhood1", "title", "description", 1499, 2099,
						"www.photos.com", IllegalArgumentException.class
				/*
				 * 
				 * 
				 * In this case, we expect Illegal Argument Exception because we
				 * are trying to create a Period Record record with an invalid
				 * start year
				 */
				},

				/* Test 2.5 ----------------------------------------------- */
				{ "brotherhood1", "title", "description", 1501, 2101,
						"www.photos.com", IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * we are trying to create a Period Record record with an
				 * invalid end date
				 */
				},

				/* Test 2.6 ----------------------------------------------- */
				{ "brotherhood1", "title", "description", 1501, 2099, "",
						ConstraintViolationException.class
				/*
				 * 
				 * In this case, we expect a Constraint Violation Exception
				 * because we are trying to create a Period Record record with
				 * blank photos
				 */
				} };

		for (int i = 0; i < testingData.length; i++)
			this.templateCreatePeriodRecord((String) testingData[i][0],
					(String) testingData[i][1], (String) testingData[i][2],
					(Integer) testingData[i][3], (Integer) testingData[i][4],
					(String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	protected void templateCreatePeriodRecord(String username, String title,
			String description, Integer startYear, Integer endYear,
			String photos, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			PeriodRecord periodRecord = this.periodRecordService.create();

			periodRecord.setTitle(title);
			periodRecord.setDescription(description);
			periodRecord.setStartYear(startYear);
			periodRecord.setEndYear(endYear);
			periodRecord.setPhotos(photos);

			this.periodRecordService.save(periodRecord);

			this.periodRecordService.flush();

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

				/* Test 1.1 ----------------------------------------------- */
				{ "brotherhood2", "periodRecord1", "title", "description",
						1501, 2099, "www.photos.com", null
				/*
				 * 
				 * There is no error expected here, a brotherhood with an
				 * history edits a Period Record with valid data
				 */
				},

				/* Test 1.2 ----------------------------------------------- */
				{ "brotherhood2", "periodRecord1", "", "description", 1501,
						2099, "www.photos.com",
						ConstraintViolationException.class
				/*
				 * 
				 * In this case, we are expecting a Constraint Violation
				 * Exception because we are trying to edit a Period Record
				 * record with an invalid title
				 */
				},

				/* Test 1.2 ----------------------------------------------- */
				{ "brotherhood2", "periodRecord1", "title", "", 1501, 2099,
						"www.photos.com", ConstraintViolationException.class
				/*
				 * 
				 * In this case, we are expecting a Constraint Violation
				 * Exception because we are trying to edit a Period Record
				 * record with an invalid description
				 */
				},

				/* Test 1.3 ----------------------------------------------- */
				{ null, "periodRecord1", "title", "description", 1501, 2099,
						"www.photos.com", IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * an user that it is not logged in is trying to edit a Period
				 * record
				 */
				},

				/* Test 1.4 ----------------------------------------------- */
				{ "brotherhood1", "periodRecord1", "title", "description",
						1501, 2099, "www.photos.com",
						IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * a brotherhood that it is not the owner of the record is
				 * trying to edit a Legal record
				 */
				},

				/* Test 1.5 ----------------------------------------------- */
				{ "member1", "periodRecord1", "title", "description", 1501,
						2099, "www.photos.com", ClassCastException.class
				/*
				 * 
				 * In this case, we expect Class Cast Exception is expected
				 * because an user that it is logged in as a 'member' is trying
				 * to create a Legal record.This is because the 'save()' in
				 * LegalRecordService casts the user that it is currently logged
				 * in to a Brotherhood.
				 */
				},

				/* Test 1.6 ----------------------------------------------- */
				{ "brotherhood2", "periodRecord1", "title", "description",
						1499, 2099, "www.photos.com",
						IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect Illegal Argument Exception because we
				 * are trying to edit a Period Record record with an invalid
				 * start date
				 */
				},

				/* Test 1.7 ----------------------------------------------- */
				{ "brotherhood2", "periodRecord1", "title", "description",
						1501, 2101, "www.photos.com",
						IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * we are trying to edit a Period Record record with an invalid
				 * end date
				 */
				},

				/* Test 1.8 ----------------------------------------------- */
				{ "brotherhood2", "periodRecord1", "title", "description",
						1501, 2099, "", ConstraintViolationException.class
				/*
				 * 
				 * In this case, we expect a Constraint Violation Exception
				 * because we are trying to edit a Period Record record with
				 * blank photos
				 */
				} };

		for (int i = 0; i < testingData.length; i++)
			this.templateEditPeriodRecord((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(String) testingData[i][2], (String) testingData[i][3],
					(Integer) testingData[i][4], (Integer) testingData[i][5],
					(String) testingData[i][6], (Class<?>) testingData[i][7]);
	}

	protected void templateEditPeriodRecord(String username, int periodid,
			String title, String description, Integer startYear,
			Integer endYear, String photos, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			PeriodRecord periodRecord = this.periodRecordService
					.findOne(periodid);

			periodRecord.setTitle(title);
			periodRecord.setDescription(description);
			periodRecord.setStartYear(startYear);
			periodRecord.setEndYear(endYear);
			periodRecord.setPhotos(photos);

			this.periodRecordService.save(periodRecord);

			this.periodRecordService.flush();

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

		/* Test 1.1 ----------------------------------------------- */
		{ "brotherhood1", "periodRecord1", IllegalArgumentException.class
		/*
		 * 
		 * In this case, we are expecting a Illegal Argument Exception because a
		 * brotherhood that does not own the record is trying to delete it
		 */
		},

		/* Test 1.2 ----------------------------------------------- */
		{ "member1", "periodRecord1", ClassCastException.class
		/*
		 * 
		 * In this case, we are expecting a Illegal Argument Exception because a
		 * member is trying to delete a Period record
		 */
		},

		/* Test 1.3 ----------------------------------------------- */
		{ null, "periodRecord1", IllegalArgumentException.class
		/*
		 * 
		 * In this case, we expect a Illegal Argument Exception because an user
		 * that it is not logged in is trying to edit a Period record
		 */
		},

		/* 2. Correct deletion */

		/* Test 2.1 ----------------------------------------------- */
		{ "brotherhood2", "periodRecord1", null
		/*
		 * 
		 * There is no error expected here, a brotherhood with an history
		 * deletes a Period Record that it owns
		 */
		} };

		for (int i = 0; i < testingData.length; i++)
			this.templateDeletePeriodRecord((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(Class<?>) testingData[i][2]);
	}

	protected void templateDeletePeriodRecord(String username, int periodid,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			PeriodRecord periodRecord = this.periodRecordService
					.findOne(periodid);

			this.periodRecordService.delete(periodRecord);

			this.periodRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
