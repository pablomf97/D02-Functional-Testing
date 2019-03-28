package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MemberRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Brotherhood;
import domain.Finder;
import domain.March;
import domain.Member;
import forms.MemberForm;

@Service
@Transactional
public class MemberService {

	// Managed Repository

	@Autowired
	private MemberRepository memberRepository;

	// Supporting Services

	@Autowired
	private Validator validator;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private EnrolmentService enrolmentService;

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private MarchService marchService;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private FinderService finderService;
	@Autowired
	private MessageBoxService messageBoxService;

	// Simple CRUD methods
	/* Create a member */
	public Member create() {
		final Member res = new Member();
		final UserAccount a = this.userAccountService.create();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.MEMBER);
		final Finder finder = new Finder();
		res.setFinder(finder);
		a.addAuthority(auth);
		a.setIsBanned(false);
		res.setUserAccount(a);

		return res;
	}

	public MemberForm createForm() {

		final MemberForm res = new MemberForm();

		return res;
	}

	/* Find one by ID */
	public Member findOne(final int memberId) {
		Member res;
		Assert.isTrue(memberId != 0, "Wrong member id");
		res = this.memberRepository.findOne(memberId);
		Assert.notNull(res, "The member does not exist");
		return res;

	}

	/* Find all members */
	public Collection<Member> findAll() {
		Collection<Member> res;

		res = this.memberRepository.findAll();
		Assert.notNull(res, "no.members");

		return res;
	}

	/* Save a member */
	public Member save(final Member member) {
		Member result;
		Assert.notNull(member);
		if (member.getId() == 0) {
			final UserAccount account = member.getUserAccount();
			final Authority au = new Authority();
			au.setAuthority(Authority.MEMBER);
			Assert.isTrue(account.getAuthorities().contains(au),
					"You can not register with this authority");
			final UserAccount savedAccount = this.userAccountService
					.save(account);
			member.setUserAccount(savedAccount);
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String hash = encoder.encodePassword(member.getUserAccount()
					.getPassword(), null);
			member.getUserAccount().setPassword(hash);
			final Finder f = this.finderService.save(member.getFinder());
			member.setFinder(f);
			result = this.memberRepository.save(member);
			this.messageBoxService.initializeDefaultBoxes(result);
		} else {
			final UserAccount userAccount = LoginService.getPrincipal();
			final Member memberBD = this.memberRepository.findOne(member
					.getId());
			Assert.isTrue(member.getUserAccount().equals(userAccount)
					|| memberBD.getUserAccount().equals(userAccount),
					"This account does not belong to you");
			result = this.memberRepository.save(member);
		}
		return result;
	}

	// Other business methods

	public Member findByPrincipal() {
		Member res;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		res = this.findMemberByUserAccount(userAccount.getId());
		Assert.notNull(res);

		return res;
	}

	public Member findMemberByUserAccount(final int userAccountId) {
		Assert.isTrue(userAccountId != 0);

		Member res;

		res = this.memberRepository.findMemberByUserAccount(userAccountId);

		Assert.notNull(res);

		return res;
	}

	public Collection<Member> findAllMembersByBrotherhood(
			final int brotherhoodId) {
		Collection<Member> result;

		result = this.memberRepository
				.findAllMembersByBrotherhood(brotherhoodId);
		Assert.notNull(result);

		return result;

	}

	// Dashboard
	// --------------------------------------------------------------------

	public Double averageMemberPerBrotherhood() {

		Collection<Brotherhood> brotherhoods;
		Collection<Member> members;
		int total = 0;
		Double result = 0.0;

		brotherhoods = this.brotherhoodService.allBros();

		for (final Brotherhood b : brotherhoods) {
			members = this.findAllMembersByBrotherhood(b.getId());
			total = total + members.size();
		}

		if (total != 0) {
			result = (double) (total / brotherhoods.size());
		}

		return result;
	}

	public Double minMemberPerBrotherhood() {
		Collection<Brotherhood> brotherhoods;
		Collection<Member> members;
		int count = 0;
		Double result = 0.0;

		brotherhoods = this.brotherhoodService.findAll();

		for (final Brotherhood b : brotherhoods) {
			members = this.findAllMembersByBrotherhood(b.getId());

			if (count == 0)
				result = (double) members.size();

			if (members.size() < result)
				result = (double) members.size();
			count++;
		}
		return result;
	}

	public Double maxMemberPerBrotherhood() {
		Collection<Brotherhood> brotherhoods;
		Collection<Member> members;
		int count = 0;
		Double result = 0.0;

		brotherhoods = this.brotherhoodService.findAll();

		for (final Brotherhood b : brotherhoods) {
			members = this.findAllMembersByBrotherhood(b.getId());

			if (count == 0)
				result = (double) members.size();

			if (members.size() > result)
				result = (double) members.size();
			count++;
		}
		return result;
	}

	public Collection<Member> acceptedMembers() {
		Collection<Member> members;
		Collection<March> marchsByMember, marchs;
		int totalAccepted = 0;
		Double percent;

		marchs = this.marchService.findAll();
		Assert.notNull(marchs);

		members = this.findAll();
		Assert.notNull(members);

		percent = totalAccepted * 0.1;

		for (final Member m : members) {
			marchsByMember = this.marchService.findMarchsByMemberId(m.getId());

			for (final March ma : marchsByMember)
				if (ma.getStatus().equals("APPROVED"))
					totalAccepted++;

		}
		return members;

	}

	/**
	 * Change the incomplete member to an domain object
	 * 
	 * @param member
	 * @param binding
	 * @return member
	 */
	public Member reconstruct(final MemberForm memberForm,
			final BindingResult binding) {
		Member result = this.create();
		if (memberForm.getId() == 0) {
			result.getUserAccount().setUsername(memberForm.getUsername());
			result.getUserAccount().setPassword(memberForm.getPassword());
			result.setAddress(memberForm.getAddress());
			result.setEmail(memberForm.getEmail());
			result.setMiddleName(memberForm.getMiddleName());
			result.setName(memberForm.getName());
			result.setPhoneNumber(memberForm.getPhoneNumber());
			result.setPhoto(memberForm.getPhoto());
			result.setSurname(memberForm.getSurname());
			this.validator.validate(memberForm, binding);

		} else {
			result = this.memberRepository.findOne(memberForm.getId());
			Assert.notNull(result);
			if (this.checkValidation(memberForm, binding, result)) {
				result.setAddress(memberForm.getAddress());
				result.setEmail(memberForm.getEmail());
				result.setMiddleName(memberForm.getMiddleName());
				result.setName(memberForm.getName());
				result.setPhoneNumber(memberForm.getPhoneNumber());
				result.setPhoto(memberForm.getPhoto());
				result.setSurname(memberForm.getSurname());
			} else {
				result = this.create();
				result.setAddress(memberForm.getAddress());
				result.setEmail(memberForm.getEmail());
				result.setMiddleName(memberForm.getMiddleName());
				result.setName(memberForm.getName());
				result.setPhoneNumber(memberForm.getPhoneNumber());
				result.setPhoto(memberForm.getPhoto());
				result.setSurname(memberForm.getSurname());
			}
		}
		return result;
	}

	private boolean checkValidation(final MemberForm memberForm,
			final BindingResult binding, final Member member) {
		boolean check = true;
		memberForm.setCheckBox(true);
		memberForm.setPassword(member.getUserAccount().getPassword());
		memberForm.setPassword2(member.getUserAccount().getPassword());
		memberForm.setUsername(member.getUserAccount().getUsername());
		this.validator.validate(memberForm, binding);
		if (binding.hasErrors())
			check = false;
		return check;
	}

	public Double stdevMembersPerBrotherhood() {
		Collection<Brotherhood> brotherhoods;
		Collection<Member> members = new ArrayList<Member>();

		brotherhoods = this.brotherhoodService.allBros();

		for (Brotherhood br : brotherhoods) {
			members.addAll(this.findAllMembersByBrotherhood(br.getId()));

		}

		Double n = (double) members.size();
		Double average = (double) members.size() / brotherhoods.size();
		Double averageTimes = (double) average * n;
		Double lele = (Math.pow(n - averageTimes, 2));
		Double lolo = 1 / (n - 1);
		Double stdev = (double) Math.sqrt(lolo * lele);

		return stdev;
	}

}
