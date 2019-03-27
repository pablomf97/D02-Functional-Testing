
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
	private LegalRecordRepository	legalRecordRepository;

	@Autowired
	private ActorService			actorService;


	public LegalRecord findOne(final int id) {
		LegalRecord res;
		Assert.isTrue(id != 0, "not saved");
		res = this.legalRecordRepository.findOne(id);
		Assert.notNull(res);
		return res;

	}
	public Collection<LegalRecord> findAll() {
		return this.legalRecordRepository.findAll();
	}

	public LegalRecord create() {

		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"), "not.allowed");
		final LegalRecord res = new LegalRecord();

		return res;

	}

	public LegalRecord save(final LegalRecord legalRecord) {
		LegalRecord res;
		Brotherhood principal;
		Collection<LegalRecord> legals;
		History historyBro;
		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"), "not.allowed");
		Assert.notNull(legalRecord.getTitle(), "not.null");
		Assert.notNull(legalRecord.getDescription(), "not.null");
		Assert.notNull(legalRecord.getLaws(), "not.null");
		Assert.notNull(legalRecord.getVAT(), "not.null");
		Assert.notNull(legalRecord.getName(), "not.null");

		historyBro = principal.getHistory();
		Assert.notNull(historyBro);
		legals = historyBro.getLegalRecords();

		res = this.legalRecordRepository.save(legalRecord);
		if (legalRecord.getId() == 0) {
			legals.add(legalRecord);
			historyBro.setLegalRecords(legals);
		}
		Assert.notNull(res);
		return res;
	}

	public void delete(final LegalRecord legalRecord) {
		Brotherhood principal;
		principal = (Brotherhood) this.actorService.findByPrincipal();
		Collection<LegalRecord> legals;
		History historyBro;

		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"), "not.allowed");
		Assert.notNull(legalRecord);
		//Assert.isTrue(legalRecord.getId()==0 , "wrong.id");
		historyBro = principal.getHistory();
		legals = historyBro.getLegalRecords();
		this.legalRecordRepository.delete(legalRecord);
		legals.remove(legalRecord);
		Assert.notNull(historyBro);

	}
	public Collection<String> getSplitLaws(final String laws) {
		final Collection<String> res = new ArrayList<>();
		if (laws != null && !laws.isEmpty()) {
			final String[] slice = laws.split(",");

			for (final String p : slice)
				if (p.trim() != "")
					res.add(p);
		}
		return res;
	}

}
