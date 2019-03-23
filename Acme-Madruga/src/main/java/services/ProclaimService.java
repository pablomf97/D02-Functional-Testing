package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProclaimRepository;
import domain.Chapter;
import domain.Proclaim;

@Service
@Transactional
public class ProclaimService {

	@Autowired
	private ProclaimRepository proclaimRepository;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private Validator validator;
	
	
	//CRUD Methods -------------------------------------------------------
	
	public Proclaim create(){
		Proclaim result;
		Chapter principal;
		
		principal = (Chapter) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, "CHAPTER"));
		
		result = new Proclaim();
		
		result.setChapter(principal);
		
		result.setPublishedMoment(new Date(System.currentTimeMillis()-1));
		
		return result;
		
	}
	
	public Proclaim save(final Proclaim result, final BindingResult binding){
		Chapter chapter;
		Proclaim proclaim = this.create();
		
		chapter = (Chapter) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(chapter, "CHAPTER"));
		Assert.isTrue(proclaim.getChapter().getId() == chapter.getId());
		
		//NO SE PUEDE MODIFICAR UNA VEZ GUARDADO
		Assert.isTrue(proclaim.getId() == 0);
		
		result.setChapter(proclaim.getChapter());
		result.setPublishedMoment(proclaim.getPublishedMoment());
		
		
		try{
			this.proclaimRepository.save(result);
		}catch(Throwable oops){
			System.out.println(oops.getMessage());
		}
		
		
		this.validator.validate(result, binding);
		
		return result;
		
	}
	
	public Proclaim findOne(int proclaimId){
		Proclaim result;
		
		result = this.proclaimRepository.findOne(proclaimId);
		
		return result;
	}
	
	public Collection<Proclaim> findAll(){
		Collection<Proclaim> result;
		
		result = this.proclaimRepository.findAll();
		
		return result;
	}
	
	public Collection<Proclaim> getProclaimsByChapter(int chapterId){
		Collection<Proclaim> result;
		
		result = this.proclaimRepository.getProclaimsByChapter(chapterId);
		
		return result;
	}
	

}
