
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.LegalRecordRepository;
import domain.LegalRecord;

@Component
@Transactional
public class StringToLegalRecordConverter implements Converter<String, LegalRecord> {

	@Autowired
	private LegalRecordRepository	repository;


	@Override
	public LegalRecord convert(final String text) {
		LegalRecord result;
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
