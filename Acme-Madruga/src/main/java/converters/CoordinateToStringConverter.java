
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Coordinate;

@Component
@Transactional
public class CoordinateToStringConverter implements Converter<Coordinate, String> {

	@Override
	public String convert(final Coordinate coordinate) {
		String result;

		if (coordinate == null)
			result = null;
		else
			result = String.valueOf(coordinate.getId());
		return result;
	}

}
