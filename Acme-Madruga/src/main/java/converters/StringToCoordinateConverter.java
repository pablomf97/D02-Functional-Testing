
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.CoordinateRepository;
import domain.Coordinate;

@Component
@Transactional
public class StringToCoordinateConverter implements Converter<String, Coordinate> {

	@Autowired
	private CoordinateRepository	repository;


	@Override
	public Coordinate convert(final String text) {
		Coordinate result;
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
