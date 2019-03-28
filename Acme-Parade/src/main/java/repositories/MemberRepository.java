package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

	@Query("select m from Member m where m.userAccount.id=?1")
	Member findMemberByUserAccount(int userAccountId);
	
	@Query("select e.member from Enrolment e where e.brotherhood.id = ?1 and e.isOut = 'false'")
	Collection<Member> findAllMembersByBrotherhood(int brotherhoodId);
	
	
		
}