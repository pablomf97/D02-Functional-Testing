package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import repositories.LegalRecordRepository;


import domain.Actor;
import domain.Brotherhood;
import domain.History;

import domain.LegalRecord;


@Service
@Transactional
public class LegalRecordService {

	@Autowired
	private LegalRecordRepository legalRecordRepository;


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

		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		LegalRecord res=new LegalRecord();

		return res;

	}

	public LegalRecord save(LegalRecord legalRecord){
		LegalRecord res;
		LegalRecord copy;
		Brotherhood principal;
		Collection<LegalRecord> legals;
		History historyBro;
		principal = (Brotherhood)this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		Assert.notNull(legalRecord.getTitle(),"not.null");
		Assert.notNull(legalRecord.getDescription(),"not.null");
		Assert.notNull(legalRecord.getLaws(),"not.null");
		Assert.notNull(legalRecord.getVAT(),"not.null");
		Assert.notNull(legalRecord.getName(),"not.null");

		historyBro = principal.getHistory();
		Assert.notNull(historyBro);
		legals=historyBro.getLegalRecords();
		if(legalRecord.getId()!=0){
			copy=this.findOne(legalRecord.getId());
			copy.setTitle(legalRecord.getTitle());
			copy.setDescription(legalRecord.getDescription());
			copy.setLaws(legalRecord.getLaws());
			copy.setName(legalRecord.getName());
			copy.setVAT(legalRecord.getVAT());
			copy.setVersion(legalRecord.getVersion());
			copy.setId(legalRecord.getId());

			res=this.legalRecordRepository.save(copy);
		}
		else{

			res=this.legalRecordRepository.save(legalRecord);
			legals.add(res);

		}		

		Assert.notNull(res);
		return res;
	}

	public void delete(LegalRecord legalRecord){
		Brotherhood principal;
		principal = (Brotherhood)this.actorService.findByPrincipal();
		Collection<LegalRecord> legals;
		History historyBro;

		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		Assert.notNull(legalRecord);
		//Assert.isTrue(legalRecord.getId()==0 , "wrong.id");
		historyBro = principal.getHistory();
		legals=historyBro.getLegalRecords();
		legals.remove(this.findOne(legalRecord.getId()));
		this.legalRecordRepository.delete(this.findOne(legalRecord.getId()));
		Assert.notNull(historyBro);

	}
	public Collection<String> getSplitLaws(final String laws) {
		final Collection<String> res = new ArrayList<>();
		final String[] slice = laws.split(",");

		for (final String p : slice){
			if (p.trim() != ""){
				res.add(p);

			}
		}
		return res;
	}

}
