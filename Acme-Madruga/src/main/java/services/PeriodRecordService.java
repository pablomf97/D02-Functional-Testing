package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import repositories.PeriodRecordRepository;

import domain.Brotherhood;
import domain.History;

import domain.PeriodRecord;


@Transactional
@Service
public class PeriodRecordService {

	@Autowired
	private PeriodRecordRepository periodRecordRepository;

	@Autowired
	private ActorService actorService;



	public PeriodRecord findOne(int id){
		PeriodRecord res;
		res=this.periodRecordRepository.findOne(id);
		return res;

	}


	public Collection<PeriodRecord> findAll(){
		return this.periodRecordRepository.findAll();
	}


	public PeriodRecord create(){

		Brotherhood principal;
		principal = (Brotherhood)this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		PeriodRecord res=new PeriodRecord();
		return res;

	}

	public PeriodRecord save(PeriodRecord periodRecord){
		PeriodRecord res;
		Brotherhood principal;
		History historyBro;
		Collection<PeriodRecord> periodRecords;
		principal = (Brotherhood)this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		Assert.notNull(periodRecord.getTitle(),"not.null");
		Assert.notNull(periodRecord.getDescription(),"not.null");
		Assert.notNull(periodRecord.getStartYear(),"not.null");
		Assert.notNull(periodRecord.getEndYear(),"not.null");
		Assert.notNull(periodRecord.getPhotos(),"not.null");
		Assert.isTrue(
				periodRecord.getStartYear()<=(periodRecord.getEndYear()),
				"not.date");
		Assert.isTrue(
				periodRecord.getStartYear()>1500||(periodRecord.getEndYear()>2100),
				"not.date");
		historyBro = principal.getHistory();
		Assert.notNull(historyBro);
		periodRecords=historyBro.getPeriodRecords();

		res=this.periodRecordRepository.save(periodRecord);
		if(periodRecord.getId() == 0){
			periodRecords.add(periodRecord);
			historyBro.setPeriodRecords(periodRecords);
		}
		Assert.notNull(res);
		return res;
	}


	public void delete(PeriodRecord periodRecord){
		Brotherhood principal;
		History historyBro;
		Collection<PeriodRecord>periods;
		principal = (Brotherhood )this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");

		Assert.notNull(periodRecord);
		historyBro = principal.getHistory();
		Assert.isTrue(historyBro.getPeriodRecords().contains(periodRecord));

		periods=historyBro.getPeriodRecords();

		this.periodRecordRepository.delete(periodRecord);
		periods.remove(periodRecord);
		Assert.notNull(historyBro);
		Assert.isTrue(periodRecord==null);
	}


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

}
