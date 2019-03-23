package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SystemConfigurationRepository;
import domain.Actor;
import domain.SystemConfiguration;

@Service
@Transactional
public class SystemConfigurationService {

	// Managed Repository

	@Autowired
	private SystemConfigurationRepository systemConfigurationRepository;

	// Supporting Services

	@Autowired
	private ActorService actorService;

	@Autowired
	private Validator validator;

	// Simple CRUD methods

	/* Find one by ID */
	public SystemConfiguration findOne(final int systemConfigurationId) {
		SystemConfiguration res;

		res = this.systemConfigurationRepository.findOne(systemConfigurationId);

		return res;

	}

	/* Find all system configurations */
	public Collection<SystemConfiguration> findAll() {
		Collection<SystemConfiguration> result;

		result = this.systemConfigurationRepository.findAll();

		return result;
	}

	/* Create a system configuration */
	public SystemConfiguration create() {
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "ADMINISTRATOR"),
				"not.allowed");
		Map<String, String> breachNotification = new HashMap<>();
		Map<String, String> wellMap = new HashMap<>();
		wellMap.put("Espa�ol",
				"�Bienvenidos a Acme Madrug�! Tu sitio para organizar procesiones.");
		wellMap.put("English",
				"Welcome to Acme Madrug�, the site to organise your processions.");

		breachNotification.put("Espa�ol", "");
		breachNotification.put("English", "");
		SystemConfiguration systemConfiguration = new SystemConfiguration();
		systemConfiguration.setSystemName("Acme-Madrugá");
		systemConfiguration.setWelcomeMessage(wellMap);
		systemConfiguration.setBreachNotification(breachNotification);
		systemConfiguration
				.setBanner("https://image.ibb.co/iuaDgV/Untitled.png");
		systemConfiguration.setCountryCode("+034");
		systemConfiguration.setTimeResultsCached(1);
		systemConfiguration.setMaxResults(10);
		systemConfiguration
				.setSpamWords("sex,viagra,cialis,one million,you've been selected,nigeria,sexo,un millon,un mill�n,ha sido seleccionado");
		systemConfiguration
				.setPossitiveWords("good,fantastic,excellent,great,amazing,terrific,beautiful,bueno,fantastico,fant�stico,excelente,genial,"
						+ "incre�ble,increible,asombroso,bonito");
		systemConfiguration
				.setNegativeWords("not,bad,horrible,average,disaster,no,malo,mediocre,desastre,desastroso");
		systemConfiguration.setMakers("VISA, MCARD, AMEX, DINNERS, FLY");
		return systemConfiguration;
	}

	/* Saving a system configuration */
	public SystemConfiguration save(SystemConfiguration systemConfiguration) {
		Assert.notNull(systemConfiguration, "null.system.configuration");
		Actor principal;
		SystemConfiguration result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "ADMINISTRATOR"),
				"not.allowed");

		result = this.systemConfigurationRepository.save(systemConfiguration);

		return result;
	}

	/* Delete system configuration */
	public void delete(SystemConfiguration systemConfiguration) {
		Assert.notNull(systemConfiguration, "null.system.configuration");

		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "ADMINISTRATOR"),
				"not.allowed");

		this.systemConfigurationRepository.delete(systemConfiguration);
	}

	// Other business methods

	/* Find system configuration */
	public SystemConfiguration findMySystemConfiguration() {
		final SystemConfiguration result;

		result = this.systemConfigurationRepository.findSystemConf();

		return result;
	}

	/* Find banner */
	public String findMyBanner() {

		String result;

		result = this.findMySystemConfiguration().getBanner();

		return result;
	}

	/* Find welcome message */
	public Map<String, String> findWelcomeMessage() {
		final Map<String, String> result;

		result = this.findMySystemConfiguration().getWelcomeMessage();

		return result;
	}

	/* Find BreachNotification */
	public Map<String, String> findBreachNotification() {
		final Map<String, String> result;
		result = this.findMySystemConfiguration().getBreachNotification();
		return result;
	}

	public SystemConfiguration reconstruct(
			SystemConfiguration systemConfiguration, String nameES,
			String nameEN, String nEs, String nEn, BindingResult binding) {

		SystemConfiguration res = new SystemConfiguration();

		Assert.isTrue(systemConfiguration.getId() == this
				.findMySystemConfiguration().getId());
		Assert.isTrue(this.actorService.checkAuthority(
				this.actorService.findByPrincipal(), "ADMINISTRATOR"));

		if (systemConfiguration.getId() == 0) {
			systemConfiguration
					.setWelcomeMessage(new HashMap<String, String>());
			systemConfiguration
					.setBreachNotification(new HashMap<String, String>());
			systemConfiguration.getWelcomeMessage().put("Espa�ol", nameES);
			systemConfiguration.getWelcomeMessage().put("English", nameEN);
			systemConfiguration.getBreachNotification().put("Espa�ol", nEs);
			systemConfiguration.getBreachNotification().put("English", nEn);
			res = systemConfiguration;
		} else {
			SystemConfiguration bd = this.systemConfigurationRepository
					.findOne(systemConfiguration.getId());

			systemConfiguration
					.setWelcomeMessage(new HashMap<String, String>());

			systemConfiguration.getWelcomeMessage().put("Espa�ol", nameES);
			systemConfiguration.getWelcomeMessage().put("English", nameEN);
			systemConfiguration
					.setBreachNotification(new HashMap<String, String>());
			systemConfiguration.getBreachNotification().put("Espa�ol", nEs);
			systemConfiguration.getBreachNotification().put("English", nEn);

			bd.setWelcomeMessage(systemConfiguration.getWelcomeMessage());
			bd.setBreachNotification(systemConfiguration
					.getBreachNotification());

			res.setSystemName(systemConfiguration.getSystemName());
			res.setBanner(systemConfiguration.getBanner());
			res.setCountryCode(systemConfiguration.getCountryCode());
			res.setTimeResultsCached(systemConfiguration.getTimeResultsCached());
			res.setMaxResults(systemConfiguration.getMaxResults());
			res.setMessagePriority(systemConfiguration.getMessagePriority());
			res.setSpamWords(systemConfiguration.getSpamWords());
			res.setPossitiveWords(systemConfiguration.getPossitiveWords());
			res.setNegativeWords(systemConfiguration.getNegativeWords());
			res.setFare(systemConfiguration.getFare());
			res.setMakers(systemConfiguration.getMakers());
			res.setVAT(systemConfiguration.getVAT());

			this.validator.validate(res, binding);

			res = bd;
		}

		return res;
	}

}