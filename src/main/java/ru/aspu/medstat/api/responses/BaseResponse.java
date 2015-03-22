package ru.aspu.medstat.api.responses;

public class BaseResponse implements IResponse {
    public String type;

    public BaseResponse() {
        this.type = "common";
    }
}
