package test.ru.aspu.medstat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ru.aspu.medstat.utils.FormatUtils;

public class FormatUtilsTest {
	@Test
	public void normalizePhoneNumberTest() {
		assertEquals("79008005000", FormatUtils.normalizePhoneNumber("+7 (900) 800-50-00"));
	}
}
