package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.LinkRecordRepository;
import domain.Brotherhood;
import domain.History;
import domain.LinkRecord;

@Transactional
@Service
public class LinkRecordService {

	@Autowired
	private LinkRecordRepository linkRecordRepository;
	
	@Autowired
	private ActorService actorService;
	
	//CRUD Methods-------------------------------------------------------------------------
	
	public LinkRecord create(){
		Brotherhood principal;
		LinkRecord result;
		
		principal= (Brotherhood) this.actorService.findByPrincipal();
		
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
		
		result = new LinkRecord();
		
		return result;
		
	}
	
	public LinkRecord save(final LinkRecord link){
		LinkRecord saved;
		Brotherhood principal;
		
		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
		
		Assert.notNull(principal.getHistory());
		
		
		saved = this.linkRecordRepository.save(link);
		
		if(link.getId() == 0){
			
			principal.getHistory().getLinkRecords().add(saved);
			
		}
		
		return saved;
		
	}
	
	public void delete(final LinkRecord link){
		Brotherhood principal;
		History historyBro;
		
		Assert.isTrue(link.getId() != 0);
		
		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
		
		historyBro = principal.getHistory();
		
		Assert.isTrue(historyBro.getLinkRecords().contains(link));
		
		historyBro.getLinkRecords().remove(link);
		
		this.linkRecordRepository.delete(link);
		
	}
	
	public LinkRecord findOne(int linkId){
		LinkRecord result;
		
		result = this.linkRecordRepository.findOne(linkId);
		
		return result;
	}
	
	public Collection<LinkRecord> findAll(){
		Collection<LinkRecord> result;
		
		result = this.linkRecordRepository.findAll();
		
		return result;
	}
	
}
