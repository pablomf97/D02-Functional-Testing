package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MiscellaneousRecordRepository;
import domain.Brotherhood;
import domain.History;
import domain.MiscellaneousRecord;

@Transactional
@Service
public class MiscellaneousRecordService {

	@Autowired
	private MiscellaneousRecordRepository miscellaneousRecordRepository;
	
	@Autowired
	private ActorService actorService;
	
	//CRUD Methods --------------------------------------------------------------
	
	public MiscellaneousRecord create(){
		MiscellaneousRecord result;
		Brotherhood principal;
		
		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
		
		result = new MiscellaneousRecord();
		
		return result;
	}
	
	public MiscellaneousRecord save(final MiscellaneousRecord record){
		MiscellaneousRecord saved;
		Brotherhood principal;
		History historyBro;
		
		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
		
		historyBro = principal.getHistory();
		Assert.notNull(historyBro);
		
		saved = this.miscellaneousRecordRepository.save(record);
		
		if(record.getId() == 0){
			historyBro.getMiscellaneousRecords().add(saved);
		}
		
		return saved;
	}
	
	public void delete(final MiscellaneousRecord record){
		Brotherhood principal;
		History historyBro;
		
		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal,"BROTHERHOOD"));
		
		historyBro = principal.getHistory();
		Assert.notNull(historyBro);
		Assert.isTrue(historyBro.getMiscellaneousRecords().contains(record));
		
		historyBro.getMiscellaneousRecords().remove(record);
		
		this.miscellaneousRecordRepository.delete(record);
		
		
	}
	
	public MiscellaneousRecord findOne(int recordId){
		MiscellaneousRecord result;
		
		result = this.miscellaneousRecordRepository.findOne(recordId);
		
		return result;
	}
	
	public Collection<MiscellaneousRecord> findAll(){
		Collection<MiscellaneousRecord> result;
		
		result = this.miscellaneousRecordRepository.findAll();
		
		return result;
	}
}
