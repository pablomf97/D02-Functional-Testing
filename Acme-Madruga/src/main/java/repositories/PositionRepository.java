package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {		

	@Query("select e.position from Enrolment e")
	Collection<Position> findPositionByEnrolments();
	@Query("select (select count(e) from Enrolment e where e.position=p) from Position p")
	Collection<Integer> countPositions();
}

