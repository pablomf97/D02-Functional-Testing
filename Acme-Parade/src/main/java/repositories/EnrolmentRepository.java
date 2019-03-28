package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Enrolment;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Integer> {

	@Query("select e from Enrolment e where e.member.id = ?1")
	Collection<Enrolment> getEnrollmentsByMember(int memberID);

	@Query("select e from Enrolment e where e.brotherhood.id = ?1")
	Collection<Enrolment> getEnrollmentsByBrotherhood(int brotherhoodID);

	@Query("select e from Enrolment e where e.isOut='false' and e.brotherhood.id = ?1")
	Collection<Enrolment> findActiveEnrolmentsByBrotherhood(int brotherhoodId);

	@Query("select e from Enrolment e where e.isOut='false' and e.member.id = ?1")
	Collection<Enrolment> findActiveEnrolmentsByMember(int memberId);

	@Query("select e from Enrolment e where e.isOut='false'")
	Collection<Enrolment> findActiveEnrolments();

	@Query("select e from Enrolment e where e.position.id = ?1")
	Collection<Enrolment> getEnrolmentsUsingPosition(int positionID);
	
}