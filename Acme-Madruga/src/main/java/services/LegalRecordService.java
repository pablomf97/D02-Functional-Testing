package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;

import repositories.LegalRecordRepository;


import domain.Actor;

import domain.LegalRecord;


@Service
@Transactional
public class LegalRecordService {

	@Autowired
	private LegalRecordRepository legalRecordRepository;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private ActorService actorService;
	
	
	
	public LegalRecord findOne(int id){
		LegalRecord res;
		Assert.isTrue(id!=0,"not saved");
		res=this.legalRecordRepository.findOne(id);
		return res;
		
	}
	public Collection<LegalRecord> findAll(){
		return this.legalRecordRepository.findAll();
	}
	
	

	public LegalRecord create(){
		LegalRecord res=new LegalRecord();
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		
		return res;
		
	}
	
	public LegalRecord save(LegalRecord legalRecord){
		LegalRecord res;
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		Assert.notNull(legalRecord.getTitle(),"not.null");
		Assert.notNull(legalRecord.getDescription(),"not.null");
		Assert.notNull(legalRecord.getLaws(),"not.null");
		Assert.notNull(legalRecord.getVAT(),"not.null");
		Assert.notNull(legalRecord.getName(),"not.null");
		res=this.legalRecordRepository.save(legalRecord);
		Assert.notNull(res);
		return res;
	}
	 
	public void delete(LegalRecord legalRecord){
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		Assert.notNull(legalRecord);
		Assert.isTrue(legalRecord.getId() != 0, "wrong.id");
		
		this.legalRecordRepository.delete(legalRecord);
		Assert.isTrue(legalRecord==null);
	}

	
}
