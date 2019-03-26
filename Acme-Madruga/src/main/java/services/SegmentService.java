package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SegmentRepository;
import domain.Actor;
import domain.Coordinate;
import domain.Segment;

@Service
@Transactional
public class SegmentService {

	// Managed repository ------------------------------
	@Autowired
	private SegmentRepository segmentRepository;

	// Supporting services -----------------------
	@Autowired
	private ActorService actorService;

	@Autowired
	private Validator validator;

	@Autowired
	private CoordinateService coordinateService;

	// Constructors

	public SegmentService() {
		super();
	}

	// /CREATE
	public Segment create() {
		Segment result;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = new Segment();

		return result;
	}

	// /FINDONE
	public Segment findOne(final int segmentId) {
		Segment result;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = this.segmentRepository.findOne(segmentId);
		Assert.notNull(result);

		return result;
	}

	// //SAVE

	public Segment save(final Segment segment) {
		Segment result = null, aux = null;
		Actor principal;
		Collection<Segment> path;

		Assert.notNull(segment);

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(segment.getExpectedTimeDestination(), "segment.NotEmpty");
		Assert.notNull(segment.getExpectedTimeOrigin(), "segment.NotEmpty");
		Assert.notNull(segment.getDestination(), "segment.NotEmpty");
		Assert.notNull(segment.getOrigin(), "segment.NotEmpty");
		Assert.notNull(segment.getParade(), "segment.NotEmpty");
		Assert.isTrue(this.actorService
				.checkAuthority(principal, "BROTHERHOOD"));
		Assert.isTrue(segment.getExpectedTimeOrigin().before(
				segment.getExpectedTimeDestination()));

		path = this.findAllSegmentsByParadeId(segment.getParade().getId());

		if (segment.getId() == 0) {
			if (path.isEmpty()) {

				segment.setIsEditable(true);

				result = this.segmentRepository.save(segment);
			} else {
				for (Segment seg : path) {
					if (seg.getDestination().toString()
							.equals(segment.getOrigin().toString())
							&& seg.getIsEditable()) {
						segment.setIsEditable(true);
						Coordinate coor1 = this.coordinateService.create();
						this.coordinateService.save(coor1);
						Coordinate coor2 = this.coordinateService.create();
						this.coordinateService.save(coor2);
						result = this.segmentRepository.save(segment);
						seg.setIsEditable(false);
						this.segmentRepository.save(seg);
						break;
					}
				}
			}
		} else if (segment.getIsEditable()) {
			Coordinate coor, toDelete = null;

			aux = this.findOne(segment.getId());
			Assert.isTrue(aux.getOrigin().toString()
					.equals(segment.getOrigin().toString()));

			if (!aux.getDestination().toString()
					.equals(segment.getDestination().toString())) {
				toDelete = aux.getDestination();
				coor = this.coordinateService.save(segment.getDestination());

				aux.setDestination(coor);
			}
			aux.setExpectedTimeDestination(segment.getExpectedTimeDestination());
			aux.setExpectedTimeOrigin(segment.getExpectedTimeOrigin());

			result = this.segmentRepository.save(aux);

			if (!aux.getDestination().toString()
					.equals(segment.getDestination().toString())) {
				this.coordinateService.delete(toDelete);
			}
		}

		Assert.notNull(result);

		return result;

	}

	// FINDALL
	public Collection<Segment> findAll() {
		Collection<Segment> result;

		result = this.segmentRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	// DELETE

	public void delete(final Segment segment) {
		Actor principal;
		Collection<Segment> path;

		Assert.notNull(segment);
		Assert.isTrue(segment.getId() != 0);
		Assert.isTrue(segment.getIsEditable());

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		path = this.findAllSegmentsByParadeId(segment.getParade().getId());
		if (path.size() != 1) {
			for (Segment seg : path) {
				if (seg.getDestination().toString()
						.equals(segment.getOrigin().toString())) {
					seg.setIsEditable(true);
					this.segmentRepository.save(seg);
					this.segmentRepository.delete(segment);
				}
			}
		} else {
			this.segmentRepository.delete(segment.getId());
		}
	}

	// // Other business methods

	public Segment reconstruct(Segment segment, BindingResult binding) {
		Segment result = null;

		if (segment.getId() != 0) {
			result = this.findOne(segment.getId());

			segment.setParade(result.getParade());
			segment.setIsEditable(result.getIsEditable());
		}

		validator.validate(segment, binding);

		return segment;
	}

	public void deleteAll(final Integer paradeId) {
		Actor principal;
		Collection<Segment> toDelete;

		Assert.isTrue(paradeId != 0);

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		toDelete = this.findAllSegmentsByParadeId(paradeId);

		this.segmentRepository.delete(toDelete);

	}

	public Collection<Segment> findAllSegmentsByParadeId(int paradeId) {
		Collection<Segment> result;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = this.segmentRepository.findAllSegmentsByParadeId(paradeId);
		Assert.notNull(result);

		return result;
	}
}
