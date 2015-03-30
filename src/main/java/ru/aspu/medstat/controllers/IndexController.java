package ru.aspu.medstat.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.entities.UserRoles;
import ru.aspu.medstat.forms.UserRegistrationForm;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.responses.ErrorResponse;
import ru.aspu.medstat.responses.IResponse;
import ru.aspu.medstat.responses.UserResponse;
import ru.aspu.medstat.services.MailService;
import ru.aspu.medstat.utils.EmailUtils;
import ru.aspu.medstat.utils.FormatUtils;
import ru.aspu.medstat.utils.PasswordUtils;

@Controller
public class IndexController {
    private static final Logger log = Logger.getLogger(IndexController.class);

    @Autowired
    private UserRepository repo;

    @Autowired
    private MailService mail;

    private SimpleDateFormat birthDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("UserRegistrationForm", new UserRegistrationForm());

        return "index/index";
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResponse registerUser(@RequestBody UserRegistrationForm form) {
        String error = "";
        if (null != repo.findByEmail(form.getEmail())) {
            error += "Пользователь с такой эл. почтой уже зарегистрирован\n";
        }
        if (!EmailUtils.validate(form.getEmail())) {
            error += "Невалидный адрес эл. почты. Принимается паттерн вида *@*\n";
        }
        if (form.getFirstName().length() == 0) {
            error += "Не допускается пустое имя пользователя\n";
        }
        if (form.getLastName().length() == 0) {
            error += "Не допускается пустая фамилия пользователя\n";
        }
        if (form.getPassword().length() < 6) {
            error += "Допускается пароль длиной от 6 символов\n";
        }

        String birthDate = form.getBirthDateDay().trim() + "/" + form.getBirthDateMonth().trim() + "/" + form.getBirthDateYear().trim();
        if (birthDate.length() == 0) {
            error += "Не допускается отсутствие даты рождения\n";
        }

        try {
            birthDateFormat.parse(birthDate);
        } catch (ParseException e) {
            error += "Неверный формат даты рождения. Ожидается dd/MM/yyyy\n";
        }

        if (error.length() != 0) {
            return new ErrorResponse(error);
        }

        final User user = new User();

        user.email = form.getEmail();
        user.firstName = form.getFirstName();
        user.lastName = form.getLastName();
        user.password = form.getPassword();
        user.birthDate = birthDate;
        user.phone = FormatUtils.normalizePhoneNumber(form.getPhone());
        user.role = UserRoles.PATIENT;
        user.emailToken = PasswordUtils.generate(32);

        mail.send(user.email, "Медицинский портал АГУ. Регистрация", String.format(
                "<a href=\"http://localhost:8080/mail/confirm/%s\">Нажмите сюда для окончания регистрации</a>",
                user.emailToken));

        return new UserResponse(repo.save(user));
    }
}
