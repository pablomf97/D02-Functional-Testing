package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PositionRepository;
import domain.Actor;
import domain.Position;

@Service
@Transactional
public class PositionService {

	// Managed Repository

	@Autowired
	private PositionRepository positionRepository;
	// Supporting Services

	@Autowired
	private ActorService actorService;

	@Autowired
	private Validator validator;

	// Simple CRUD methods

	/* Find one by ID */
	public Position findOne(final int positionId) {
		Position res;

		res = this.positionRepository.findOne(positionId);

		return res;

	}

	/* Find all members */
	public Collection<Position> findAll() {
		Collection<Position> res;

		res = this.positionRepository.findAll();
		Assert.notNull(res, "no.positions");

		return res;
	}

	/* Create a position */
	public Position create() {
		Actor principal;
		Position res;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "ADMINISTRATOR"),
				"not.allowed");

		res = new Position();

		return res;
	}

	/* Save a position */
	public Position save(Position position) {
		Actor principal;
		Position res;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "ADMINISTRATOR"),
				"not.allowed");

		Assert.notNull(position, "null.position");
		Assert.isTrue(!(position.getName().get("Español").isEmpty() || position
				.getName().get("English").isEmpty()), "position.name.empty");

		res = this.positionRepository.save(position);

		return res;
	}

	/* Delete a position */
	public void delete(Position position) {
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(
				this.actorService.checkAuthority(principal, "ADMINISTRATOR"),
				"not.allowed");

		Assert.notNull(position, "null.position");

		this.positionRepository.delete(position);
	}

	// Other business methods

	/* Reconstruct */
	public Position reconstruct(Position position, String nameES,
			String nameEN, BindingResult binding) {
		Position res;

		if (position.getId() == 0) {
			position.setName(new HashMap<String, String>());

			position.getName().put("Español", nameES);
			position.getName().put("English", nameEN);
			res = position;
		} else {
			res = this.positionRepository.findOne(position.getId());

			position.setName(new HashMap<String, String>());

			position.getName().put("Español", nameES);
			position.getName().put("English", nameEN);

			res.setName(position.getName());
		}
		this.validator.validate(res, binding);

		return res;
	}

	/* Find position by name */
	public Position findByName(String name) {
		Position res;
		Collection<Position> positions;

		res = null;
		positions = this.findAll();
		for (Position position : positions) {
			if (position.getName().containsValue(name)) {
				res = position;
				break;
			}
		}

		return res;
	}
	public Collection<Integer> countPositions(){
		
		Collection<Integer> res=this.positionRepository.countPositions();
		
		return res;
		
	}
	
	public Collection<String> nameEsPositions(){
		Collection<String> result = new ArrayList<String>();
		Collection<Position> positions;
		positions = this.findAll();
		
		for(Position p : positions){
			result.add("'"+p.getName().get("Español")+"'");
		}
		return result;
		
	}
	
	public Collection<String> nameEnPositions(){
		Collection<String> result = new ArrayList<String>();
		Collection<Position> positions;
		
		positions = this.findAll();
		
		for(Position p : positions){
			result.add("'"+p.getName().get("English")+"'");
		}
		 
		return result;
		
	}
/*	public Integer[] histogram() {

		Collection<Position> positions;
		Collection<Position> positiosnByEnrolments;
		Integer president = 0;
		Integer vicepresident = 0;
		Integer secretary = 0;
		Integer treasurer = 0;
		Integer historian = 0;
		Integer fundraiser = 0;
		Integer officer = 0;

		positions = this.positionRepository.findAll();

		Integer[] result = new Integer[positions.size()];

		positiosnByEnrolments = this.findPositionByEnrolmennts();

		for (Position p : positiosnByEnrolments) {
			if (p.getName().containsValue("President")) {

				president++;
			}

			if (p.getName().containsValue("Vicepresident")) {
				vicepresident++;
			}
			if (p.getName().containsValue("Secreatry")) {
				secretary++;
			}
			if (p.getName().containsValue("Treasurer")) {
				treasurer++;
			}
			if (p.getName().containsValue("Historian")) {
				historian++;
			}
			if (p.getName().containsValue("Fundraiser")) {
				fundraiser++;
			}

			if (p.getName().containsValue("Officer")) {
				officer++;
			}

		}

		result[0] = president;
		result[1] = vicepresident;
		result[2] = secretary;
		result[3] = treasurer;
		result[4] = historian;
		result[5] = fundraiser;
		result[6] = officer;

		return result;
	}
*/
	public Collection<Position> findPositionByEnrolmennts() {
		Collection<Position> result;

		result = this.positionRepository.findPositionByEnrolments();

		return result;

	}

}
