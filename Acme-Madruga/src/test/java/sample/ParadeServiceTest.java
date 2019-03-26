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
<<<<<<< HEAD

	@Autowired
	private ParadeService paradeService;

	//Tests ----------------------------------------------------

=======
	
	@Autowired
	private ParadeService paradeService;
	
	//Tests ----------------------------------------------------
	
>>>>>>> developer
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
<<<<<<< HEAD

=======
		
>>>>>>> developer
		for(int i = 0; i < testingData.length; i++){
			template((String)testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]),
					(Class<?>) testingData[i][2]);
		}
	}
<<<<<<< HEAD

	protected void template(String username, int paradeId, Class<?> expected){
		Class<?> caught;

		caught = null;

=======
	
	protected void template(String username, int paradeId, Class<?> expected){
		Class<?> caught;
		
		caught = null;
		
>>>>>>> developer
		try{
			authenticate(username);
			paradeService.copyParade(paradeId);
			unauthenticate();
		}catch(Throwable oops){
			caught = oops.getClass();
		}
<<<<<<< HEAD

		super.checkExceptions(expected, caught);
	}

}
=======
		
		super.checkExceptions(expected, caught);
	}
		
}
>>>>>>> developer
