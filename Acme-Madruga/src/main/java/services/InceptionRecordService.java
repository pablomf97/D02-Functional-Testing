package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import domain.Actor;
import domain.Brotherhood;
import domain.InceptionRecord;


import repositories.InceptionRecordRepository;

@Service
@Transactional
public class InceptionRecordService {

	@Autowired
	private InceptionRecordRepository inceptionRecordRepository;


	@Autowired
	private ActorService actorService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private MiscellaneousRecordService miscellaneousRecordService;

	@Autowired
	private LegalRecordService legalRecordService;

	@Autowired
	private LinkRecordService linkRecordService;
	@Autowired
	private PeriodRecordService periodRecordService;


	public InceptionRecord findOne(int id){
		InceptionRecord res;

		res=this.inceptionRecordRepository.findOne(id);
		return res;

	}
	public Collection<InceptionRecord> findAll(){

		return this.inceptionRecordRepository.findAll();

	}


	public InceptionRecord create(){
		InceptionRecord res=new InceptionRecord();
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		return res;

	}

	public InceptionRecord save(InceptionRecord inceptionRecord){
		InceptionRecord res;
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		Assert.notNull(inceptionRecord.getTitle(),"not.null");
		Assert.notNull(inceptionRecord.getDescription(),"not.null");
		Assert.notNull(inceptionRecord.getPhotos(),"not.null");//falta anotacion @URL?**
		res=this.inceptionRecordRepository.save(inceptionRecord);
		Assert.notNull(res);
		return res;
	}

	public void delete(InceptionRecord inceptionRecord){
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		//comprobar que pertenece a ese brotherhood el record

		Assert.notNull(inceptionRecord);
		Assert.isTrue(inceptionRecord.getId() != 0, "wrong.id");

		this.inceptionRecordRepository.delete(inceptionRecord);
		Assert.isTrue(inceptionRecord==null);
	}

	//ancillary methods


	public Collection<String> getSplitPhotos(final String photos) {
		final Collection<String> res = new ArrayList<>();
		final String[] slice = photos.split("< >");
		for (final String p : slice)
			res.add(p);
		return res;
	}
	public String checkURLPhotos(final Collection<String> photos) {
		String result = "";
		if (!photos.isEmpty())
			for (final String p : photos)
				result = result + p.trim() + "< >";
		return result;
	}

	// The brotherhood with the largest history.
	public String getLargestBrotherhood(){
		Collection<Brotherhood> bros;
		List<Integer> results=new ArrayList<Integer>();
		bros=this.brotherhoodService.findAll();
		Integer maxValue;

		for(Brotherhood b:bros){
			results.add(this.getSizeAll(b));
		}
		maxValue=Collections.max(results);
		return null ;

	}
	//number of records of a bro
	public Integer getSizeAll(Brotherhood b){
		Integer res ;
		Integer inceptions=0;
		if(b.getHistory().getInceptionRecord()!=null){
			inceptions=1;
		}
		Integer legals=b.getHistory().getLegalRecords().size();
		Integer miscs=b.getHistory().getMiscellaneousRecords().size();
		Integer periods=b.getHistory().getPeriodRecords().size();
		Integer links=b.getHistory().getLinkRecords().size();

		res=inceptions+legals+miscs+periods+links;

		return res;
	}
	//The average
	
	public Double avgRecordsPerHistory(){
		//sumo todas las avg y luego divido entre 5 
		
		return null;
		
	}
	

	//the minimum
	
	public Integer minRecordsPerHistory(){
		return null;
		//sumo todos los mins de stats query
	}
	
	

	//the maximum
	public Integer maxRecordsPerHistory(){
		////sumo todos los max de stats query
		return null;
		
	}
	
	

	//the standard deviation of the
	//number of records per history
	public Double stedvRecordsPerHistory(){
		//sumo todas las stedv y luego entre 5
		return null;
		
	}
	
	
	// The brotherhoods whose history is larger than the average.
	public Collection<Brotherhood> largerBrosthanAvg(){
		return null;
		
	}
	
	

}
