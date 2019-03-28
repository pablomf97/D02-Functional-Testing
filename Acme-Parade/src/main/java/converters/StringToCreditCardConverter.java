
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.CreditCardRepository;
import domain.CreditCard;

@Component
@Transactional
public class StringToCreditCardConverter implements Converter<String, CreditCard> {

	@Autowired
	private CreditCardRepository	repository;


	@Override
	public CreditCard convert(final String text) {
		CreditCard result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.repository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
