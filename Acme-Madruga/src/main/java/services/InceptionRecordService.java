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
/*	public String getLargestBrotherhood(){
		Collection<Brotherhood> bros;
		List<Integer> results=new ArrayList<Integer>();
		bros=this.brotherhoodService.findAll();
		Integer maxValue;

		for(Brotherhood b:bros){
			results.add(this.getSizeAll(b));
		}
		maxValue=Collections.max(results);
		return null ;

	}*/
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
		
		Double misc;
		Double legals;
		Double links;
		Double periods;
		Double inception=0.;
		Double res;
		if(this.brotherhoodService.findAll()!=null){
			inception=1.;
		}

		misc=this.inceptionRecordRepository.statsMisc()[2];
		periods=this.inceptionRecordRepository.statsPeriod()[2];
		legals=this.inceptionRecordRepository.statsLegal()[2];
		links=this.inceptionRecordRepository.statsLink()[2];
		
		res=misc+periods+legals+links+inception;


		return res;
		

	}


	//the minimum

	public Double minRecordsPerHistory(){
		Double misc;
		Double legals;
		Double links;
		Double periods;
		Double inception=0.;
		Double res;
		if(this.brotherhoodService.findAll()!=null){
			inception=1.;
		}

		misc=this.inceptionRecordRepository.statsMisc()[1];
		periods=this.inceptionRecordRepository.statsPeriod()[1];
		legals=this.inceptionRecordRepository.statsLegal()[1];
		links=this.inceptionRecordRepository.statsLink()[1];
		
		res=misc+periods+legals+links+inception;


		return res;
		
	}



	//the maximum
	public Double maxRecordsPerHistory(){
		
		Double misc;
		Double legals;
		Double links;
		Double periods;
		Double inception=0.;
		Double res;
		if(this.brotherhoodService.findAll()!=null){
			inception=1.;
		}

		misc=this.inceptionRecordRepository.statsMisc()[0];
		periods=this.inceptionRecordRepository.statsPeriod()[0];
		legals=this.inceptionRecordRepository.statsLegal()[0];
		links=this.inceptionRecordRepository.statsLink()[0];
		
		res=misc+periods+legals+links+inception;


		return res;

	}



	//the standard deviation of the
	//number of records per history
	public Double stedvRecordsPerHistory(){
		Double misc;
		Double legals;
		Double links;
		Double periods;
		Double inception=0.;
		Double res;
		if(this.brotherhoodService.findAll()!=null){
			inception=1.;
		}

		misc=this.inceptionRecordRepository.statsMisc()[3];
		periods=this.inceptionRecordRepository.statsPeriod()[3];
		legals=this.inceptionRecordRepository.statsLegal()[3];
		links=this.inceptionRecordRepository.statsLink()[3];
		
		res=(misc+periods+legals+links+inception)/5;


		return res;

	}


	// The brotherhoods whose history is larger than the average.
	public Collection<Brotherhood> largerBrosthanAvg(){
		Collection<Brotherhood> bros;
		Collection<Brotherhood> results = new ArrayList<Brotherhood>();
		Double records;
		bros=this.brotherhoodService.findAll();
		
		for(Brotherhood b:bros){
			records=1.+b.getHistory().getLegalRecords().size()+
					b.getHistory().getLinkRecords().size()+
					b.getHistory().getMiscellaneousRecords().size()+b.getHistory().getPeriodRecords().size();
			if(records>=avgRecordsPerHistory()){
				results.add(b);
			}
		}
		return results;

	}
	// The brotherhood with the largest history.
	public String getLargestBrotherhood(){

		Collection<Brotherhood> bros;
		Collection<Double> results = new ArrayList<Double>();
		Double records;
		String bro = "";
		bros=this.brotherhoodService.findAll();
		
		for(Brotherhood b:bros){
			records=1.+b.getHistory().getLegalRecords().size()+
					b.getHistory().getLinkRecords().size()+
					b.getHistory().getMiscellaneousRecords().size()+b.getHistory().getPeriodRecords().size();
			results.add(records);
			if(records==Collections.max(results)){
				bro=b.getUserAccount().getUsername();
			}
		}
		return bro;

	}




}
