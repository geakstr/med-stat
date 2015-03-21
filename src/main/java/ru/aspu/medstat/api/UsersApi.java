package ru.aspu.medstat.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.aspu.medstat.api.responses.ErrorResponse;
import ru.aspu.medstat.api.responses.NoticeResponse;
import ru.aspu.medstat.api.responses.UserResponse;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.api.responses.IResponse;

@RestController
@RequestMapping("/api/users")
public class UsersApi {
    private static final Logger log = Logger.getLogger(UsersApi.class);

    @Autowired
    private UserRepository repo;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User getUserById(final @PathVariable Long userId) {
        return repo.findOne(userId);
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST, produces = "application/json")
    public IResponse registerUser(final @RequestParam("email") String email,
                                  final @RequestParam("first_name") String firstName,
                                  final @RequestParam("last_name") String lastName,
                                  final @RequestParam("birth_date") Long birthDate,
                                  final @RequestParam("password") String password) {
        if (null != repo.findByEmail(email)) {
            return new NoticeResponse("User with " + email + " email already exist");
        }
        return new UserResponse(repo.save(new User(email, firstName, lastName, birthDate, password)), 201);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public IResponse loginUser(final @RequestParam("email") String email,
                               final @RequestParam("password") String password) {
        final User user = repo.findByEmailAndPassword(email, password);
        if (null == user) {
            return new ErrorResponse("Incorrect credentials", 403);
        }
        return new UserResponse(user);
    }
}
