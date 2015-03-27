package ru.aspu.medstat.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
import ru.aspu.medstat.services.UsersService;
import ru.aspu.medstat.utils.FormatUtils;
import ru.aspu.medstat.utils.PasswordUtils;

@Controller
@RequestMapping("/users")
public class UsersController {
    private static final Logger log = Logger.getLogger(UsersController.class);

    @Autowired
    private UserRepository usersRepo;
    
    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/**", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResponse registerUser(@RequestBody UserRegistrationForm form) {
    	System.out.println("Test");
    	
    	String error = "";
    	
    	User user = usersRepo.findByEmail(form.getEmail());
    	if (null != user) {
    		error += "Пользователь с такой эл. почтой уже зарегистрирован\n";
    		return new ErrorResponse(error);
        }
    	
    	user = new User();
        user.email = form.getEmail();
        user.firstName = form.getFirstName();
        user.lastName = form.getLastName();
        user.password = form.getPassword();
        user.birthDate = form.getBirthDateDay() + "/" + form.getBirthDateMonth() + "/" + form.getBirthDateYear();
        user.phone = FormatUtils.normalizePhoneNumber(form.getPhone());
        user.role = UserRoles.PATIENT;
        user.emailToken = PasswordUtils.generate(32);
    	
        error += usersService.getErrors(user);
        if (error.length() != 0) {
            return new ErrorResponse(error);
        }
        
        usersService.sendMail(user);

        return new UserResponse(usersRepo.save(user));
    }
}
