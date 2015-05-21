package test.ru.aspu.medstat;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

import ru.aspu.medstat.utils.PasswordUtils;

public class PasswordUtilsTest {
	@Test
	public void generateTest() {
		assertEquals(20, PasswordUtils.generate(20).length());
		assertNotEquals(21, PasswordUtils.generate(20).length());
		
		assertTrue(Pattern.compile("[a-zA-Z0-9]*").matcher(PasswordUtils.generate(20)).matches());
	}
}
