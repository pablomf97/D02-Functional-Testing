package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;

@Repository
public interface BrotherhoodRepository extends
		JpaRepository<Brotherhood, Integer> {

	@Query("select b from Brotherhood b where b.zone.id = ?1")
	Collection<Brotherhood> brotherhoodsByZone(int zoneId);

	@Query("select b from Brotherhood b")
	Collection<Brotherhood> allBros();

	@Query("select b from Enrolment e join e.brotherhood b where e.member.id = ?1 and (e.isOut = true or e.isOut IS NULL)")
	Collection<Brotherhood> brotherhoodsByMemberId(int memberId);

	@Query("select b from Enrolment e join e.brotherhood b where e.member.id = ?1")
	Collection<Brotherhood> allBrotherhoodsByMemberId(int memberId);

	@Query("select b from Enrolment e join e.brotherhood b where e.member.id = ?1 and e.isOut = false")
	Collection<Brotherhood> brotherhoodsByMemberInId(int memberId);

}
