
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.ZoneRepository;
import domain.Zone;

@Component
@Transactional
public class StringToZoneConverter implements Converter<String, Zone> {

	@Autowired
	private ZoneRepository	zoneRepository;


	@Override
	public Zone convert(final String text) {
		Zone result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.zoneRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
