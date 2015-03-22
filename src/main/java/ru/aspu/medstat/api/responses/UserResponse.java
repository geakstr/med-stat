package ru.aspu.medstat.api.responses;

import ru.aspu.medstat.entities.User;

public class UserResponse extends BaseResponse {
    public User user;

    public UserResponse(User user) {
        this.type = "user";
        this.user = user;
    }
}
