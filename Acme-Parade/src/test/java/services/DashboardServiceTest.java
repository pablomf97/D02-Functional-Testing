package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import repositories.ParadeRepository;
import utilities.AbstractTest;
import domain.Actor;
import domain.Brotherhood;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DashboardServiceTest extends AbstractTest {

	/*
	 * Total coverage of all tests
	 * 
	 * 
	 * Coverage of the total project (%): 30.4
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 9503
	 * 
	 * ################################################################
	 * 
	 * Total coverage by exclusively executing this test class
	 * 
	 * 
	 * Coverage of the total project (%): 8.8
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 2732
	 */

	@Autowired
	private ActorService actorService;

	@Autowired
	private ParadeRepository paradeRepository;

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private InceptionRecordService inceptionRecordService;

	// Req.8.1.5:The ratio of parades in draft mode versus parades in final
	// mode.
	@Test
	public void ratioFinalModeGroupedByStatusDriver() {
		Object testingData[][] = { { "admin", 0.4, 0.2, 0.4, null },// Positivo
				{ "admin", 0.4, 0.4, 0.4, IllegalArgumentException.class },// Negativo->Resultado
																			// no
																			// esperado
																			// seg�n
																			// populate.xml
				{ "member1", 0.4, 0.4, 0.4, IllegalArgumentException.class } // Negative->Resultado
																				// esperado
																				// logueado
																				// con
																				// otro
																				// tipo
																				// de
																				// actor
																				// (no
																				// permitido)
		};

		for (int i = 0; i < testingData.length; i++) {
			ratioFinalModeGroupedByStatusTemplate((String) testingData[i][0],
					(Double) testingData[i][1], (Double) testingData[i][2],
					(Double) testingData[i][3], (Class<?>) testingData[i][4]);
		}
	}

	protected void ratioFinalModeGroupedByStatusTemplate(String username,
			Double accepted, Double rejected, Double submitted,
			Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			authenticate(username);

			this.ratioFinalModeGroupedByStatusTest(accepted, rejected,
					submitted);

			unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void ratioFinalModeGroupedByStatusTest(Double accepted,
			Double rejected, Double submitted) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal,
				"ADMINISTRATOR"));

		Assert.isTrue(this.paradeRepository.ratioFinalModeGroupedByStatus()
				.toArray()[0].equals(accepted));
		Assert.isTrue(this.paradeRepository.ratioFinalModeGroupedByStatus()
				.toArray()[1].equals(rejected));
		Assert.isTrue(this.paradeRepository.ratioFinalModeGroupedByStatus()
				.toArray()[2].equals(submitted));

	}

	// Req.8.1.4:The ratio of parades in final mode grouped by status.
	@Test
	public void ratioDraftVsFinalDriver() {
		Object testingData[][] = { { "admin", 0.2, null },// Positivo
				{ "admin", 0.4, IllegalArgumentException.class },// Negativo->Resultado
																	// no
																	// esperado
																	// seg�n
																	// populate.xml
				{ "member1", 0.2, IllegalArgumentException.class } // Negative->Resultado
																	// esperado
																	// logueado
																	// con otro
																	// tipo de
																	// actor (no
																	// permitido)
		};

		for (int i = 0; i < testingData.length; i++) {
			ratioDraftVsFinalTemplate((String) testingData[i][0],
					(Double) testingData[i][1], (Class<?>) testingData[i][2]);
		}
	}

	protected void ratioDraftVsFinalTemplate(String username, Double ratio,
			Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			authenticate(username);

			this.ratioDraftVsFinalTest(ratio);

			unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void ratioDraftVsFinalTest(Double ratio) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal,
				"ADMINISTRATOR"));

		Assert.isTrue(this.paradeRepository.ratioDraftVsFinal().equals(ratio));

	}

	@Test
	public void ratioAreasNotCoordinatedDriver() {

		/*
		 * (RF8.1.1) The ratio of areas that are not coordinated by any
		 * chapters.
		 */
		Object testingData[][] = {

		{ "admin", 0.5, null },// positive
				{ "admin", 0.7, IllegalArgumentException.class },// non expected
																	// value
				{ "member1", 0.2, IllegalArgumentException.class } // non
																	// authorized
																	// actor

		};

		for (int i = 0; i < testingData.length; i++) {
			ratioAreasNotCoordinatedTemplate((String) testingData[i][0],
					(Double) testingData[i][1], (Class<?>) testingData[i][2]);
		}
	}

	protected void ratioAreasNotCoordinatedTemplate(String username,
			Double ratio, Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			authenticate(username);

			Assert.isTrue(ratio.equals(this.chapterService
					.ratioAreasNotManaged()));

			unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void statisticsChapterDriver() {

		/*
		 * (RF8.1.2) The average, the minimum, the maximum, and the standard
		 * deviation of the number of parades co-ordinated by the chapters.
		 */

		Object testingData[][] = {

				{ "admin", 0.3333333333333333, 0.0, 5.0, 2.7284509239574835,
						null },// positive
				{ "admin", 0.4, 0.0, 5.0, 2.7284509239574835,
						IllegalArgumentException.class },// non expected value
				{ "member1", 0.4, 0.0, 5.0, 2.7284509239574835,
						IllegalArgumentException.class } // non authorized actor

		};

		for (int i = 0; i < testingData.length; i++) {
			ratioAreasNotCoordinatedTemplate((String) testingData[i][0],
					(Double) testingData[i][1], (Double) testingData[i][2],
					(Double) testingData[i][3], (Double) testingData[i][4],
					(Class<?>) testingData[i][5]);
		}
	}

	protected void ratioAreasNotCoordinatedTemplate(String username,
			Double avg, Double min, Double max, Double stdev, Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			authenticate(username);

			Assert.isTrue(avg.equals(this.chapterService
					.paradeChapterStatistics()[0]));
			Assert.isTrue(min.equals(this.chapterService
					.paradeChapterStatistics()[1]));
			Assert.isTrue(max.equals(this.chapterService
					.paradeChapterStatistics()[2]));
			Assert.isTrue(stdev.equals(this.chapterService
					.paradeChapterStatistics()[3]));

			unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void statisticsRecordsDriver() {

		/*
		 * (R4.1.1) The average, the minimum, the maximum, and the standard
		 * deviation of the number of records per history.
		 */

		Object testingData[][] = {

				{ "admin", 5.0, 2.0, 3.6667, 0.6461420286492252, null },// positive
				{ "admin", 0.4, 0.0, 5.0, 2.7284509239574835,
						IllegalArgumentException.class },// non expected value
				{ "member1", 0.4, 0.0, 5.0, 2.7284509239574835,
						IllegalArgumentException.class } // non authorized actor

		};

		for (int i = 0; i < testingData.length; i++) {
			statisticsRecordsTemplate((String) testingData[i][0],
					(Double) testingData[i][1], (Double) testingData[i][2],
					(Double) testingData[i][3], (Double) testingData[i][4],
					(Class<?>) testingData[i][5]);
		}
	}

	protected void statisticsRecordsTemplate(String username, Double max,
			Double min, Double avg, Double stdev, Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			authenticate(username);

			Assert.isTrue(avg.equals(this.inceptionRecordService
					.avgRecordsPerHistory()));
			Assert.isTrue(min.equals(this.inceptionRecordService
					.minRecordsPerHistory()));
			Assert.isTrue(max.equals(this.inceptionRecordService
					.maxRecordsPerHistory()));
			Assert.isTrue(stdev.equals(this.inceptionRecordService
					.stedvRecordsPerHistory()));

			unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void largestHisoryDriver() {

		/* (FR4.1.2) The brotherhood with the largest history. */

		Object testingData[][] = {

		{ "admin", "brotherhood1", null },// positive
				{ "admin", "brotherhood2", IllegalArgumentException.class },// non
																			// expected
																			// value

		};

		for (int i = 0; i < testingData.length; i++) {
			largestHisoryTemplate((String) testingData[i][0],
					(String) testingData[i][1], (Class<?>) testingData[i][2]);
		}
	}

	protected void largestHisoryTemplate(String username, String brotherhood,
			Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			authenticate(username);

			Assert.isTrue(inceptionRecordService.getLargestBrotherhood()
					.equals(brotherhood));

			unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void largestHisoryAvgDriver() {

		/* (FR4.1.3)The brotherhoods whose history is larger than the average. */
		Object testingData[][] = {

				{ "admin", "brotherhood1", "brotherhood2", null },// positive
				{ "admin", "brotherhood2", "brotherhood3",
						IllegalArgumentException.class },// non expected value

		};

		for (int i = 0; i < testingData.length; i++) {
			largestHisoryAvgTemplate((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(int) super.getEntityId((String) testingData[i][2]),
					(Class<?>) testingData[i][3]);
		}
	}

	protected void largestHisoryAvgTemplate(String username, int brotherhood1,
			int brotherhood2, Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			authenticate(username);

			Collection<Brotherhood> bros = inceptionRecordService
					.largerBrosthanAvg();

			for (Brotherhood b : bros) {
				Assert.isTrue(b.getId() == brotherhood1
						|| b.getId() == brotherhood2);
			}

			unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
}
