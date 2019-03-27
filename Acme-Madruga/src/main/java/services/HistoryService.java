
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.HistoryRepository;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@Transactional
@Service
public class HistoryService {

	@Autowired
	private HistoryRepository		historyRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private InceptionRecordService	inceptionService;


	//CRUD Methods ---------------------------------------------------------

	public History create() {
		History result;
		final Brotherhood principal;
		final Collection<PeriodRecord> periodRecords = new ArrayList<PeriodRecord>();
		final Collection<LegalRecord> legalRecords = new ArrayList<LegalRecord>();
		final Collection<LinkRecord> linkRecords = new ArrayList<LinkRecord>();
		final Collection<MiscellaneousRecord> miscellaneousRecords = new ArrayList<MiscellaneousRecord>();

		//principal = (Brotherhood) this.actorService.findByPrincipal();
		//Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));

		result = new History();

		result.setLegalRecords(legalRecords);
		result.setPeriodRecords(periodRecords);
		result.setLinkRecords(linkRecords);
		result.setMiscellaneousRecords(miscellaneousRecords);

		return result;

	}

	public History save(final History history) {
		History saved;

		//No se puede guardar un history sin inception record

		saved = this.historyRepository.save(history);

		return saved;

	}
	//	
	//	public void delete(final History history){
	//		Brotherhood principal;
	//		
	//		principal = (Brotherhood) this.actorService.findByPrincipal();
	//		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
	//		
	//		Assert.isTrue(history.getId() != 0);
	//		
	//		//Comprobar que el history a eliminar es el de la brotherhood logeada
	//		Assert.isTrue(principal.getHistory().getId() == history.getId());
	//		
	//		this.historyRepository.delete(history);
	//		
	//	}
	//	
	public History findOne(final int historyId) {
		History result;

		result = this.historyRepository.findOne(historyId);

		return result;

	}

	public Collection<History> findAll() {
		Collection<History> result;

		result = this.historyRepository.findAll();

		return result;
	}

}
