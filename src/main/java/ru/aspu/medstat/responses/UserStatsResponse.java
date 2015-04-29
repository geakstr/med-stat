package ru.aspu.medstat.responses;

import org.json.simple.JSONObject;

import ru.aspu.medstat.entities.User;

public class UserStatsResponse extends BaseResponse {
    public JSONObject stats;

    public UserStatsResponse(JSONObject stats) {
        this.type = "user_stat";
        this.stats = stats;
    }
}
