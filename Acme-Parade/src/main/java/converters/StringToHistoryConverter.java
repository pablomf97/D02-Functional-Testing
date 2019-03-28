package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.HistoryRepository;

import domain.History;




@Component
@Transactional
public class StringToHistoryConverter  implements Converter<String, History>  {
	
	@Autowired
	private HistoryRepository repository;
	
	@Override
	public History convert(final String text) {
		History result;
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
