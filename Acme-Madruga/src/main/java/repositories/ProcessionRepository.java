package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Procession;

@Repository
public interface ProcessionRepository extends JpaRepository<Procession, Integer> {

	
	@Query("select p from Procession p where p.brotherhood.id= ?1")
	Collection<Procession> findProcessionsByBrotherhoodId(int brotherhoodId);
	
	@Query("select p from March m join m.procession p where m.status = 'APPROVED' and m.member.id = ?1")
	Collection<Procession> findAcceptedProcessionsByMemberId(int memberId);	
	
	@Query("select p from March m join m.procession p where m.member.id = ?1")
	Collection<Procession> findProcessionsAlreadyApplied(int memberId);	
	
	@Query("select p from Procession p where p.isDraft = 'false'")
	Collection<Procession> findFinalProcessions();	
	
	@Query("select p from Procession p where p.isDraft = 'false' and p.brotherhood.id =?1")
	Collection<Procession> findFinalProcessionByBrotherhood(int brotherhoodId);	
		
	@Query("select p from Procession p where p.organisedMoment > NOW() AND p.organisedMoment < ?1")
	Collection<Procession> findEarlyProcessions(Date maxDate);

}
