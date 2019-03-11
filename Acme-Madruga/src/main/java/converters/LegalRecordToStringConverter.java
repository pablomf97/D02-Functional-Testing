
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.LegalRecord;

@Component
@Transactional
public class LegalRecordToStringConverter implements Converter<LegalRecord, String> {

	@Override
	public String convert(final LegalRecord entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
