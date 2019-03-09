
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Zone;

@Component
@Transactional
public class ZoneToStringConverter implements Converter<Zone, String> {

	@Override
	public String convert(final Zone zone) {
		String result;

		if (zone == null)
			result = null;
		else
			result = String.valueOf(zone.getId());
		return result;
	}

}
