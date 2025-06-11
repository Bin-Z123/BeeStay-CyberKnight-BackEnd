package com.poly.beestaycyberknightbackend.exception;

public enum ErrorCode {
    RANK_EXISTED(409, "Rank already exists"),
    RANK_NOT_EXISTED(404, "Rank not exists")
    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
