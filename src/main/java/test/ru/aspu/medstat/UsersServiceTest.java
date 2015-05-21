package test.ru.aspu.medstat;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.services.UsersService;

public class UsersServiceTest {
	@Test
	public void getErrorsTest() {
		User user = new User();
		user.email = "a@b";
		user.firstName = "Azaza";
		user.lastName = "Ololo";
		user.password = "111111";
		user.birthDate = "30/06/1993";
		
		UsersService usersService = new UsersService(new SimpleDateFormat() {
			public Date parse(String s) throws ParseException {
				return null;
			}
		});
		
		assertEquals("", usersService.getErrors(user));
		
		String error;
		
		user.email = "a";
		error = "Невалидный адрес эл. почты. Принимается паттерн вида *@*\n";
		assertEquals(error, usersService.getErrors(user));
		
		user.firstName = "";
		error += "Не допускается пустое имя пользователя\n";
		assertEquals(error, usersService.getErrors(user));
		
		user.lastName = "";
		error += "Не допускается пустая фамилия пользователя\n";
		assertEquals(error, usersService.getErrors(user));
		
		user.password = "1";
		error += "Допускается пароль длиной от 6 символов\n";
		assertEquals(error, usersService.getErrors(user));
		
		user.birthDate = "";
		String emptyBdError = error + "Не допускается отсутствие даты рождения\n";
		assertEquals(emptyBdError, usersService.getErrors(user));
		
		usersService = new UsersService(new SimpleDateFormat() {
			public Date parse(String s) throws ParseException {
				throw new ParseException("ERRRORORORO", 1);
			}
		});
		
		user.birthDate = "06/1993";
		error += "Неверный формат даты рождения. Ожидается dd/MM/yyyy\n";
		assertEquals(error, usersService.getErrors(user));
	}
}
