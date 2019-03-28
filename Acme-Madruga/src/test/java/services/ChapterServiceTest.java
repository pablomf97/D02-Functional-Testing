package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChapterRepository;
import utilities.AbstractTest;
import domain.Chapter;
import domain.Zone;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ChapterServiceTest extends AbstractTest {

	/*
	 * Total coverage of all tests
	 * 
	 * 
	 * Coverage of the total project (%): 28.5
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 8814
	 * 
	 * ################################################################
	 * 
	 * Total coverage by exclusively executing this test class
	 * 
	 * 
	 * Coverage of the total project (%): 4.7
	 * 
	 * 
	 * Coverage of the total project (lines of codes): 1449
	 */

	// System under test ---------------------------------------
	@Autowired
	private Validator validator;

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private ZoneService zoneService;

	@Autowired
	private ChapterRepository chapterRepository;

	// Tests ----------------------------------------------------

	// Test: Caso de uso:
	// An actor who is not authenticated must be able to:
	// Register to the system as a chapter. (7.1)
	@Test
	public void driver() {
		Object testingData[][] = {
				{ null, "Manantial 5", "carlos12@gmail.com", "carl",
						"618922569", "https://www.google.com/img=219", "Rico",
						"Carlos", "Chapter", "zone1", null },// Positivo:registro
																// normal
				{ null, null, "carlos12@gmail.com", null, null,
						"https://www.google.com/img=219", "Rico", "Carlos",
						"Chapter", "zone2", null },// Positivo:campos no
													// obligatorios nulos
				{ null, "Manantial 5", "carlos12@gmail.com", "carl",
						"618922569", "https://www.google.com/img=219", "Rico",
						"Carlos", "", "zone1",
						java.lang.NullPointerException.class },// Negativo->RN:Title
																// no puedes ser
																// notBlank
				{ null, "Manantial 5", "carlos12@gmail.com", "carl",
						"618922569", "https://www.google.com/img=219", "Rico",
						"Carlos", null, "zone1",
						java.lang.NullPointerException.class },// Negativo->RN:Title
																// no puedes ser
																// notNull
				{ null, "Manantial 5", "carlos12@gmail.com", "carl",
						"618922569", "url@aa", "Rico", "Carlos", "Chapter",
						"zone1", java.lang.NullPointerException.class },// Negativo->RN:Url
																		// no
																		// tiene
																		// el
																		// formato
																		// adecuado
				{ null, "Manantial 5", null, "carl", "618922569",
						"https://www.google.com/img=219", null, null,
						"Chapter", "zone1",
						java.lang.NullPointerException.class },// Negativo->RN:nombre,
																// correo,
																// apellidos no
																// pueden ser
																// nulos
				{ "vxv", "Manantial 5", "carlos12@gmail.com", "carl",
						"618922569", "https://www.google.com/img=219", "Rico",
						"Carlos", "Chapter", "zone1",
						IllegalArgumentException.class },// Negativo->Intento de
															// registro logueado
															// como un actor no
															// existente en la
															// DB

		};

		for (int i = 0; i < testingData.length; i++) {
			template((String) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (String) testingData[i][3],
					(String) testingData[i][4], (String) testingData[i][5],
					(String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8],
					(int) super.getEntityId((String) testingData[i][9]),
					(Class<?>) testingData[i][10]);
		}
	}

	protected void template(String username, String Address, String Email,
			String MiddleName, String PhoneNumber, String Photo,
			String Surname, String Name, String Title, int zone,
			Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			authenticate(username);

			Zone z = this.zoneService.findOne(zone);
			this.registerChapter(Address, Email, MiddleName, PhoneNumber,
					Photo, Surname, Name, Title, z);
			unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void registerChapter(String address, String email,
			String middleName, String phoneNumber, String photo,
			String surname, String name, String title, Zone z) {

		final Chapter principal = this.chapterService.create();
		BindingResult binding = null;

		principal.setAddress(address);
		principal.setEmail(email);
		principal.setMiddleName(middleName);
		principal.setPhoneNumber(phoneNumber);
		principal.setPhoto(photo);
		principal.setSurname(surname);
		principal.setName(name);
		principal.setTitle(title);
		principal.setZone(z);

		this.validator.validate(principal, binding);
		this.chapterService.save(principal);
		this.chapterRepository.flush();

	}

}
