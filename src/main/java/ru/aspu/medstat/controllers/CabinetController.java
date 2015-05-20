package ru.aspu.medstat.controllers;

import java.security.Principal;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.aspu.medstat.entities.Statistic;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.entities.UserGym;
import ru.aspu.medstat.forms.AdminDetachPacientFromDoctor;
import ru.aspu.medstat.forms.AdminDoctorRegistrationForm;
import ru.aspu.medstat.forms.AdminGetPacientsByDoctor;
import ru.aspu.medstat.forms.AdminSetDoctorToPacientForm;
import ru.aspu.medstat.forms.DoctorAddGymToPacient;
import ru.aspu.medstat.forms.DoctorRemoveGymFromUserForm;
import ru.aspu.medstat.repositories.GymnasticRepository;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.repositories.UsersGymsRepository;
import ru.aspu.medstat.responses.DoctorPacientsResponse;
import ru.aspu.medstat.responses.ErrorResponse;
import ru.aspu.medstat.responses.IResponse;
import ru.aspu.medstat.responses.SuccessResponse;
import ru.aspu.medstat.responses.UserGymCreateResponse;
import ru.aspu.medstat.responses.UserResponse;
import ru.aspu.medstat.services.GymnasticService;
import ru.aspu.medstat.services.MailService;
import ru.aspu.medstat.services.StatisticsService;
import ru.aspu.medstat.services.UsersService;
import ru.aspu.medstat.utils.EmailUtils;
import ru.aspu.medstat.utils.PasswordUtils;

@Controller
@RequestMapping("/lk")
public class CabinetController {
	private static final Logger log = Logger.getLogger(CabinetController.class);

	@Autowired
	private StatisticsService statService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private GymnasticService gymService;

	@Autowired
	private MailService mail;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UsersGymsRepository userGymRepo;
	@Autowired
	private GymnasticRepository gymRepo;

	@RequestMapping("/")
	public String index(Model model, Principal principal) {
		User user = userRepo.findByEmail(principal.getName());

		if (user.role == User.Roles.PATIENT.getValue()) {
			return "redirect:user/";
		} else if (user.role == User.Roles.DOCTOR.getValue()) {
			return "redirect:doctor/";
		} else if (user.role == User.Roles.ADMIN.getValue()) {
			return "redirect:admin/";
		}

		return "redirect:/";
	}

	@RequestMapping("/admin")
	public String adminIndex(Model model) {
		model.addAttribute("AdminDoctorRegistrationForm",
				new AdminDoctorRegistrationForm());
		model.addAttribute("AdminSetDoctorToPacientForm",
				new AdminSetDoctorToPacientForm());
		model.addAttribute("AdminDetachPacientFromDoctor",
				new AdminDetachPacientFromDoctor());
		model.addAttribute("AdminGetPacientsByDoctor", new AdminGetPacientsByDoctor());

		List<User> doctors = userRepo.findAllDoctors();
		long doctorId = !doctors.isEmpty() ? doctors.get(0).id : -2;

		model.addAttribute("usersList", userRepo.findAllNewUsers());
		model.addAttribute("doctorsList", doctors);
		model.addAttribute("doctorPacientsList",
				userRepo.findAllPacientByDoctor(doctorId));

		return "cabinet/admin/index";
	}

	@RequestMapping("/user")
	public String userIndex(Model model, Principal principal) {
		User user = userRepo.findByEmail(principal.getName());
		model.addAttribute("userGyms", gymService.getAllUserGymnastics(user.id));
		model.addAttribute("userStats",
				statService.getAllActualUserStats(user.id));
		model.addAttribute("userId", user.id);
		model.addAttribute("role",
				userRepo.findByEmail(principal.getName()).role);

		return "cabinet/user/index";
	}

	@RequestMapping("/doctor")
	public String doctorIndex(Model model, Principal principal) {
		User doctor = userRepo.findByEmail(principal.getName());

		model.addAttribute("doctorPacients",
				userRepo.findAllPacientByDoctor(doctor.id));
		model.addAttribute("gymsList", gymRepo.findAll());
		model.addAttribute("DoctorRemoveGymFromUserForm",
				new DoctorRemoveGymFromUserForm());
		model.addAttribute("DoctorAddGymToPacient", new DoctorAddGymToPacient());

		return "cabinet/doctor/index";
	}

