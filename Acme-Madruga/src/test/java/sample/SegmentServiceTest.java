package sample;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.SegmentService;
import utilities.AbstractTest;
import domain.Segment;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SegmentServiceTest extends AbstractTest{

	//System under test ------------------------------------------------
	
	@Autowired
	private SegmentService segmentService;
	
	//Tests ------------------------------------------------------------
	//Testing UC 9.3 ---------------------------------------------------
	
	@Test
	public void driver(){
		Object testingData[][] = {
				{"brotherhood2","segment1","save",null}//Positive
		};
		
		for(int i = 0; i< testingData.length; i++){
			template((String)testingData[i][0],
					(int) super.getEntityId((String)testingData[i][1]),
					(String)testingData[i][2],
					(Class<?>) testingData[i][3]);
		}
	}
	
	protected void template(String username, int segmentId, String currentOperation, Class<?>expected){
		Class<?> caught;
		
		caught = null;
		
		try{
			authenticate(username);
			Segment segment = this.segmentService.findOne(segmentId);
			
			segmentService.save(segment);
			unauthenticate();
			
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}
	
	
}
