package ru.aspu.medstat.api.responses;

public class NoticeResponse extends BaseResponse {
    public String message;

    public NoticeResponse(String message) {
        this.type = "notice";
        this.message = message;
    }
}
