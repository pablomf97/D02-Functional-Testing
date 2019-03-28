package sample;

import java.text.SimpleDateFormat;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ParadeService;
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

	@Autowired
	private ParadeService paradeService;



	//Tests ------------------------------------------------------------
	//Testing UC 9.3 ---------------------------------------------------
	//First instance going to test with segments created in the populate.xml
	@Test
	public void driver(){
		Object testingData[][] = {
				{"brotherhood2","segment3","save",null},//Positive
				{"brotherhood1","segment3","save",IllegalArgumentException.class},//brotherhood trying to save not own segemnt.
				{null,"segment1","save",IllegalArgumentException.class},//Non authenticated
				{"brotherhood2","segment3","delete",null},//Positive
				{"brotherhood1","segment3","delete",IllegalArgumentException.class},//brotherhood trying to delete not own segemnt.
				{"brotherhood1","segment1","delete",IllegalArgumentException.class},//Intenta eliminar un segment cuyo iSeditable=false
				{"brotherhood1","segment1","editNegative",IllegalArgumentException.class},//Va a intentar editar un segment que no es editable, peta
				{"brotherhood1","segment2","editPositive",null}//positive edit

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
			if(currentOperation.equals("save")){
				authenticate(username);
				Segment segment = segmentService.findOne(segmentId);

				segmentService.save(segment);
				segmentService.flush();
				unauthenticate();
			}else if(currentOperation.equals("delete")){
				authenticate(username);
				Segment segment = segmentService.findOne(segmentId);

				segmentService.delete(segment);
				unauthenticate();
			}else if(currentOperation.equals("editNegative")){
				authenticate(username);
				Segment segment = segmentService.findOne(segmentId);

				segment.setOriginLatitude(54.5);

				segmentService.save(segment);
				segmentService.flush();
				unauthenticate();
			}else if(currentOperation.equals("editPositive")){
				authenticate(username);
				Segment segment = segmentService.findOne(segmentId);

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

				segment.setExpectedTimeDestination(sdf.parse("05/09/2021 12:00"));

				segmentService.save(segment);
				segmentService.flush();
				unauthenticate();
			}

		}catch(Throwable oops){
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	//Second instance, lets test some segments created by us.

	@Test
	public void driver2(){

		Object testingData[][] = {

				{"brotherhood1","05/09/2021 12:00","05/09/2021 14:00","parade1",-21.2,14.6,30.5,40.4,false,"save",null},//Positive
				{"brotherhood1","05/09/2021 12:00","05/09/2021 14:00","parade1",12.2,13.6,30.5,40.4,false,"save",IllegalArgumentException.class},//longitud y latitud de origen no coincide cno elultimo segment, debe petear.
				{"brotherhood1","05/09/2021 12:00","05/09/2021 10:00","parade1",-21.2,14.6,30.5,40.4,false,"save",IllegalArgumentException.class}//la hora de inicio es posterior a la de fin debe petar

		};

		for(int i = 0; i < testingData.length; i++){
			template2((String)testingData[i][0],
					(String)testingData[i][1],
					(String)testingData[i][2],
					(int) super.getEntityId((String)testingData[i][3]),
					(Double)testingData[i][4],
					(Double)testingData[i][5],
					(Double)testingData[i][6],
					(Double)testingData[i][7],
					(boolean)testingData[i][8],
					(String)testingData[i][9],
					(Class<?>)testingData[i][10]);

		}		
	}

	protected void template2(String username,String originDate,String destinationDate,int paradeId,
			Double originLatitude, Double originLongitude, Double destinationLatitude,Double destinationLongitude,
			boolean isEditable,String currentOperation, Class<?> expected){

		Class<?> caught;

		caught = null;

		try{
			authenticate(username);

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			Segment segment = segmentService.create();

			segment.setExpectedTimeOrigin(sdf.parse(originDate));
			segment.setExpectedTimeDestination(sdf.parse(destinationDate));
			segment.setParade(paradeService.findOne(paradeId));
			segment.setOriginLatitude(originLatitude);
			segment.setOriginLongitude(originLongitude);
			segment.setDestinationLatitude(destinationLatitude);
			segment.setDestinationLongitude(destinationLongitude);
			segment.setIsEditable(isEditable);

			if(currentOperation.equals("save")){
				segmentService.save(segment);
				segmentService.flush();
			}

			unauthenticate();

		}catch(Throwable oops){
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);	

	}


}
