package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import repositories.InceptionRecordRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.History;
import domain.InceptionRecord;

@Service
@Transactional
public class InceptionRecordService {

	@Autowired
	private InceptionRecordRepository inceptionRecordRepository;

	@Autowired
	private ActorService actorService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	public InceptionRecord findOne(final int id) {
		InceptionRecord res;

		res = this.inceptionRecordRepository.findOne(id);
		return res;

	}

	public Collection<InceptionRecord> findAll() {

		return this.inceptionRecordRepository.findAll();

	}

	public InceptionRecord create() {
		final InceptionRecord res = new InceptionRecord();
		final Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		return res;

	}

	public InceptionRecord save(final InceptionRecord inceptionRecord) {
		InceptionRecord res;
		InceptionRecord copy;
		Brotherhood principal;
		Assert.notNull(inceptionRecord.getTitle(), "not.null");
		Assert.notNull(inceptionRecord.getDescription(), "not.null");
		Assert.notNull(inceptionRecord.getPhotos(), "not.null");
		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		if (inceptionRecord.getId() == 0)
			Assert.isTrue(principal.getHistory().getInceptionRecord() == null);
		else
			Assert.isTrue(principal.getHistory().getInceptionRecord()
					.equals(inceptionRecord));

		final History hist = principal.getHistory();
		if (inceptionRecord.getId() != 0) {
			copy = this.findOne(inceptionRecord.getId());
			copy.setTitle(inceptionRecord.getTitle());
			copy.setDescription(inceptionRecord.getDescription());
			copy.setPhotos(inceptionRecord.getPhotos());
			res = this.inceptionRecordRepository.save(copy);
			hist.setInceptionRecord(res);
		} else {
			res = this.inceptionRecordRepository.save(inceptionRecord);
			hist.setInceptionRecord(res);

		}
		Assert.notNull(res);
		return res;
	}

	public void delete(final InceptionRecord inceptionRecord) {
		Brotherhood principal;
		History historyBro;
		principal = (Brotherhood) this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		// comprobar que pertenece a ese brotherhood el record

		historyBro = principal.getHistory();
		Assert.isTrue(historyBro.getInceptionRecord().getId() == inceptionRecord
				.getId());
		Assert.notNull(inceptionRecord);
		// Assert.isTrue(inceptionRecord.getId() != 0, "wrong.id");

		this.inceptionRecordRepository.delete(this.findOne(inceptionRecord
				.getId()));
		historyBro.setInceptionRecord(null);

	}

	// ancillary methods

	public Collection<String> getSplitPictures(final String pictures)
	// throws MalformedURLException, URISyntaxException
	{
		final Collection<String> res = new ArrayList<>();

		if (pictures != null && !pictures.isEmpty()) {

			final String[] slice = pictures.split(",");
			for (final String p : slice)
				// brotherhoodService.checkUrl(p);
				if (p.trim() != "") {
					Assert.isTrue(ResourceUtils.isUrl(p), "error.url");
					res.add(p);
				}
		}
		return res;
	}

	// number of records of a bro
	public Integer getSizeAll(final Brotherhood b) {
		Integer res;
		Integer inceptions = 0;
		if (b.getHistory().getInceptionRecord() != null)
			inceptions = 1;
		final Integer legals = b.getHistory().getLegalRecords().size();
		final Integer miscs = b.getHistory().getMiscellaneousRecords().size();
		final Integer periods = b.getHistory().getPeriodRecords().size();
		final Integer links = b.getHistory().getLinkRecords().size();

		res = inceptions + legals + miscs + periods + links;

		return res;
	}

	// The average

	public Double avgRecordsPerHistory() {

		Double misc;
		Double legals;
		Double links;
		Double periods;
		Double inception = 0.;
		Double res;
		final Collection<Brotherhood> bros = this.brotherhoodService.findAll();

		for (final Brotherhood b : bros)
			if (b.getHistory().getInceptionRecord() != null) {
				inception = 1.;
				break;
			}
		misc = this.inceptionRecordRepository.statsMisc()[2];
		periods = this.inceptionRecordRepository.statsPeriod()[2];
		legals = this.inceptionRecordRepository.statsLegal()[2];
		links = this.inceptionRecordRepository.statsLink()[2];

		res = misc + periods + legals + links + inception;

		return res;

	}

