
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	@Query("select s from Sponsorship s where s.sponsor.id = ?1")
	Collection<Sponsorship> getSponsorshipsBySponsor(int sponsorId);
	
	@Query("select count (*) from Sponsorship s where s.sponsor.id= ?1 AND s.isDeactivated='false'")
	Integer numberActiveSponsorshipPerSponsor(int sponsorId);
	
	@Query("select (sum(case when s.isDeactivated='0.0' then 1.0 else 0 end)/count(*)) from Sponsorship s")
	Double ratioActiveSponsorship(); 
	//@Query("select count(a)/(select count(a) from Application a where a.fixUpTask.endMoment < CURRENT_DATE and a.status='PENDING')*1.0 from Application a where a.status='PENDING'")
	//Double ratioPendingApplicationsElapsedPeriod();

	//@Query("select avg(a.offeredPrice), max(a.offeredPrice), min(a.offeredPrice), sqrt(sum(a.offeredPrice*a.offeredPrice)/count(a.offeredPrice)-(avg(a.offeredPrice)*avg(a.offeredPrice))) from Application a group by 'a'")
	//Double[] findDataApplicationsOfferedPrice();
	// select max (s) from Sponsorship s where s.isDeactivated='0.0';
	
}
