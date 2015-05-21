package test.ru.aspu.medstat;

import static org.junit.Assert.*;

import org.junit.Test;

import ru.aspu.medstat.utils.EmailUtils;

public class EmailUtilsTest {
	@Test
	public void validateTest() {
		assertTrue(EmailUtils.validate("a@b"));
		assertTrue(EmailUtils.validate("a@b.ru"));
		assertFalse(EmailUtils.validate("a@b."));
		assertFalse(EmailUtils.validate("ab"));
	}
}
