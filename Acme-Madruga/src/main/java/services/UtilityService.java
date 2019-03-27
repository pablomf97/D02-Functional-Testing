
package services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Actor;
import domain.Message;
import domain.Parade;

@Service
@Transactional
public class UtilityService {

	// Supporting Services ------------------------------------

	@Autowired
	private ParadeService				paradeService;

	@Autowired
	private MessageService				messageService;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// Utility methods ----------------------------------------

	public String generateTicker() {
		String uniqueTicker = null;
		Calendar date;
		String year, month, day, alphaNum, todayDate;
		boolean unique = false;

		date = Calendar.getInstance();
		date.setTime(LocalDate.now().toDate());
		year = String.valueOf(date.get(Calendar.YEAR));
		year = year.substring(year.length() - 2, year.length());
		month = String.valueOf(date.get(Calendar.MONTH) + 1);
		if (month.length() == 1)
			month = "0" + month;
		day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1)
			day = "0" + day;

		while (unique == false) {
			alphaNum = this.randomString();
			todayDate = year + month + day;
			uniqueTicker = todayDate + "-" + alphaNum;
			for (final Parade parade : this.paradeService.findAll())
				if (parade.getTicker().equals(uniqueTicker))
					continue;
			unique = true;
		}
		return uniqueTicker;
	}

	public String randomString() {

		final String possibleChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final SecureRandom rnd = new SecureRandom();
		final int length = 5;

		final StringBuilder stringBuilder = new StringBuilder(length);

		for (int i = 0; i < length; i++)
			stringBuilder.append(possibleChars.charAt(rnd.nextInt(possibleChars.length())));
		return stringBuilder.toString();

	}

	public Integer checkSpammers() {
		final Collection<Actor> allActors = this.actorService.findAll();
		Integer numMessages = 0;
		Integer spammers = 0;

		for (final Actor actor : allActors) {
			numMessages = this.messageService.findNumberMessagesByActorId(actor.getId());
			if ((this.messageService.findNumberMessagesSpamByActorId(actor.getId()) / ((numMessages != 0) ? numMessages : 1) >= 0.1)) {
				actor.setSpammer(true);
				spammers++;
			} else
				actor.setSpammer(false);
		}
		return spammers;
	}

	public List<String> getNegativeWords() {

		final String neg = this.systemConfigurationService.findMySystemConfiguration().getNegativeWords();
		final List<String> listNegWords = new ArrayList<String>(Arrays.asList(neg.split(",")));

		return listNegWords;

	}

	public List<String> getPossitiveWords() {

		final String pos = this.systemConfigurationService.findMySystemConfiguration().getPossitiveWords();
		final List<String> listPosWords = new ArrayList<String>(Arrays.asList(pos.split(",")));

		return listPosWords;

	}

	public Double getNumberNegativeWords(final String s, final List<String> negativeWords) {

		Double res = 0.;

		final String[] words = s.split(" ");

		for (final String a : words)
			if (negativeWords.contains(a))
				res = res + 1.0;

		return res;
	}

	public Double getNumberPossitiveWords(final String s, final List<String> possitiveWords) {

		Double res = 0.;

		final String[] words = s.split(" ");

		for (final String a : words)
			if (possitiveWords.contains(a))
				res = res + 1.0;

		return res;
	}

	public boolean isSpam(final List<String> atributosAComprobar) {
		boolean containsSpam = false;
		final String[] spamWords = this.systemConfigurationService.findMySystemConfiguration().getSpamWords().split(",");
		for (int i = 0; i < atributosAComprobar.size(); i++)
			if (containsSpam == false) {
				for (final String spamWord : spamWords)
					if (atributosAComprobar.get(i).toLowerCase().contains(spamWord.toLowerCase())) {
						containsSpam = true;
						break;
					}
			} else
				break;
		return containsSpam;
	}

	public void computeScore() {
		final Collection<Actor> allActors = this.actorService.findAll();
		final List<String> possitiveWords = this.getPossitiveWords();
		final List<String> negativeWords = this.getNegativeWords();

		for (final Actor actor : allActors) {
			Double possitive = 0.;
			Double negative = 0.;
			final Collection<Message> mes = this.messageService.findMessagesByActorId(actor.getId());
			for (final Message message : mes) {
				final String toCheck = message.getSubject() + message.getBody();
				possitive += this.getNumberPossitiveWords(toCheck, possitiveWords);
				negative += this.getNumberNegativeWords(toCheck, negativeWords);
			}
			final Double pm = possitive - negative;
			if (pm != 0.0) {
				final Double res = pm / (possitive + negative);
				actor.setScore(res);
			} else
				actor.setScore(0.);

		}
	}

}
