package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PeriodRecordRepository;
import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;

@Transactional
@Service
public class PeriodRecordService {

	@Autowired
	private PeriodRecordRepository periodRecordRepository;

	@Autowired
	private ActorService actorService;

	public PeriodRecord findOne(final int id) {
		PeriodRecord res;
		res = this.periodRecordRepository.findOne(id);
		return res;

	}

	public Collection<PeriodRecord> findAll() {
		return this.periodRecordRepository.findAll();
	}

	public PeriodRecord create() {

		Brotherhood principal;
		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		final PeriodRecord res = new PeriodRecord();
		return res;

	}

	public PeriodRecord save(final PeriodRecord periodRecord) {
		PeriodRecord res;
		Brotherhood principal;
		History historyBro;
		// Collection<PeriodRecord> periodRecords;
		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		Assert.notNull(periodRecord.getTitle(), "not.null");
		Assert.notNull(periodRecord.getDescription(), "not.null");
		Assert.notNull(periodRecord.getStartYear(), "not.null");
		Assert.notNull(periodRecord.getEndYear(), "not.null");
		Assert.notNull(periodRecord.getPhotos(), "not.null");
		this.checkYear(periodRecord);
		historyBro = principal.getHistory();
		Assert.notNull(historyBro);
		// periodRecords=historyBro.getPeriodRecords();
		// periodRecord.setTitle(periodRecord.getTitle());
		// periodRecord.setDescription(periodRecord.getDescription());
		// periodRecord.setStartYear(periodRecord.getStartYear());
		// periodRecord.setEndYear(periodRecord.getEndYear());
		// periodRecord.setPhotos(periodRecord.getPhotos());
		res = this.periodRecordRepository.save(periodRecord);
		if (periodRecord.getId() == 0)
			historyBro.getPeriodRecords().add(periodRecord);
		Assert.notNull(res);
		return res;
	}

	public void checkYear(final PeriodRecord periodRecord) {
		Assert.isTrue(
				periodRecord.getStartYear() <= (periodRecord.getEndYear()),
				"not.date");
		Assert.isTrue(
				periodRecord.getStartYear() > 1500
						&& (periodRecord.getEndYear() < 2100), "not.date");
	}

	public void delete(final PeriodRecord periodRecord) {
		Brotherhood principal;
		History historyBro;
		Collection<PeriodRecord> periods;
		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");

		Assert.notNull(periodRecord);
		historyBro = principal.getHistory();
		Assert.isTrue(historyBro.getPeriodRecords().contains(periodRecord));

		periods = historyBro.getPeriodRecords();

		this.periodRecordRepository.delete(periodRecord);
		periods.remove(periodRecord);
		Assert.notNull(historyBro);

	}

	public Collection<String> getSplitPictures(final String pictures) {
		final Collection<String> res = new ArrayList<>();
		if (pictures != null && !pictures.isEmpty()) {
			final String[] slice = pictures.split(",");

			for (final String p : slice)
				if (p.trim() != "")
					res.add(p);
		}

		return res;

	}

}