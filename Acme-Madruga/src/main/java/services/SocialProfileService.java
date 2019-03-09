package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SocialProfileRepository;
import domain.Actor;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	// Managed repository ------------------------------
	@Autowired
	private SocialProfileRepository socialProfileRepository;

	// Supporting services -----------------------
	@Autowired
	private ActorService actorService;

	// Constructors

	public SocialProfileService() {
		super();
	}

	// /CREATE
	public SocialProfile create() {
		SocialProfile result;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = new SocialProfile();
		result.setActor(principal);

		return result;
	}

	// /FINDONE
	public SocialProfile findOne(final int socialProfileId) {
		SocialProfile result;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = this.socialProfileRepository.findOne(socialProfileId);
		Assert.notNull(result);

		return result;
	}

	// //SAVE

	public SocialProfile save(final SocialProfile socialProfile) {
		SocialProfile result;
		Actor principal;

		Assert.notNull(socialProfile);

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		if (socialProfile.getId() != 0) {
			Assert.isTrue(
					checkifPrincipalIsOwnerBySocialProfileId(socialProfile
							.getId()), "not.allowed");
		}

		Assert.notNull(socialProfile.getName(), "socialProfile.NotEmpty");
		Assert.notNull(socialProfile.getNick(), "socialProfile.NotEmpty");
		Assert.notNull(socialProfile.getLinkProfile(), "socialProfile.NotEmpty");
		socialProfile.setActor(principal);
		result = this.socialProfileRepository.save(socialProfile);
		Assert.notNull(result);

		return result;

	}

	// FINDALL
	public Collection<SocialProfile> findAll() {
		Collection<SocialProfile> result;

		result = this.socialProfileRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	// DELETE

	public void delete(final SocialProfile socialProfile) {
		Actor principal;

		Assert.notNull(socialProfile);
		Assert.isTrue(socialProfile.getId() != 0);
		Assert.isTrue(
				checkifPrincipalIsOwnerBySocialProfileId(socialProfile.getId()),
				"not.allowed");
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		this.socialProfileRepository.delete(socialProfile);

	}

	// // Other business methods

	public Boolean checkifPrincipalIsOwnerBySocialProfileId(int socialProfileID) {
		Actor principal;
		Boolean res = false;

		principal = this.actorService.findByPrincipal();

		for (SocialProfile sp : this.findAll()) {
			if (socialProfileID == sp.getId()
					&& principal.getId() == sp.getActor().getId()) {
				res = true;
			}
		}

		return res;
	}

	public Collection<SocialProfile> socialProfilesbyActor() {
		Collection<SocialProfile> cols;
		Collection<SocialProfile> res = new ArrayList<SocialProfile>();
		Actor principal;

		principal = this.actorService.findByPrincipal();

		cols = this.findAll();
		System.out.println("asd:" + cols);
		for (SocialProfile sp : cols) {
			if (principal.getId() == sp.getActor().getId()) {
				res.add(sp);
			}
		}
		return res;
	}
	
	public Collection<SocialProfile> socialProfilesByUserName(String username){
		
		Collection<SocialProfile> res;
		res=this.socialProfileRepository.socialProfilesByUserName(username);
		return res;
		
	}
	
	public Collection<SocialProfile> socialProfilesByUserId(int userId){
		
		Collection<SocialProfile> res;
		res=this.socialProfileRepository.socialProfilesByUserId(userId);

		return res;

	}

	public Collection<SocialProfile> socialProfilesById(int actorId) {
		Collection<SocialProfile> res = new ArrayList<>();
		Collection<SocialProfile> all;
		Actor principal = this.actorService.findByPrincipal();

		all = this.findAll();
		for (SocialProfile socialProfile : all) {
			if (socialProfile.getActor().equals(principal)) {
				res.add(socialProfile);
			}
		}

		return res;

	}
}
