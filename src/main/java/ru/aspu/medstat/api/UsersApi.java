package ru.aspu.medstat.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.responses.ErrorResponse;
import ru.aspu.medstat.responses.IResponse;
import ru.aspu.medstat.responses.UserResponse;

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

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    public IResponse loginUser(final @RequestParam("email") String email,
                               final @RequestParam("password") String password) {
        final User user = repo.findByEmailAndPassword(email, password);
        if (null == user) {
            return new ErrorResponse("Incorrect credentials");
        }
        return new UserResponse(user);
    }
}