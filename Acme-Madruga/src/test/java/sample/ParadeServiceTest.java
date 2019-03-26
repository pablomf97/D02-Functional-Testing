package sample;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ParadeService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ParadeServiceTest extends AbstractTest {

	//System under test ---------------------------------------

	@Autowired
	private ParadeService paradeService;

	//Tests ----------------------------------------------------

	//Testing UC 9.2
	@Test
	public void driver(){
		Object testingData[][] = {
				{"brotherhood2", "parade3", null},//positive
				{null, "parade3",IllegalArgumentException.class},//nonAuthenticated
				{"brotherhood1","parade3", IllegalArgumentException.class},//Wrong brotherhood trying copy parade
				{"brotherhood2", "parade1", IllegalArgumentException.class},//Brotherhood tryng copy wrong parade
				{"member1", "parade4", IllegalArgumentException.class}//Wrong actor
		};

		for(int i = 0; i < testingData.length; i++){
			template((String)testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(Class<?>) testingData[i][2]);
		}
	}

	protected void template(String username, int paradeId, Class<?> expected){
		Class<?> caught;

		caught = null;

		try{
			authenticate(username);
			paradeService.copyParade(paradeId);
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}