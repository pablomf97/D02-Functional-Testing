package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PlatformRepository;
import domain.Actor;
import domain.Brotherhood;
import domain.Platform;

@Service
@Transactional
public class PlatformService {

	// Managed repository ------------------------------------

	@Autowired
	private PlatformRepository platformRepository;

	// Supporting services -----------------------------------

	@Autowired
	private ActorService actorService;

	@Autowired
	private Validator validator;

	// Simple CRUD methods -----------------------------------

	public Platform create() {
		Actor principal;
		Platform result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");

		result = new Platform();

		return result;
	}

	public Collection<Platform> findAll() {
		Collection<Platform> result;
		result = this.platformRepository.findAll();

		return result;
	}

	public Platform findOne(final int platformId) {
		Platform result;
		result = this.platformRepository.findOne(platformId);

		return result;
	}

	public Platform save(final Platform platform) {
		Brotherhood brotherhood;
		Actor principal;
		Platform result;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		Assert.isTrue(platform.getBrotherhood().equals(principal),
				"not.allowed");

		Assert.notNull(platform);
		Assert.notNull(platform.getDescription());
		Assert.notNull(platform.getTitle());

		brotherhood = (Brotherhood) principal;

		if (platform.getId() == 0) {
			Assert.notNull(brotherhood.getZone());
		}

		result = this.platformRepository.save(platform);
		Assert.notNull(result);

		return result;
	}

	public void delete(final Platform platform) {
		Actor principal;

		Assert.notNull(platform);
		Assert.isTrue(platform.getId() != 0, "wrong.id");

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "BROTHERHOOD"),
				"not.allowed");
		Assert.isTrue(platform.getBrotherhood().getId() == principal.getId(),
				"not.allowed");

		this.platformRepository.delete(platform.getId());

	}

	// Other business methods -------------------------------

	public Platform reconstruct(Platform platform, BindingResult binding) {
		Platform result;
		Actor principal = this.actorService.findByPrincipal();

		if (platform.getId() == 0) {
			result = platform;
			result.setBrotherhood((Brotherhood) principal);
		} else {
			result = this.findOne(platform.getId());

			Assert.notNull(result);
			Assert.isTrue(result.getBrotherhood().getId() == principal.getId());

			result.setTitle(platform.getTitle());
			result.setDescription(platform.getDescription());
			result.setPictures(platform.getPictures());

		}

		validator.validate(result, binding);

		return result;
	}

	public Collection<Platform> findPlatformsByBrotherhoodId(int brotherhoodId) {
		Collection<Platform> result;

		result = this.platformRepository
				.findPlatformsByBrotherhoodId(brotherhoodId);

		return result;
	}
}
