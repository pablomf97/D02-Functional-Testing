
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Segment;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Integer> {
	
	@Query("select s from Segment s where s.parade.id = ?1")
	Collection<Segment> findAllSegmentsByParadeId (int paradeId);

}
