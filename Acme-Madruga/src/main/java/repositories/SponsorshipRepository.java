package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Administrator;

@Repository
public interface SponsorshipRepository extends JpaRepository<Administrator, Integer> {
		


}