	// the minimum

	public Double minRecordsPerHistory() {
		Collection<Brotherhood> bros;
		final Collection<Double> results = new ArrayList<Double>();
		Double records;

		Double inception = 0.;
		bros = this.brotherhoodService.findAll();

		for (final Brotherhood b : bros) {
			if (b.getHistory().getInceptionRecord() != null)
				inception = 1.;
			records = inception + b.getHistory().getLegalRecords().size()
					+ b.getHistory().getLinkRecords().size()
					+ b.getHistory().getMiscellaneousRecords().size()
					+ b.getHistory().getPeriodRecords().size();
			results.add(records);

		}
		return Collections.min(results);

	}

	// the maximum
	public Double maxRecordsPerHistory() {

		Collection<Brotherhood> bros;
		final Collection<Double> results = new ArrayList<Double>();
		Double records;

		Double inception = 0.;
		bros = this.brotherhoodService.findAll();

		for (final Brotherhood b : bros) {
			if (b.getHistory().getInceptionRecord() != null)
				inception = 1.;
			records = inception + b.getHistory().getLegalRecords().size()
					+ b.getHistory().getLinkRecords().size()
					+ b.getHistory().getMiscellaneousRecords().size()
					+ b.getHistory().getPeriodRecords().size();
			results.add(records);

		}
		return Collections.max(results);

	}

	// the standard deviation of the
	// number of records per history
	public Double stedvRecordsPerHistory() {
		Double misc;
		Double legals;
		Double links;
		Double periods;
		Double inception = 0.;
		Double res;
		final Collection<Brotherhood> bros = this.brotherhoodService.findAll();

		for (final Brotherhood b : bros)
			if (b.getHistory().getInceptionRecord() != null) {
				inception = 1.;
				break;
			}

		misc = this.inceptionRecordRepository.statsMisc()[3];
		periods = this.inceptionRecordRepository.statsPeriod()[3];
		legals = this.inceptionRecordRepository.statsLegal()[3];
		links = this.inceptionRecordRepository.statsLink()[3];

		res = (misc + periods + legals + links + inception) / 5;

		return res;

	}

	// The brotherhoods whose history is larger than the average.
	public Collection<Brotherhood> largerBrosthanAvg() {
		Collection<Brotherhood> bros;
		final Collection<Brotherhood> results = new ArrayList<Brotherhood>();
		Double records;
		Double inception = 0.;
		bros = this.brotherhoodService.findAll();

		for (final Brotherhood b : bros) {
			if (b.getHistory().getInceptionRecord() != null)
				inception = 1.;
			records = inception + b.getHistory().getLegalRecords().size()
					+ b.getHistory().getLinkRecords().size()
					+ b.getHistory().getMiscellaneousRecords().size()
					+ b.getHistory().getPeriodRecords().size();
			if (records > this.avgRecordsPerHistory())
				results.add(b);
		}
		return results;

	}

	// The brotherhood with the largest history.
	public String getLargestBrotherhood() {

		Collection<Brotherhood> bros;
		final Collection<Double> results = new ArrayList<Double>();
		Double records;
		String bro = "";
		Double inception = 0.;
		bros = this.brotherhoodService.findAll();

		for (final Brotherhood b : bros) {
			if (b.getHistory().getInceptionRecord() != null)
				inception = 1.;
			records = inception + b.getHistory().getLegalRecords().size()
					+ b.getHistory().getLinkRecords().size()
					+ b.getHistory().getMiscellaneousRecords().size()
					+ b.getHistory().getPeriodRecords().size();
			results.add(records);
			if (records == Collections.max(results))
				bro = b.getUserAccount().getUsername();
		}
		return bro;

	}

	public void flush() {
		this.inceptionRecordRepository.flush();

	}

}
