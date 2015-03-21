package ru.aspu.medstat.api.responses;

import ru.aspu.medstat.entities.User;

public class UserResponse extends BaseResponse {
    public User user;

    public UserResponse(User user) {
        this(user, 200);
    }

    public UserResponse(User user, int httpCode) {
        super(httpCode);
        this.type = "user";
        this.user = user;
    }
}
