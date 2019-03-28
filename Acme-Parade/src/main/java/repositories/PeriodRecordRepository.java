
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;
import domain.PeriodRecord;

@Repository
public interface PeriodRecordRepository extends JpaRepository<PeriodRecord, Integer> {



}
