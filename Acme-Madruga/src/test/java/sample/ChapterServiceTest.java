package sample;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ChapterService;

import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ChapterServiceTest extends AbstractTest {
	
	//System under test ---------------------------------------


		@Autowired
		private ChapterService chapterService;

		//Tests ----------------------------------------------------

		//Testing UC 7.1
		
		@Test
		public void driver(){
			Object testingData[][] = {
					{null, null},//positive
					
			};

			for(int i = 0; i < testingData.length; i++){
				template((String)testingData[i][0],
						(Class<?>) testingData[i][1]);
			}
		}
		
		protected void template(String username, Class<?> expected){
			Class<?> caught;

			caught = null;


			try{
				authenticate(username);
				
				unauthenticate();
			}catch(Throwable oops){
				caught = oops.getClass();
			}


			super.checkExceptions(expected, caught);
		}


}
