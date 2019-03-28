
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.LinkRecordRepository;
import domain.LinkRecord;

@Component
@Transactional
public class StringToLinkRecordConverter implements Converter<String, LinkRecord> {

	@Autowired
	private LinkRecordRepository	repository;


	@Override
	public LinkRecord convert(final String text) {
		LinkRecord result;
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
