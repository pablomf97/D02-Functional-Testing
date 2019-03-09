
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsorship, Integer> {

}
