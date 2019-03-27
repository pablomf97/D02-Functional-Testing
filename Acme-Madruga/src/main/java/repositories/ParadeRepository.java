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

	@Query("select p from Parade p where p.brotherhood.zone.id = ?1")
	Collection<Parade> findParadesByAres(int areaId);

	/* The ratio of parades in draft mode versus parades in final mode */
	@Query("select count(p) /(select count(p1) from Parade p1 where p1.isDraft = false)*1.0 from Parade p where p.isDraft = true")
	Double ratioDraftVsFinal();

	/* The ratio of parades in final mode grouped by status */
	@Query("select count(distinct p)*1.0 / count(distinct p1) from Parade p, Parade p1 where p.isDraft = false and p1.isDraft = false group by p.status")
	Double[] ratioFinalModeGroupedByStatus();

	@Query("select s from Parade s where s.id = ?1")
	Parade findOne(int id);
}
