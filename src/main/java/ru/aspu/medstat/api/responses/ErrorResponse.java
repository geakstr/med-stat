package ru.aspu.medstat.api.responses;

public class ErrorResponse extends BaseResponse {
    public String message;

    public ErrorResponse(String message, int httpCode) {
        super(httpCode);
        this.type = "error";
        this.message = message;
    }
}
