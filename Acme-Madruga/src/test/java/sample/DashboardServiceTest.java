package sample;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Actor;



import repositories.ParadeRepository;
import services.ActorService;


import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DashboardServiceTest extends AbstractTest {
	
	

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ParadeRepository paradeRepository;
	
	//Req.8.1.5:The ratio of parades in draft mode versus parades in final mode.
	@Test
	public void ratioFinalModeGroupedByStatusDriver(){
		Object testingData[][] = {
				{"admin",0.4,0.4,0.2,null},//Positivo
				{"admin",0.4,0.4,0.4,IllegalArgumentException.class},//Negativo->Resultado no esperado según populate.xml
				{"member1",0.4,0.4,0.4,IllegalArgumentException.class}//Negative->Resultado esperado logueado con otro tipo de actor (no permitido)
		};
		
		for(int i = 0; i< testingData.length; i++){
			ratioFinalModeGroupedByStatusTemplate((String)testingData[i][0],
					(Double)testingData[i][1],
					(Double)testingData[i][2],
					(Double)testingData[i][3],
					(Class<?>) testingData[i][4]);
		}
	}
	

	protected void ratioFinalModeGroupedByStatusTemplate(String username, Double accepted,Double rejected , Double submitted , Class<?>expected){
		Class<?> caught;
		
		caught = null;
		
		try{
			authenticate(username);
			
			this.ratioFinalModeGroupedByStatusTest(accepted,rejected,submitted);
			
			unauthenticate();
			
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}
	
	public void ratioFinalModeGroupedByStatusTest ( Double accepted,Double rejected , Double submitted ){
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMINISTRATOR"));

		Assert.isTrue(this.paradeRepository.ratioFinalModeGroupedByStatus().toArray()[0].equals(accepted));
		Assert.isTrue(this.paradeRepository.ratioFinalModeGroupedByStatus().toArray()[1].equals(rejected));
		Assert.isTrue(this.paradeRepository.ratioFinalModeGroupedByStatus().toArray()[2].equals(submitted));
		
	}

	
	
	
	//Req.8.1.4:The ratio of parades in final mode grouped by status.
	@Test
	public void ratioDraftVsFinalDriver(){
		Object testingData[][] = {
				{"admin",0.2,null},//Positivo
				{"admin",0.4,IllegalArgumentException.class},//Negativo->Resultado no esperado según populate.xml
				{"member1",0.2,IllegalArgumentException.class}//Negative->Resultado esperado logueado con otro tipo de actor (no permitido)
		};
		
		for(int i = 0; i< testingData.length; i++){
			ratioDraftVsFinalTemplate((String)testingData[i][0],
					(Double)testingData[i][1],
					(Class<?>) testingData[i][2]);
		}
	}
	

	protected void ratioDraftVsFinalTemplate(String username,Double ratio, Class<?>expected){
		Class<?> caught;
		
		caught = null;
		
		try{
			authenticate(username);
			
			this.ratioDraftVsFinalTest(ratio);
			
			unauthenticate();
			
		}catch(Throwable oops){
			caught = oops.getClass();
		}
		
		super.checkExceptions(expected, caught);
	}
	


	public void ratioDraftVsFinalTest(Double ratio) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "ADMINISTRATOR"));

		Assert.isTrue(this.paradeRepository.ratioDraftVsFinal().equals(ratio));
		
	}

}
