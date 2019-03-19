
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.InceptionRecordRepository;
import domain.InceptionRecord;

@Component
@Transactional
public class StringToInceptionRecordConverter implements Converter<String, InceptionRecord> {

	@Autowired
	private InceptionRecordRepository	repository;


	@Override
	public InceptionRecord convert(final String text) {
		InceptionRecord result;
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
