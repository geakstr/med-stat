package ru.aspu.medstat.api.responses;

public class BaseResponse implements IResponse {
    public String type;
    public int http_code;

    public BaseResponse(int httpCode) {
        this.type = "common";
        this.http_code = httpCode;
    }
}
