package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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

	@Autowired
	private Validator validator;
	
	@Autowired
	private BrotherhoodService brotherhoodService;

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

	public LinkRecord reconstruct(final LinkRecord linkRecord, final BindingResult binding){
		LinkRecord result = new LinkRecord();

		if(linkRecord.getId() == 0){
			result.setTitle(linkRecord.getTitle());
			result.setDescription(linkRecord.getDescription());
			result.setLinkedBrotherhood(linkRecord.getLinkedBrotherhood());

		}else{
			final LinkRecord record = this.linkRecordRepository.findOne(linkRecord.getId());

			linkRecord.setLinkedBrotherhood(record.getLinkedBrotherhood());
			
			result = linkRecord;

			

		}
		
		this.validator.validate(result,binding);
		
		return result;


	}
	
	public Collection<Brotherhood> getFreeBrotherhoods(){
		Collection<Brotherhood> linkedBrotherhoods;
		Collection<Brotherhood> brotherhoods;
		Brotherhood prinicipal = (Brotherhood) this.actorService.findByPrincipal();
		
		brotherhoods = this.brotherhoodService.findAll();
		linkedBrotherhoods = this.linkRecordRepository.getLinkedBrotherhoods();
		
		brotherhoods.removeAll(linkedBrotherhoods);
		brotherhoods.remove(prinicipal);
		
		return brotherhoods;
	}

}
