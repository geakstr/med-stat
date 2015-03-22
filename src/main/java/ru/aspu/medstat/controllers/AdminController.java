package ru.aspu.medstat.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aspu.medstat.api.responses.ErrorResponse;
import ru.aspu.medstat.api.responses.IResponse;
import ru.aspu.medstat.api.responses.UserResponse;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.entities.UserRoles;
import ru.aspu.medstat.forms.UserRegistrationForm;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.utils.EmailUtil;
import ru.aspu.medstat.utils.PasswordUtil;
import ru.aspu.medstat.utils.TelephoneUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger log = Logger.getLogger(AdminController.class);


    @Autowired
    private UserRepository repo;

    private SimpleDateFormat birthDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping("/")
    public String index(Model model) {
        UserRegistrationForm form = new UserRegistrationForm();
        form.setPassword(PasswordUtil.generate(6));
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
        if (!EmailUtil.validate(form.getEmail())) {
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
        if (form.getPassword().length() < 6) {
            error += "Допускается пароль длиной от 6 символов\n";
        }

        try {
            birthDateFormat.parse(form.getBirthDate());
        } catch (ParseException e) {
            error += "Неверный формат даты рождения. Ожидается dd-MM-yyyy\n";
        }

        if (error.length() != 0) {
            return new ErrorResponse(error);
        }

        form.setTelephone(TelephoneUtil.normalize(form.getTelephone()));

        final User user = repo.save(
                new User(
                        form.getEmail(),
                        form.getFirstName(),
                        form.getLastName(),
                        form.getBirthDate(),
                        form.getPassword(),
                        form.getTelephone(),
                        form.getRole()
                )
        );

        return new UserResponse(user);
    }

    @RequestMapping(value = "/generate-password/{len}", method = RequestMethod.GET)
    @ResponseBody
    public String generatePassword(final @PathVariable int len) {
        return PasswordUtil.generate(len);
    }
}
