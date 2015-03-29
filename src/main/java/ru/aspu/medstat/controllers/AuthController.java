package ru.aspu.medstat.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.entities.UserRoles;
import ru.aspu.medstat.forms.UserRegistrationForm;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.responses.ErrorResponse;
import ru.aspu.medstat.responses.IResponse;
import ru.aspu.medstat.responses.SuccessResponse;
import ru.aspu.medstat.responses.UserResponse;
import ru.aspu.medstat.services.UsersService;
import ru.aspu.medstat.utils.FormatUtils;
import ru.aspu.medstat.utils.PasswordCrypto;
import ru.aspu.medstat.utils.PasswordUtils;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private static final Logger log = Logger.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersService usersService;

    @RequestMapping("/login")
    public String index(Model model) {
        return "auth/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResponse registerUser(@RequestBody UserRegistrationForm form) {
        String error = "";

        User user = userRepository.findByEmail(form.getEmail());
        if (null != user) {
            error += "Пользователь с такой эл. почтой уже зарегистрирован\n";
            return new ErrorResponse(error);
        }
        user = User.create(form.getEmail(), form.getPassword());
        user.firstName = form.getFirstName();
        user.lastName = form.getLastName();
        user.birthDate = form.getBirthDateDay() + "/" + form.getBirthDateMonth() + "/" + form.getBirthDateYear();
        user.phone = FormatUtils.normalizePhoneNumber(form.getPhone());
        user.role = UserRoles.PATIENT;
        user.emailToken = PasswordUtils.generate(32);

        error += usersService.getErrors(user);
        if (error.length() != 0) {
            return new ErrorResponse(error);
        }

        usersService.sendMail(user);

        return new UserResponse(userRepository.save(user));
    }

    @RequestMapping(value = "/confirm/{mailToken}", method = RequestMethod.GET)
    public String confirm(@PathVariable String mailToken, Model model) {
        final User user = userRepository.findByEmailToken(mailToken);
        if (user == null || (user.emailApproved)) {
            return "redirect:/";
        }

        if (user.role == UserRoles.PATIENT) {
            user.emailApproved = true;
            userRepository.save(user);
            return "redirect:/";
        }

        UserRegistrationForm form = new UserRegistrationForm();

        form.setPassword(PasswordUtils.generate(6));
        form.setEmail(user.email);
        form.setEmailToken(mailToken);
        form.setAction("/auth/confirm");
        form.setMethod("post");

        model.addAttribute("title", "Подтверждение регистрации");
        model.addAttribute("regform", form);
        model.addAttribute("regformEmailDisabled", true);

        return "auth/confirm";
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResponse confirm(@RequestBody UserRegistrationForm form) {
        String error = "";

        final User user = userRepository.findByEmailToken(form.getEmailToken());

        if (user == null) {
            error += "Такой пользователь не зарегистрирован\n";
        } else if (user.emailApproved) {
            error += "Вы уже подтверждали эту эл. почту\n";
        } else {
            user.firstName = form.getFirstName();
            user.lastName = form.getLastName();
            user.password = PasswordCrypto.getInstance().encrypt(form.getPassword());
            user.birthDate = form.getBirthDateDay() + "/" + form.getBirthDateMonth() + "/" + form.getBirthDateYear();
            user.phone = FormatUtils.normalizePhoneNumber(form.getPhone());

            error += usersService.getErrors(user);
            if (error.length() != 0) {
                return new ErrorResponse(error);
            }
        }

        if (error.length() != 0) {
            return new ErrorResponse(error);
        }

        user.emailApproved = true;
        userRepository.save(user);

        return new SuccessResponse();
    }
}
