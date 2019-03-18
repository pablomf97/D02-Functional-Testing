
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;
import domain.LinkRecord;

@Repository
public interface LinkRecordRepository extends JpaRepository<LinkRecord, Integer> {

	@Query("select l.linkedBrotherhood from LinkRecord l")
	public Collection<Brotherhood> getLinkedBrotherhoods();
}
