package com.poly.beestaycyberknightbackend.exception;

public enum ErrorCode {

    RANK_EXISTED(409, "Rank already exists"),
    RANK_NOT_EXISTED(404, "Rank not exists"),
    VOUCHER_EXISTED(409, "VoucherCode already exists"),
    VOUCHER_NOT_EXISTED(404, "Voucher not found"),
    ROOMTYPE_NOT_EXISTED(404, "RoomType not found"),
    DISCOUNT_NOT_EXISTED(404, "Discount not found"),
    USER_NOT_EXISTED(404, "User not found"),
    NOT_LOG(204, "Not log")

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
