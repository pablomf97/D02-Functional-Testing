package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;

import domain.Actor;
import domain.InceptionRecord;


import repositories.InceptionRecordRepository;

@Service
@Transactional
public class InceptionRecordService {
	
	@Autowired
	private InceptionRecordRepository inceptionRecordRepository;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private ActorService actorService;
	
	
	public InceptionRecord findOne(int id){
		InceptionRecord res;
		Assert.isTrue(id!=0,"not saved");
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
	
	
	
}
