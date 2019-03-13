
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Parade;

@Repository
public interface ParadeRepository extends JpaRepository<Parade, Integer> {

	@Query("select p from Parade p where p.brotherhood.id= ?1")
	Collection<Parade> findParadesByBrotherhoodId(int brotherhoodId);

	@Query("select p from March m join m.parade p where m.status = 'APPROVED' and m.member.id = ?1")
	Collection<Parade> findAcceptedParadesByMemberId(int memberId);

	@Query("select p from March m join m.parade p where m.member.id = ?1")
	Collection<Parade> findParadesAlreadyApplied(int memberId);

	@Query("select p from Parade p where p.isDraft = 'false'")
	Collection<Parade> findFinalParades();

	@Query("select p from Parade p where p.isDraft = 'false' and p.brotherhood.id =?1")
	Collection<Parade> findFinalParadeByBrotherhood(int brotherhoodId);

	@Query("select p from Parade p where p.organisedMoment > NOW() AND p.organisedMoment < ?1")
	Collection<Parade> findEarlyParades(Date maxDate);

}