	@RequestMapping(value = "/admin/doctors", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public IResponse registerDoctor(
			@RequestBody AdminDoctorRegistrationForm form) {
		String error = "";
		if (null != userRepo.findByEmail(form.getEmail())) {
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
		doctor.role = User.Roles.DOCTOR.getValue();
		doctor.wasLogin = true;
		doctor.emailToken = PasswordUtils.generate(32);

		usersService.sendMail(doctor);

		return new UserResponse(userRepo.save(doctor));
	}

	@RequestMapping(value = "/admin/requests", method = RequestMethod.POST)
	public String setDoctorToPacient(
			@ModelAttribute AdminSetDoctorToPacientForm form) {
		User user = userRepo.findOne(form.getPacient());
		user.doctorId = form.getDoctor();
		userRepo.save(user);

		return "redirect:/lk/admin";
	}

	@RequestMapping(value = "/admin/detach", method = RequestMethod.POST)
	public String detachPacintFromDoctor(
			@ModelAttribute AdminDetachPacientFromDoctor form) {
		User pacient = userRepo.findOne(form.getPacient());
		if (pacient.doctorId == form.getDoctor()) {
			pacient.doctorId = -1;
			userRepo.save(pacient);
		}
		return "redirect:/lk/admin";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/admin/getCurDocPacient", method = RequestMethod.POST, 
				consumes = MediaType.APPLICATION_JSON_VALUE, 
				produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public IResponse loadCurrentDoctorPacients(@RequestBody AdminGetPacientsByDoctor form) {
		List<User> pacients = userRepo.findAllPacientByDoctor(form.getDoctor());
		JSONArray json = new JSONArray();
		for (User pacient : pacients) {
			JSONObject jsonPacient = new JSONObject();
			jsonPacient.put("id", pacient.id);
			jsonPacient.put("name", pacient.lastName + ", " + pacient.firstName);
			json.add(jsonPacient);
		}
		return new DoctorPacientsResponse(json);
	}

	@RequestMapping(value = "/user/gym/{gymId}", method = RequestMethod.GET)
	public String getStatsByUserGym(final @PathVariable Long gymId, Model model, Principal principal) {
		User user = userRepo.findByEmail(principal.getName());
		List<Statistic> userStats = statService
				.getAllActualUserStatsByGymnastic(user.id, gymId);
		model.addAttribute("userStats", userStats);
		model.addAttribute("userGyms", gymService.getAllUserGymnastics(user.id));
		model.addAttribute("gymId", gymId);
		model.addAttribute("userId", user.id);
		return "cabinet/user/index";
	}

	@RequestMapping(value = "/doctor/users/{userId}")
	public String getStatsByUser(final @PathVariable Long userId, Model model, Principal principal) {
		User user = userRepo.findOne(userId);
		model.addAttribute("userGyms", gymService.getAllUserGymnastics(user.id));
		model.addAttribute("userStats",
				statService.getAllActualUserStats(user.id));
		model.addAttribute("userId", userId);
		model.addAttribute("role",
				userRepo.findByEmail(principal.getName()).role);
		return "cabinet/user/index";
	}

	@RequestMapping(value = "/doctor/users/")
	public String getStatsForAllUsers(Model model, Principal principal) {
		User doctor = userRepo.findByEmail(principal.getName());
		model.addAttribute("userStats",
				statService.getAllActualUsersStatsByDoctor(doctor.id));
		return "cabinet/user/index";
	}

	@RequestMapping(value = "/doctor/users/{userId}/gym/{gymId}")
	public String getStatsByUserAndGym(final @PathVariable Long userId, final @PathVariable Long gymId, 
						Model model, Principal principal) {
		model.addAttribute("userGyms", gymService.getAllUserGymnastics(userId));
		model.addAttribute("userStats",
				statService.getAllActualUserStatsByGymnastic(userId, gymId));
		model.addAttribute("role",
				userRepo.findByEmail(principal.getName()).role);
		return "cabinet/user/index";
	}

	@RequestMapping(value = "/doctor/gyms/remove", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public IResponse removeGymFromUser(
			@RequestBody DoctorRemoveGymFromUserForm form) {
		userGymRepo.delete(form.getUserGymId());
		return new SuccessResponse();
	}

	@RequestMapping(value = "/doctor/gyms/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public IResponse addGymToUser(@RequestBody DoctorAddGymToPacient form) {
		UserGym ug = new UserGym();
		ug.setUser(userRepo.findOne(form.getUserId()));
		ug.setGymnastic(gymRepo.findOne(form.getGymId()));
		userGymRepo.save(ug);
		return new UserGymCreateResponse(ug.id);
	}
}