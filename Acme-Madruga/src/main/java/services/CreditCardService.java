package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import domain.CreditCard;
import domain.Sponsor;

import repositories.CreditCardRepository;

@Transactional
@Service
public class CreditCardService {

	@Autowired
	private CreditCardRepository creditCardRepository;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UtilityService			utilityService;

	
	// CRUD Methods
		// --------------------------------

	public Collection<CreditCard> findAll() {
		Collection<CreditCard> result = new ArrayList<>();

		result = this.creditCardRepository.findAll();
		Assert.notNull(result);

		return result;
	}
	public CreditCard findOne(final int creditCardId) {
		CreditCard result;

		result = this.creditCardRepository.findOne(creditCardId);
		Assert.notNull(result);
		return result;
	}
	
	public CreditCard create() {
		Sponsor principal;
		CreditCard result;
		
		principal = (Sponsor) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService
				.checkAuthority(principal, "SPONSOR"));

			
		result = new CreditCard();
		return result;
	}

	public CreditCard save(final CreditCard creditCard) {
		Sponsor principal;
		Assert.notNull(creditCard);
		CreditCard res;
		principal = (Sponsor) this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService
				.checkAuthority(principal, "SPONSOR"));

		String input = creditCard.getExpirationMonth()+"/"+creditCard.getExpirationYear(); // for example
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
		simpleDateFormat.setLenient(false);
		Date expiry = null;
		try {
			expiry = simpleDateFormat.parse(input);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean expired = expiry.before(new Date());
		Assert.isTrue(expired==false,"commit.error");
		
		//Calendar expiration;
		
		// Comprobacion de fecha
	//	expiration = Calendar.getInstance();
		//expiration.set(creditCard.getExpirationYear(), creditCard.getExpirationMonth() + 1, 1);
		//Assert.isTrue(expiration.getTime().before(LocalDate.now().toDate()));

		// Comprobacion de que el tipo de tarjeta es uno de los almacenados en systemConf
		Assert.isTrue(this.utilityService.getCreditCardMakes().contains(creditCard.getMake()));
		Assert.notNull(creditCard.getCVV());
		Assert.notNull(creditCard.getExpirationMonth());
		Assert.notNull(creditCard.getExpirationYear());
		Assert.notNull(creditCard.getHolder());
		Assert.notNull(creditCard.getNumber());
		Assert.notNull(creditCard.getMake());
		res = this.creditCardRepository.save(creditCard);
		Assert.notNull(res);
	//COMPROBAR QUE UN SPONSOR NO PUEDE VER UNA CREDITCARD Q NO LE PERTENECE

		return res;
		
	}




}
