package ru.aspu.medstat.api.responses;

public class ErrorResponse extends BaseResponse {
    public String message;

    public ErrorResponse(String message) {
        this.type = "error";
        this.message = message;
    }
}
