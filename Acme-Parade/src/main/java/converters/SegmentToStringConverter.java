
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Segment;

@Component
@Transactional
public class SegmentToStringConverter implements Converter<Segment, String> {

	@Override
	public String convert(final Segment entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
