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
		Assert.notNull(segment.getDestinationLatitude(), "segment.NotEmpty");
		Assert.notNull(segment.getDestinationLongitude(), "segment.NotEmpty");
		Assert.notNull(segment.getOriginLatitude(), "segment.NotEmpty");
		Assert.notNull(segment.getOriginLongitude(), "segment.NotEmpty");
		Assert.notNull(segment.getParade(), "segment.NotEmpty");
		
		Assert.isTrue(this.actorService.checkAuthority(principal, "BROTHERHOOD"));
		Assert.isTrue(segment.getExpectedTimeOrigin().before(segment.getExpectedTimeDestination()));
		Assert.isTrue(segment.getParade().getBrotherhood().getId() == principal.getId());
		path = this.findAllSegmentsByParadeId(segment.getParade().getId());

		if (segment.getId() == 0) {
			if (path.isEmpty()) {

				segment.setIsEditable(true);

				result = this.segmentRepository.save(segment);
			} else {
				for (Segment seg : path) {
					if ((seg.getDestinationLatitude().toString()
							.equals(segment.getOriginLatitude().toString())) && 
							(seg.getDestinationLongitude().toString()
							.equals(segment.getOriginLongitude().toString()))
							&& seg.getIsEditable()) {
						
						segment.setIsEditable(true);
						result = this.segmentRepository.save(segment);
						seg.setIsEditable(false);
						this.segmentRepository.save(seg);
						break;
					}
				}
			}
		} else if (segment.getIsEditable()) {

			aux = this.findOne(segment.getId());
			Assert.isTrue((aux.getOriginLatitude().toString()
					.equals(segment.getOriginLatitude().toString())) && 
					(aux.getOriginLongitude().toString()
					.equals(segment.getOriginLongitude().toString())));

			result = this.segmentRepository.save(segment);
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
		
		Assert.isTrue(segment.getParade().getBrotherhood().getId() == principal.getId());

		path = this.findAllSegmentsByParadeId(segment.getParade().getId());
		if (path.size() != 1) {
			for (Segment seg : path) {
				if ((seg.getDestinationLatitude().toString()
						.equals(segment.getOriginLatitude().toString())) && 
						(seg.getDestinationLongitude().toString()
						.equals(segment.getOriginLongitude().toString()))) {
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

		if (segment.getId() != 0) {
			Segment result = null;
			result = this.findOne(segment.getId());
			
			Assert.isTrue((result.getOriginLatitude().toString()
					.equals(segment.getOriginLatitude().toString())) && 
					(result.getOriginLongitude().toString()
					.equals(segment.getOriginLongitude().toString())));

			result.setDestinationLatitude(segment.getDestinationLatitude());
			result.setDestinationLongitude(segment.getDestinationLongitude());
			result.setExpectedTimeDestination(segment.getExpectedTimeDestination());
			result.setExpectedTimeOrigin(segment.getExpectedTimeOrigin());
			result.setOriginLatitude(segment.getOriginLatitude());
			result.setOriginLongitude(segment.getOriginLongitude());
			
			validator.validate(result, binding);

			return result;
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
	
	public void flush(){
		this.segmentRepository.flush();
	}
}
