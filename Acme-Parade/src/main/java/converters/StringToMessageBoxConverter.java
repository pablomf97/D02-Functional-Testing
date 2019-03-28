
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.MessageBoxRepository;
import domain.MessageBox;

@Component
@Transactional
public class StringToMessageBoxConverter implements Converter<String, MessageBox> {

	@Autowired
	private MessageBoxRepository	messageBoxRepository;


	@Override
	public MessageBox convert(final String text) {
		MessageBox result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.messageBoxRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
