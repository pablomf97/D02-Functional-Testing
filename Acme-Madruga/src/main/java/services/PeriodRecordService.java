package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;

import repositories.PeriodRecordRepository;


import domain.Actor;

import domain.PeriodRecord;

@Service
@Transactional
public class PeriodRecordService {

	@Autowired
	private PeriodRecordRepository periodRecordRepository;
	
	@Autowired
	private ActorService actorService;
	
	public PeriodRecord findOne(int id){
		PeriodRecord res;
		Assert.isTrue(id!=0,"not saved");
		res=this.periodRecordRepository.findOne(id);
		return res;
		
	}
	
	
	public Collection<PeriodRecord> findAll(){
		return this.periodRecordRepository.findAll();
	}
	

	public PeriodRecord create(){
		PeriodRecord res=new PeriodRecord();
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		
		return res;
		
	}
	
	public PeriodRecord save(PeriodRecord periodRecord){
		PeriodRecord res;
		Actor principal;
		principal = this.actorService.findByPrincipal();
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
		
		res=this.periodRecordRepository.save(periodRecord);
		Assert.notNull(res);
		return res;
	}
	
	
	public void delete(PeriodRecord periodRecord){
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		
		Assert.notNull(periodRecord);
		Assert.isTrue(periodRecord.getId() != 0, "wrong.id");
		
		this.periodRecordRepository.delete(periodRecord);
		Assert.isTrue(periodRecord==null);
	}

	
}
