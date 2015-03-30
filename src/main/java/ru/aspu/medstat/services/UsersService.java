package ru.aspu.medstat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.utils.EmailUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class UsersService {
    @Autowired
    private MailService mail;

    private SimpleDateFormat birthDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public String getErrors(User user) {
        String errors = "";
        if (!EmailUtils.validate(user.email)) {
            errors += "Невалидный адрес эл. почты. Принимается паттерн вида *@*\n";
        }
        if (user.firstName.length() == 0) {
            errors += "Не допускается пустое имя пользователя\n";
        }
        if (user.lastName.length() == 0) {
            errors += "Не допускается пустая фамилия пользователя\n";
        }
        if (user.password.length() < 6) {
            errors += "Допускается пароль длиной от 6 символов\n";
        }

        if (user.birthDate.length() == 0) {
            errors += "Не допускается отсутствие даты рождения\n";
        }

        try {
            birthDateFormat.parse(user.birthDate);
        } catch (ParseException e) {
            errors += "Неверный формат даты рождения. Ожидается dd/MM/yyyy\n";
        }

        return errors;
    }

    public void sendMail(User user) {
        mail.send(user.email, "Медицинский портал АГУ. Регистрация", String.format(
                "<a href=\"http://localhost:8080/auth/confirm/%s\">Нажмите сюда для окончания регистрации</a>",
                user.emailToken));
    }
}
