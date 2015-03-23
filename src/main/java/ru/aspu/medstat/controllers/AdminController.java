package ru.aspu.medstat.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aspu.medstat.responses.ErrorResponse;
import ru.aspu.medstat.responses.IResponse;
import ru.aspu.medstat.responses.UserResponse;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.entities.UserRoles;
import ru.aspu.medstat.forms.UserRegistrationForm;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.services.MailService;
import ru.aspu.medstat.utils.EmailUtils;
import ru.aspu.medstat.utils.PasswordUtils;
import ru.aspu.medstat.utils.FormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger log = Logger.getLogger(AdminController.class);

    @Autowired
    private UserRepository repo;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private MailService mail;

    private SimpleDateFormat birthDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping("/")
    public String index(Model model) {
        UserRegistrationForm form = new UserRegistrationForm();
        form.setBirthDate("1973-01-01");

        model.addAttribute("UserRegistrationForm", form);

        return "admin/index";
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResponse registerUser(@RequestBody UserRegistrationForm form) {
        String error = "";
        if (form.getRole() != UserRoles.PATIENT && form.getRole() != UserRoles.DOCTOR) {
            error += "Допускается регистрация только пациентов и докторов\n";
        }
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
        if (form.getBirthDate().length() == 0) {
            error += "Не допускается отсутствие даты рождения пользователя\n";
        }

        try {
            birthDateFormat.parse(form.getBirthDate());
        } catch (ParseException e) {
            error += "Неверный формат даты рождения. Ожидается dd-MM-yyyy\n";
        }

        if (error.length() != 0) {
            return new ErrorResponse(error);
        }

        final User user = new User();

        user.email = form.getEmail();
        user.firstName = form.getFirstName();
        user.lastName = form.getLastName();
        user.birthDate = form.getBirthDate();
        user.phone = FormatUtils.normalizePhoneNumber(form.getPhone());
        user.role = form.getRole();
        user.emailToken = PasswordUtils.generate(32);

        final User dbUser = repo.save(user);
        if (null != dbUser) {
            mail.send(user.email, "Медицинский портал АГУ. Регистрация", String.format(
                    "<a href=\"http://localhost:8080/mail/confirm/%s\">Ссылка для подтверждения</a>",
                    user.emailToken));
        }

        return new UserResponse(dbUser);
    }
}
