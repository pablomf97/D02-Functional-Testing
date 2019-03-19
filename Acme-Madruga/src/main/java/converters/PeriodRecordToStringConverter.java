
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.PeriodRecord;

@Component
@Transactional
public class PeriodRecordToStringConverter implements Converter<PeriodRecord, String> {

	@Override
	public String convert(final PeriodRecord entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
