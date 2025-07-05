package com.poly.beestaycyberknightbackend.exception;

public enum ErrorCode {

    RANK_EXISTED(409, "Rank already exists"),
    RANK_NOT_EXISTED(404, "Rank does not exist"),
    VOUCHER_EXISTED(409, "Voucher code already exists"),
    VOUCHER_NOT_EXISTED(404, "Voucher not found"),
    ROOMTYPE_NOT_EXISTED(404, "Room type not found"),
    DISCOUNT_NOT_EXISTED(404, "Discount not found"),
    USER_NOT_EXISTED(404, "User not found"),
    NOT_LOG(204, "User not logged in"),
    ROLE_NOT_EXISTED(404, "Role not found"),
    FACILITIES_EXISTED(409, "Facility already exists"),
    FACILITIES_NOT_EXISTED(404, "Facility not found"),
    NO_USER_INFORMATION(400, "No user information found"),
    BOOKING_NOT_EXISTED(404, "Booking not found"),
    BOOKINGDETAIL_NOT_EXISTED(404, "BookingDetail not found")
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
