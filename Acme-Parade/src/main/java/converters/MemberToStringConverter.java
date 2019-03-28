
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Member;

@Component
@Transactional
public class MemberToStringConverter implements Converter<Member, String> {

	@Override
	public String convert(final Member actor) {
		String result;

		if (actor == null)
			result = null;
		else
			result = String.valueOf(actor.getId());
		return result;
	}

}
