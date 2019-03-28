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

	// CRUD Methods
	// --------------------------------------------------------------

	public MiscellaneousRecord create() {
		MiscellaneousRecord result;
		Brotherhood principal;

		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService
				.checkAuthority(principal, "BROTHERHOOD"));

		result = new MiscellaneousRecord();

		return result;
	}

	public MiscellaneousRecord save(final MiscellaneousRecord recordParam) {
		Brotherhood principal;
		MiscellaneousRecord recordBD = new MiscellaneousRecord();
		History historyBro;

		principal = (Brotherhood) this.actorService.findByPrincipal();

		historyBro = principal.getHistory();

		if (recordParam.getId() != 0) {

			Assert.isTrue(this.actorService.checkAuthority(principal,
					"BROTHERHOOD"));
			Assert.isTrue(historyBro.getMiscellaneousRecords().contains(
					recordParam));

			recordBD = this.miscellaneousRecordRepository.findOne(recordParam
					.getId());

			recordBD.setTitle(recordParam.getTitle());
			recordBD.setDescription(recordParam.getDescription());

			this.miscellaneousRecordRepository.save(recordBD);

		} else {
			Assert.isTrue(this.actorService.checkAuthority(principal,
					"BROTHERHOOD"));

			recordBD.setTitle(recordParam.getTitle());
			recordBD.setDescription(recordParam.getDescription());

			this.miscellaneousRecordRepository.save(recordBD);

			historyBro.getMiscellaneousRecords().add(recordBD);
		}

		return recordBD;
	}

	public void delete(final MiscellaneousRecord recordParam) {
		Brotherhood principal;
		History historyBro;
		MiscellaneousRecord recordBD = new MiscellaneousRecord();

		Assert.isTrue(recordParam.getId() != 0);

		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService
				.checkAuthority(principal, "BROTHERHOOD"));

		recordBD = this.miscellaneousRecordRepository.findOne(recordParam
				.getId());

		historyBro = principal.getHistory();
		Assert.notNull(historyBro);
		Assert.isTrue(historyBro.getMiscellaneousRecords().contains(recordBD));

		historyBro.getMiscellaneousRecords().remove(recordBD);

		this.miscellaneousRecordRepository.delete(recordBD);

	}

	public MiscellaneousRecord findOne(final int recordId) {
		MiscellaneousRecord result;

		result = this.miscellaneousRecordRepository.findOne(recordId);
		Assert.notNull(result);

		return result;
	}

	public Collection<MiscellaneousRecord> findAll() {
		Collection<MiscellaneousRecord> result;

		result = this.miscellaneousRecordRepository.findAll();

		return result;
	}

	public void flush() {
		this.miscellaneousRecordRepository.flush();

	}
}
