package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.March;

@Repository
public interface MarchRepository extends JpaRepository<March, Integer> {
	
	@Query("select m from March m where m.member.id = ?1")
	Collection<March> findMarchsByMemberId(int memberId);

	@Query("select m from March m join m.procession p where p.brotherhood.id = ?1")
	Collection<March> findMarchsByBrotherhoodId(int brotherhoodId);

	@Query("select (sum(case when m.status='APPROVED' then 1.0 else 0 end)/count(*)) from March m")
	Double ratioApprovedRequests();
	
	@Query("select (sum(case when m.status='REJECTED' then 1.0 else 0 end)/count(*)) from March m")
	Double ratioRejectedRequests();
	
	@Query("select (sum(case when m.status='PENDING' then 1.0 else 0 end)/count(*)) from March m")
	Double ratioPendingRequests();

	
	@Query("select m from March m where m.member.id = ?1")
	Collection<March> findByMember(int memberId);
	
	@Query("select m from March m where m.procession.id = ?1")
	Collection<March> findMarchByProcession(int processionId);
	
	



}
