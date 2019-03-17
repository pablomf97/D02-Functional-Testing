package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.History;


@Component
@Transactional
public class HistoryToStringConverter implements Converter<History, String>{

	@Override
	public String convert(final History entity) {
		String result;

		if (entity == null)
			result = null;
		else
			result = String.valueOf(entity.getId());
		return result;
	}

}
