
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.LinkRecord;

@Component
@Transactional
public class LinkRecordToStringConverter implements Converter<LinkRecord, String> {

	@Override
	public String convert(final LinkRecord entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
