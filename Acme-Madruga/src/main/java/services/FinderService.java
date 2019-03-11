package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.transaction.Transactional;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.FinderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Finder;
import domain.Member;
import domain.Procession;

@Service
@Transactional
public class FinderService {

	// Managed repository ------------------------------
	@Autowired
	private FinderRepository finderRepository;

	// Supporting services -----------------------
	@Autowired
	private MemberService memberService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private SystemConfigurationService systemConfigurationService;

	// Constructors
	public FinderService() {
		super();
	}

	// /CREATE
	public Finder create() {
		Finder result;

		result = new Finder();
		result.setSearchResults(new ArrayList<Procession>());

		return result;
	}

	// /FINDONE
	public Finder findOne(final int finderId) {
		Finder result;
		Member principal;

		principal = this.memberService.findByPrincipal();
		Assert.notNull(principal, "not.null");

		result = this.finderRepository.findOne(finderId);
		Assert.notNull(result, "not.null");

		return result;
	}

	// //SAVE

	public Finder save(final Finder finder) {
		Finder result;
		Date currentMoment;
		Actor principal;

		currentMoment = new Date(System.currentTimeMillis() - 1);

		if (finder.getId() != 0) {
			try {
				principal = this.actorService.findByPrincipal();
				Assert.isTrue(
						this.actorService.checkAuthority(principal, "MEMBER"),
						"not.allowed");
				finder.setSearchMoment(currentMoment);
			} catch (Throwable oops) {
				principal = this.actorService.findByPrincipal();
				Assert.isTrue(
						this.actorService.checkAuthority(principal, "MEMBER"),
						"not.allowed");
			}

		}

		Assert.notNull(finder, "not.allowed");

		if (finder.getMinimumMoment() != null
				&& finder.getMaximumMoment() != null) {

			Assert.isTrue(
					finder.getMinimumMoment().before(finder.getMaximumMoment()),
					"not.date");

		}

		result = this.finderRepository.save(finder);
		Assert.notNull(result, "not.null");

		return result;

	}

	// FINDALL
	public Collection<Finder> findAll() {
		Collection<Finder> result;
		result = this.finderRepository.findAll();
		Assert.notNull(result, "not.null");

		return result;

	}

	// DELETE
	public void delete(final Finder finder) {
		Member principal;

		Assert.notNull(finder, "not.allowed");
		Assert.isTrue(finder.getId() != 0);

		principal = this.memberService.findByPrincipal();
		Assert.notNull(principal, "not.null");

		Assert.isTrue(principal.getFinder().equals(finder), "not.allowed");

		finder.setSearchResults(null);
		finder.setArea(null);
		finder.setKeyWord(null);
		finder.setMaximumMoment(null);
		finder.setMinimumMoment(null);

		this.finderRepository.save(finder);

	}

	// Other business methods

	// expiración de la busqueda cuando termina tiempo caché
	public void deleteExpiredFinder(Finder finder) {

		Date maxLivedMoment = new Date();
		int timeChachedFind;
		Date currentMoment;
		currentMoment = new Date(System.currentTimeMillis() - 1);

		timeChachedFind = this.systemConfigurationService
				.findMySystemConfiguration().getTimeResultsCached();
		maxLivedMoment = DateUtils.addHours(currentMoment, -timeChachedFind);

		if (finder.getSearchMoment().before(maxLivedMoment)) {
			finder.setSearchResults(null);
			finder.setArea(null);
			finder.setKeyWord(null);
			finder.setMaximumMoment(null);
			finder.setMinimumMoment(null);
			finder.setSearchMoment(null);
			this.finderRepository.save(finder);
		}

	}

	public Collection<Procession> search(Finder finder) {
		UserAccount userAccount;
		String keyWord;
		String area;
		Date maximumDate;
		Date minimumDate;
		Collection<Procession> results = new ArrayList<Procession>();

		int nResults;

		Collection<Procession> resultsPageables = new ArrayList<Procession>();

		nResults = this.systemConfigurationService.findMySystemConfiguration()
				.getMaxResults();

		userAccount = LoginService.getPrincipal();

		final Authority au = new Authority();
		au.setAuthority(Authority.MEMBER);

		Assert.isTrue(userAccount.getAuthorities().contains(au), "not.allowed");

		keyWord = (finder.getKeyWord() == null || finder.getKeyWord().isEmpty()) ? ""
				: finder.getKeyWord();
		area = (finder.getArea() == null || finder.getArea().isEmpty()) ? ""
				: finder.getArea();

		final Date d1 = new GregorianCalendar(2000, Calendar.JANUARY, 1)
				.getTime();
		final Date d2 = new GregorianCalendar(2200, Calendar.JANUARY, 1)
				.getTime();
		minimumDate = finder.getMinimumMoment() == null ? d1 : finder
				.getMinimumMoment();
		maximumDate = finder.getMaximumMoment() == null ? d2 : finder
				.getMaximumMoment();

		if (finder.getMinimumMoment() == null
				&& finder.getMaximumMoment() == null
				&& finder.getKeyWord().isEmpty() && finder.getArea().isEmpty()) {
			results = this.finderRepository.searchAllProcession();
		} else {
			results = this.finderRepository.findByFilter(keyWord, area,
					minimumDate, maximumDate);
		}
		int count=0;
		for(Procession p : results){
			resultsPageables.add(p);
			count++;
			if(count>=nResults){
				break;
			}
		}
		
	
		//resultsPageables.addAll( results.subList(0, nResults));

		finder.setSearchResults(resultsPageables);

		this.save(finder);

		return resultsPageables;

	}

	public Double[] statsFinder() {
		Double[] res;
		res = this.finderRepository.StatsFinder();
		return res;
	}

	public Double ratioFinders() {
		Double emptys = this.FindersEmpty();
		Double all = (double) this.findAll().size();
		Double res;

		res = (emptys / all);

		return res;

	}

	public Double FindersEmpty() {
		Double res;
		res = (double) this.finderRepository.FindersEmpty().size();
		return res;
	}

}
