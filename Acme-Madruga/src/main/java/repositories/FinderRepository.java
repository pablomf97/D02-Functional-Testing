
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;
import domain.Parade;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select p from Parade p where  p.isDraft='0'")
	Collection<Parade> searchAllParade();

	@Query("select max(m.finder.searchResults.size), min(m.finder.searchResults.size), avg(m.finder.searchResults.size),sqrt(sum(m.finder.searchResults.size* m.finder.searchResults.size) / count(m.finder.searchResults.size) -(avg(m.finder.searchResults.size) * avg(m.finder.searchResults.size))) from Member m")
	Double[] StatsFinder();

	@Query("select p from Parade p where p.isDraft='0' and (p.ticker like %?1% or p.description like %?1% or p.title like %?1%) and (p.brotherhood.zone.name like %?2%)  and p.organisedMoment between ?3 and ?4 ")
	Collection<Parade> findByFilter(String keyWord, String area, Date minimumMoment, Date maximumMoment);

	@Query("select f from Finder f where f.searchResults.size='0'")
	public Collection<Finder> FindersEmpty();

}
