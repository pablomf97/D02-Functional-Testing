package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CoordinateRepository;
import domain.Actor;
import domain.Coordinate;

@Service
@Transactional
public class CoordinateService {

	// Managed repository ------------------------------
	@Autowired
	private CoordinateRepository coordinateRepository;

	// Supporting services -----------------------
	@Autowired
	private ActorService actorService;

	// Constructors

	public CoordinateService() {
		super();
	}

	// /CREATE
	public Coordinate create() {
		Coordinate result;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = new Coordinate();

		return result;
	}

	// /FINDONE
	public Coordinate findOne(final int coordinateId) {
		Coordinate result;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = this.coordinateRepository.findOne(coordinateId);
		Assert.notNull(result);

		return result;
	}

	// //SAVE

	public Coordinate save(final Coordinate coordinate) {
		Coordinate result;
		Actor principal;

		Assert.notNull(coordinate);

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(coordinate.getLatitude(), "coordinate.NotEmpty");
		Assert.notNull(coordinate.getLongitude(), "coordinate.NotEmpty");
		result = this.coordinateRepository.save(coordinate);
		this.coordinateRepository.flush();
		Assert.notNull(result);

		return result;

	}

	// FINDALL
	public Collection<Coordinate> findAll() {
		Collection<Coordinate> result;

		result = this.coordinateRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	// DELETE

	public void delete(final Coordinate coordinate) {
		Actor principal;

		Assert.notNull(coordinate);
		Assert.isTrue(coordinate.getId() != 0);

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		this.coordinateRepository.delete(coordinate);

	}

	// // Other business methods
	
	public List<Coordinate> saveAll(final Collection<Coordinate> coordinates) {
		List<Coordinate> result;
		Actor principal;

		Assert.notNull(coordinates);

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		for(Coordinate coordinate : coordinates) {
			Assert.notNull(coordinate.getLatitude(), "coordinate.NotEmpty");
			Assert.notNull(coordinate.getLongitude(), "coordinate.NotEmpty");
		}
		
		result = this.coordinateRepository.save(coordinates);
		this.coordinateRepository.flush();
		Assert.notNull(result);

		return result;

	}

}
