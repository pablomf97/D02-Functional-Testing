
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.InceptionRecord;

@Repository
public interface InceptionRecordRepository extends JpaRepository<InceptionRecord, Integer> {

	/*The average, the minimum, the maximum, and the standard deviation of the
	number of records per history.*/
	// The brotherhood with the largest history.
	// The brotherhoods whose history is larger than the average.
	//select size(e.miscellaneousRecords)from History e ;
	//select size(b.history.periodRecords) from Brotherhood b;
	//select max(m.history.miscellaneousRecords.size), min(m.history.miscellaneousRecords.size), avg(m.history.miscellaneousRecords.size),sqrt(sum(m.history.miscellaneousRecords.size* m.history.miscellaneousRecords.size) / count(m.history.miscellaneousRecords.size) -(avg(m.history.miscellaneousRecords.size) * avg(m.history.miscellaneousRecords.size))) from Brotherhood m;
	
}
