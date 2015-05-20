package ru.aspu.medstat.responses;

import org.json.simple.JSONObject;

public class UserStatsResponse extends BaseResponse {
    public JSONObject stats;

    public UserStatsResponse(JSONObject stats) {
        this.type = "user_stat";
        this.stats = stats;
    }
}
