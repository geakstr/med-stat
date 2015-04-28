package ru.aspu.medstat.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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

import ru.aspu.medstat.entities.Message;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.forms.UserCreateMessageForm;
import ru.aspu.medstat.forms.UserRemoveMessageForm;
import ru.aspu.medstat.repositories.MessagesRepository;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.responses.ErrorResponse;
import ru.aspu.medstat.responses.IResponse;
import ru.aspu.medstat.responses.SuccessResponse;

@Controller
@RequestMapping("/messages")
public class MessagingController {
	private static final Logger log = Logger.getLogger(CabinetController.class);

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private MessagesRepository messageRepo;

	public Model setModelAttributes(User user, Model model, Principal principal) {
		model.addAttribute("UserRemoveMessageForm", new UserRemoveMessageForm());
		model.addAttribute("UserCreateMessageForm", new UserCreateMessageForm());
		List<User> usersAddresses = new ArrayList<>();
		if (user.role == User.Roles.DOCTOR.getValue()) {
			usersAddresses = userRepo.findAllPacientByDoctor(user.id);
		} else if (user.role == User.Roles.PATIENT.getValue()) {
			usersAddresses.add(userRepo.findOne(user.doctorId));
		}
		model.addAttribute("toUsersList", usersAddresses);
		model.addAttribute("unreadMessagesCount", messageRepo.countUnreadUserMessages(user.id));
		return model;
	}

	@RequestMapping(value = { "/", "/new" })
	public String index(Model model, Principal principal) {
		User user = userRepo.findByEmail(principal.getName());
		model.addAttribute("messages", messageRepo.findAllByToUser(user.id));
		setModelAttributes(user, model, principal);
		return "messages/index";
	}

	@RequestMapping("/posted")
	public String getPosted(Model model, Principal principal) {
		User user = userRepo.findByEmail(principal.getName());
		model.addAttribute("messages", messageRepo.findAllByFromUser(user.id));
		setModelAttributes(user, model, principal);
		return "messages/index";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public IResponse removeMessage(@RequestBody UserRemoveMessageForm form, 
								   Principal principal) {
		User user = userRepo.findByEmail(principal.getName());
		Message message = messageRepo.findOne(form.getMessageId());
		
		if (message == null) {
			return new ErrorResponse("Message delete processing faild: access denied");
		}
		
		boolean messageInFrom = message.fromUser.equals(user);
		boolean messageInTo = message.toUser.equals(user);
		
		if (messageInFrom) {
			message.deleteFrom = 1;
		}
		if (messageInTo) {
			message.deleteTo = 1;
		}
		if (!messageInTo && !messageInFrom) {
			return new ErrorResponse("Message delete processing faild: access denied");
		}
		messageRepo.save(message);
		return new SuccessResponse();
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createMessage(final HttpServletRequest request, 
								@ModelAttribute UserCreateMessageForm form,
								Principal principal) {
		User user = userRepo.findByEmail(principal.getName());
		
		if (user.role == User.Roles.PATIENT.getValue()) {
			if (form.getToUserId() != user.doctorId) {
				return "messages/errors/brokenUserIdFound";
			}
		} else if (user.role == User.Roles.DOCTOR.getValue()) {
			User pacient = userRepo.findOne(form.getToUserId());
			if (pacient == null || pacient.doctorId != user.id) {
				return "messages/errors/brokenUserIdFound";
			}
		}
		
		if (!"".equals(form.getMessage())) {
			Message message = new Message();
			message.fromUser = user;
			message.toUser = userRepo.findOne(form.getToUserId());
			message.header = form.getHeader();
			message.message = form.getMessage();
			messageRepo.save(message);
		}

		String referer = request.getHeader("referer");
		return "redirect:"+referer;
	}
	
	@RequestMapping(value = "/view/{messageId}", method = RequestMethod.GET)
	public String viewMessage(final HttpServletRequest request,
							  final @PathVariable Long messageId,
							  Model model, Principal principal) {
		User user = userRepo.findByEmail(principal.getName());
		Message message = messageRepo.findOne(messageId);
		
		if (message == null) {
			return "messages/errors/brokenMessageIdFound";
		}
		
		boolean isFrom = message.fromUser.equals(user);
		boolean isTo = message.toUser.equals(user);
		
		if (!isFrom && !isTo) {
			return "messages/errors/brokenMessageIdFound";
		}
		if (isTo && message.isRead != 1) {
			message.isRead = 1;
			messageRepo.save(message);
		}
		model.addAttribute("UserCreateMessageForm", new UserCreateMessageForm());
		model.addAttribute("message", message);
		return "messages/details";
	}
}