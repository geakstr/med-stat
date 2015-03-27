package ru.aspu.medstat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.entities.UserRoles;
import ru.aspu.medstat.forms.MailConfirmationForm;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.responses.ErrorResponse;
import ru.aspu.medstat.responses.IResponse;
import ru.aspu.medstat.responses.SuccessResponse;
import ru.aspu.medstat.utils.PasswordUtils;

@Controller
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private UserRepository userRepo;

    @RequestMapping(value = "/confirm/{mailToken}", method = RequestMethod.GET)
    public String confirm(@PathVariable String mailToken, Model model) {
        final User user = userRepo.findByEmailToken(mailToken);
        if (user == null || (user.emailApproved)) {
            return "redirect:/";
        }

        if (user.role == UserRoles.PATIENT) {
            user.emailApproved = true;
            userRepo.save(user);
            return "redirect:/";
        }

        MailConfirmationForm form = new MailConfirmationForm();

        form.setPassword(PasswordUtils.generate(6));
        form.setEmailToken(mailToken);
        model.addAttribute("MailConfirmationForm", form);

        return "mail/confirm";
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResponse confirm(@RequestBody MailConfirmationForm form) {
        String error = "";

        if (form.getPassword().trim().length() < 6) {
            error += "Не допускается пароль меньше 6 символов длиной\n";
        }

        if (form.getEmailToken().trim().length() == 0) {
            error += "Неверный токен регистрации\n";
        }

        final User user = userRepo.findByEmailToken(form.getEmailToken());
        if (user == null) {
            error += "Такой пользователь не зарегистрирован\n";
        } else if (user.emailApproved == true) {
            error += "Вы уже подтверждали эту эл. почту\n";
        }

        if (error.length() != 0) {
            return new ErrorResponse(error);
        }

        user.emailApproved = true;
        userRepo.save(user);

        return new SuccessResponse();
    }
}
