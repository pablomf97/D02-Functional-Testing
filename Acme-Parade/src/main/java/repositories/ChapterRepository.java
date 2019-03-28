package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chapter;
import domain.Parade;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	/* Ratio of the areas that are not coordinated by any chapter */
	@Query("select count(a) / (select count(b) from Zone b)*1.0 from Chapter c join c.zone a where a != null")
	Double ratioAreasNotManaged();

	@Query("select p from Parade p where p.brotherhood.zone.id = ?1")
	Collection<Parade> getParadesPerArea(int areaId);
}
