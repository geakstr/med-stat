package ru.aspu.medstat.controllers;

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
import ru.aspu.medstat.forms.AdminDoctorRegistrationForm;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.responses.ErrorResponse;
import ru.aspu.medstat.responses.IResponse;
import ru.aspu.medstat.responses.UserResponse;
import ru.aspu.medstat.services.MailService;
import ru.aspu.medstat.services.UsersService;
import ru.aspu.medstat.utils.EmailUtils;
import ru.aspu.medstat.utils.PasswordUtils;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger log = Logger.getLogger(AdminController.class);

    @Autowired
    private UserRepository usersRepo;

    @Autowired
    private UsersService usersService;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private MailService mail;

    @RequestMapping("/*")
    public String index(Model model) {
        model.addAttribute("AdminDoctorRegistrationForm", new AdminDoctorRegistrationForm());

        return "admin/index";
    }

    @RequestMapping(value = "/doctors", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResponse registerDoctor(@RequestBody AdminDoctorRegistrationForm form) {
        String error = "";
        if (null != usersRepo.findByEmail(form.getEmail())) {
            error += "Такая эл. почта уже занята\n";
        }
        if (!EmailUtils.validate(form.getEmail())) {
            error += "Невалидный адрес эл. почты. Принимается паттерн вида *@*\n";
        }

        if (error.length() != 0) {
            return new ErrorResponse(error);
        }

        final User doctor = new User();

        doctor.email = form.getEmail();
        doctor.role = UserRoles.DOCTOR;
        doctor.wasLogin = true;
        doctor.emailToken = PasswordUtils.generate(32);

        usersService.sendMail(doctor);

        return new UserResponse(usersRepo.save(doctor));
    }
}
