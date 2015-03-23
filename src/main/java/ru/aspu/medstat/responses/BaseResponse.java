package ru.aspu.medstat.responses;

public class BaseResponse implements IResponse {
    public String type;

    public BaseResponse() {
        this.type = "common";
    }
}
