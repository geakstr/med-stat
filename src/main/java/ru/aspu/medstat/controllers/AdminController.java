package ru.aspu.medstat.controllers;

import java.util.ArrayList;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.entities.UserRoles;
import ru.aspu.medstat.forms.AdminDoctorRegistrationForm;
import ru.aspu.medstat.forms.AdminSetDoctorToPacientForm;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.responses.ErrorResponse;
import ru.aspu.medstat.responses.IResponse;
import ru.aspu.medstat.responses.UserResponse;
import ru.aspu.medstat.services.MailService;
import ru.aspu.medstat.utils.EmailUtils;
import ru.aspu.medstat.utils.PasswordUtils;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger log = Logger.getLogger(AdminController.class);

    @Autowired
    private UserRepository repo;

    @Autowired
    private MailService mail;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("AdminDoctorRegistrationForm", new AdminDoctorRegistrationForm());
        model.addAttribute("AdminSetDoctorToPacientForm", new AdminSetDoctorToPacientForm());
    	
        model.addAttribute("usersList", repo.findAllNewUsers());
        model.addAttribute("doctorsList", repo.findAllDoctors());
    	
        return "admin/index";
    }

    @RequestMapping(value = "/doctors", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResponse registerDoctor(@RequestBody AdminDoctorRegistrationForm form) {
        String error = "";
        if (null != repo.findByEmail(form.getEmail())) {
            error += "Пользователь с такой эл. почтой уже зарегистрирован\n";
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

        mail.send(doctor.email, "Медицинский портал АГУ. Регистрация", String.format(
                "<a href=\"http://localhost:8080/mail/confirm/%s\">Нажмите сюда для окончания регистрации</a>",
                doctor.emailToken));

        return new UserResponse(repo.save(doctor));
    }
    
    
	@RequestMapping(value = "/requests", method = RequestMethod.POST)
    @ResponseBody
    public IResponse setDoctorToPacient(@ModelAttribute AdminSetDoctorToPacientForm form, @ModelAttribute ArrayList<User> usersList, Model model) {
		final User user = repo.findOne(form.getPacient());
		if (null == user) {
			return new ErrorResponse("Пациент не выбран\n");
		}
		user.doctorId = form.getDoctor();

		for (ListIterator<User> iter = usersList.listIterator(); iter.hasNext();) {
			User someUser = iter.next();
			if (user.equals(someUser)) {
				iter.remove();
			}
		}
		
		return new UserResponse(repo.save(user));
    }
}
