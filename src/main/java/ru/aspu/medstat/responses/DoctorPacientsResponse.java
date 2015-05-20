package ru.aspu.medstat.responses;

import org.json.simple.JSONArray;

public class DoctorPacientsResponse extends BaseResponse {
	public JSONArray pacients;

    public DoctorPacientsResponse(JSONArray pacients) {
        this.type = "doctor_pacients";
        this.pacients = pacients;
    }
}