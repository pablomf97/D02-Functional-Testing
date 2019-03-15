
package repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.InceptionRecord;

@Repository
public interface InceptionRecordRepository extends JpaRepository<InceptionRecord, Integer> {

	@Query("select max(m.history.miscellaneousRecords.size), min(m.history.miscellaneousRecords.size), avg(m.history.miscellaneousRecords.size),sqrt(sum(m.history.miscellaneousRecords.size* m.history.miscellaneousRecords.size) / count(m.history.miscellaneousRecords.size) -(avg(m.history.miscellaneousRecords.size) * avg(m.history.miscellaneousRecords.size))) from Brotherhood m")
	Double[] statsMisc();
	@Query("select max(m.history.periodRecords.size), min(m.history.periodRecords.size), avg(m.history.periodRecords.size),sqrt(sum(m.history.periodRecords.size* m.history.periodRecords.size) / count(m.history.periodRecords.size) -(avg(m.history.periodRecords.size) * avg(m.history.periodRecords.size))) from Brotherhood m")
	Double[] statsPeriod();
	@Query("select max(m.history.legalRecords.size), min(m.history.legalRecords.size), avg(m.history.legalRecords.size),sqrt(sum(m.history.legalRecords.size* m.history.legalRecords.size) / count(m.history.legalRecords.size) -(avg(m.history.legalRecords.size) * avg(m.history.legalRecords.size))) from Brotherhood m")
	Double[] statsLegal();
	@Query("select max(m.history.linkRecords.size), min(m.history.linkRecords.size), avg(m.history.linkRecords.size),sqrt(sum(m.history.linkRecords.size* m.history.linkRecords.size) / count(m.history.linkRecords.size) -(avg(m.history.linkRecords.size) * avg(m.history.linkRecords.size))) from Brotherhood m")
	Double[] statsLink();
	

}
