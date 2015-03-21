package ru.aspu.medstat.api.responses;

public class NoticeResponse extends BaseResponse {
    public String message;

    public NoticeResponse(String message) {
        this(message, 200);
    }

    public NoticeResponse(String message, int httpCode) {
        super(httpCode);
        this.type = "notice";
        this.message = message;
    }
}
