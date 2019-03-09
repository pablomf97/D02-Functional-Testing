package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Zone;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Integer> {
	
	@Query("select b.zone from Brotherhood b")
	public Collection<Zone> findSelectedZones();



}
