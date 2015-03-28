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
import ru.aspu.medstat.forms.UserRegistrationForm;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.responses.ErrorResponse;
import ru.aspu.medstat.responses.IResponse;
import ru.aspu.medstat.responses.SuccessResponse;
import ru.aspu.medstat.services.UsersService;
import ru.aspu.medstat.utils.FormatUtils;
import ru.aspu.medstat.utils.PasswordUtils;

@Controller
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private UsersService usersService;

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

        UserRegistrationForm form = new UserRegistrationForm();

        form.setPassword(PasswordUtils.generate(6));
        form.setEmail(user.email);
        form.setEmailToken(mailToken);
        form.setAction("/mail/confirm");
    	form.setMethod("post");
        
        model.addAttribute("title", "Подтверждение регистрации");
        model.addAttribute("regform", form);
        model.addAttribute("regformEmailDisabled", true);

        return "mail/confirm";
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResponse confirm(@RequestBody UserRegistrationForm form) {
    	String error = "";
    	
    	final User user = userRepo.findByEmailToken(form.getEmailToken());
    	
    	if (user == null) {
            error += "Такой пользователь не зарегистрирован\n";
        } else if (user.emailApproved == true) {
            error += "Вы уже подтверждали эту эл. почту\n";
        } else {
        	user.firstName = form.getFirstName();
            user.lastName = form.getLastName();
            user.password = form.getPassword();
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
        userRepo.save(user);

        return new SuccessResponse();
    }
}
