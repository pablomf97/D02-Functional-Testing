package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.LinkRecord;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class LinkedRecordServiceTest extends AbstractTest {

	@Autowired
	private LinkRecordService linkRecordService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	// TODO Documentar coverage
	/*
	 * ####################### TEST CREATE LINK RECORDS #######################
	 */

	@Test
	public void driverCreateLinkRecord() {
		Object testingData[][] = {

				/* 1. Correct link record Testing */

				/* Test 1.1 ----------------------------------------------- */
				{ "brotherhood2", "title", "description", "brotherhood1", null
				/*
				 * 
				 * There is no error expected here, a brotherhood with an
				 * history creates a Link Record with valid data
				 */
				},

				/* 2. Incorrect link record Testing */

				/* Test 2.1 ----------------------------------------------- */
				{ "brotherhood2", "", "description", "brotherhood1",
						ConstraintViolationException.class
				/*
				 * 
				 * In this case, we are expecting a Constraint Violation
				 * Exception because we are trying to create a Link Record
				 * record with a blank title
				 */
				},

				/* Test 2.2 ----------------------------------------------- */
				{ "brotherhood2", "title", "", "brotherhood1",
						ConstraintViolationException.class
				/*
				 * 
				 * In this case, we are expecting a Constraint Violation
				 * Exception because we are trying to create a Link Record with
				 * a blank description
				 */
				},

				/* Test 2.3 ----------------------------------------------- */
				{ "brotherhood2", "title", "description", "member1",
						IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * we are trying to create a Link record with an invalid
				 * brotherhood as linked brotherhood
				 */
				},

				/* Test 2.3 ----------------------------------------------- */
				{ null, "title", "description", "brotherhood2",
						IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * an user that it is not logged in is trying to create a Link
				 * record
				 */
				},

				/* Test 2.4 ----------------------------------------------- */
				{ "member1", "title", "description", "brotherhood2",
						ClassCastException.class
				/*
				 * 
				 * In this case, we expect Class Cast Exception is expected
				 * because an user that it is logged in as a 'member' is trying
				 * to create a Legal record
				 */
				} };

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateLinkRecord((String) testingData[i][0],
					(String) testingData[i][1], (String) testingData[i][2],
					(int) super.getEntityId((String) testingData[i][3]),
					(Class<?>) testingData[i][4]);
	}

	protected void templateCreateLinkRecord(String username, String title,
			String description, int brotherhoodid, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			LinkRecord linkRecord = this.linkRecordService.create();

			Brotherhood linkedBrotherhood = this.brotherhoodService
					.findOne(brotherhoodid);

			linkRecord.setTitle(title);
			linkRecord.setDescription(description);
			linkRecord.setLinkedBrotherhood(linkedBrotherhood);

			this.linkRecordService.save(linkRecord);

			this.linkRecordService.flush();

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
	public void driverEditLinkRecord() {
		Object testingData[][] = {

				/* 1. Correct Testing */

				/* Test 1.1 ----------------------------------------------- */
				{ "brotherhood1", "linkRecord2", "title", "description",
						"brotherhood2", null
				/*
				 * 
				 * There is no error expected here, a brotherhood with an
				 * history edits a Link Record with valid data
				 */
				},

				/* 1. Incorrect Testing */

				/* Test 2.1 ----------------------------------------------- */
				{ "brotherhood1", "linkRecord2", "", "description",
						"brotherhood2", ConstraintViolationException.class
				/*
				 * 
				 * In this case, we are expecting a Constraint Violation
				 * Exception because we are trying to edit a Link Record record
				 * with a blank title
				 */
				},

				/* Test 2.2 ----------------------------------------------- */
				{ "brotherhood1", "linkRecord2", "title", "", "brotherhood2",
						ConstraintViolationException.class
				/*
				 * 
				 * In this case, we are expecting a Constraint Violation
				 * Exception because we are trying to edit a Legal Record record
				 * with a blank description
				 */
				},

				/* Test 2.3 ----------------------------------------------- */
				{ "brotherhood1", "linkRecord2", "title", "description",
						"member1", IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * a brotherhood is trying to use a member as a Linked
				 * brotherhood
				 */
				},

				/* Test 2.4 ----------------------------------------------- */
				{ "brotherhood1", "linkRecord2", "title", "description",
						"brotherhood1", IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * a brotherhood is trying to use itself as linked brotherhood
				 */
				},

				/* Test 2.5 ----------------------------------------------- */
				{ null, "linkRecord2", "title", "description", "brotherhood2",
						IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * an user that it is not logged in is trying to edit a Link
				 * record
				 */
				},

				/* Test 2.6 ----------------------------------------------- */
				{ "member1", "linkRecord2", "title", "description",
						"brotherhood2", ClassCastException.class
				/*
				 * 
				 * In this case, we expect Class Cast Exception because an user
				 * that it is logged in as an 'member' is trying to create a
				 * Legal record. This is because the 'save()' in
				 * LegalRecordService casts the user that it is currently logged
				 * in to a Brotherhood.
				 */
				},

				/* Test 2.7 ----------------------------------------------- */
				{ "brotherhood2", "linkRecord2", "title", "description",
						"brotherhood1", IllegalArgumentException.class
				/*
				 * 
				 * In this case, we expect a Illegal Argument Exception because
				 * a brotherhood is trying to edit other's linked record
				 */
				}, };

		for (int i = 0; i < testingData.length; i++)
			this.templateEditLinkRecord((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(String) testingData[i][2], (String) testingData[i][3],
					(int) super.getEntityId((String) testingData[i][4]),
					(Class<?>) testingData[i][5]);
	}

	protected void templateEditLinkRecord(String username, int linkid,
			String title, String description, int brotherhoodid,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			LinkRecord linkRecord = this.linkRecordService.findOne(linkid);

			Brotherhood linkedBrotherhood = this.brotherhoodService
					.findOne(brotherhoodid);

			linkRecord.setTitle(title);
			linkRecord.setDescription(description);
			linkRecord.setLinkedBrotherhood(linkedBrotherhood);

			this.linkRecordService.save(linkRecord);

			this.linkRecordService.flush();

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
	public void driverDeleteLinkRecord() {
		Object testingData[][] = {

		/* 1. Correct deletion */

		/* Test 1.1 ----------------------------------------------- */
		{ "brotherhood1", "linkRecord2", null
		/*
		 * 
		 * There is no error expected here, a brotherhood with an history
		 * deletes a Link Record that it owns
		 */
		},

		/* 2. Incorrect deletion */

		/* Test 2.1 ----------------------------------------------- */
		{ "brotherhood2", "linkRecord1", IllegalArgumentException.class
		/*
		 * 
		 * In this case, we are expecting a Illegal Argument Exception because a
		 * brotherhood that does not own the record is trying to delete it
		 */
		},

		/* Test 2.2 ----------------------------------------------- */
		{ "member1", "linkRecord1", ClassCastException.class
		/*
		 * 
		 * In this case, we are expecting a Illegal Argument Exception because a
		 * member is trying to delete a legal record
		 */
		},

		/* Test 2.3 ----------------------------------------------- */
		{ null, "linkRecord1", IllegalArgumentException.class
		/*
		 * 
		 * In this case, we expect a Illegal Argument Exception because an user
		 * that it is not logged in is trying to edit a Legal record
		 */
		} };

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteLinkRecord((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(Class<?>) testingData[i][2]);
	}

	protected void templateDeleteLinkRecord(String username, int linkid,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			LinkRecord linkRecord = this.linkRecordService.findOne(linkid);

			this.linkRecordService.delete(linkRecord);

			this.linkRecordService.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
