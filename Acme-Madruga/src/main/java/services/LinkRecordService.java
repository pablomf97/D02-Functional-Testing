package services;


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

	public LinkRecord save(final LinkRecord recordParam){
		Brotherhood principal;
		LinkRecord recordBD = new LinkRecord();
		History historyBro;
		LinkRecord copy=null;

		principal = (Brotherhood) this.actorService.findByPrincipal();

		historyBro = principal.getHistory();

		if(recordParam.getId() != 0){

			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
			Assert.isTrue(historyBro.getLinkRecords().contains(recordParam));

			recordBD = this.linkRecordRepository.findOne(recordParam.getId());

			recordBD.setTitle(recordParam.getTitle());
			recordBD.setDescription(recordParam.getDescription());
			recordBD.setLinkedBrotherhood(recordParam.getLinkedBrotherhood());

			copy=this.linkRecordRepository.save(recordBD);

		}else{
			Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));

			recordBD.setTitle(recordParam.getTitle());
			recordBD.setDescription(recordParam.getDescription());
			recordBD.setLinkedBrotherhood(recordParam.getLinkedBrotherhood());
			try{

				copy=this.linkRecordRepository.save(recordBD);
			}catch(Throwable oops){

			}


			historyBro.getLinkRecords().add(copy);
		}

		return copy;
	}


	public void delete(final LinkRecord link){
		Brotherhood principal;
		History historyBro;
		LinkRecord recordBD;

		Assert.isTrue(link.getId() != 0);

		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));

		recordBD = this.linkRecordRepository.findOne(link.getId());

		historyBro = principal.getHistory();

		Assert.isTrue(historyBro.getLinkRecords().contains(recordBD));

		historyBro.getLinkRecords().remove(recordBD);

		this.linkRecordRepository.delete(recordBD);

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

		/*if(linkRecord.getId() == 0){
			result.setTitle(linkRecord.getTitle());
			result.setDescription(linkRecord.getDescription());
			result.setLinkedBrotherhood(linkRecord.getLinkedBrotherhood());

		}*/if(linkRecord.getId() != 0){
			final LinkRecord record = this.linkRecordRepository.findOne(linkRecord.getId());

			record.setDescription(linkRecord.getDescription());
			record.setTitle(linkRecord.getTitle());
			//record.setLinkedBrotherhood(linkRecord.getLinkedBrotherhood());

			result = record;



		}else{
			result=linkRecord;
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
