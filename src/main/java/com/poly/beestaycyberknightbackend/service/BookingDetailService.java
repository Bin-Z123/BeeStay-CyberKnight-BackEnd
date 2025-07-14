package com.poly.beestaycyberknightbackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.BookingDetail;
import com.poly.beestaycyberknightbackend.domain.RoomType;
import com.poly.beestaycyberknightbackend.dto.request.BookingDetailRequest;
import com.poly.beestaycyberknightbackend.dto.request.BookingDetailUpdateRequest;
import com.poly.beestaycyberknightbackend.dto.response.BookingDetailDTO;
import com.poly.beestaycyberknightbackend.dto.response.RoomTypeDTO;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.BookingDetailMapper;
import com.poly.beestaycyberknightbackend.mapper.RoomTypeMapper;
import com.poly.beestaycyberknightbackend.repository.BookingDetailRepository;
import com.poly.beestaycyberknightbackend.repository.BookingRepository;
import com.poly.beestaycyberknightbackend.repository.RoomTypeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookingDetailService {
    BookingDetailRepository bookingDetailRepository;
    BookingRepository bookingRepository;
    RoomTypeRepository roomTypeRepository;
    BookingDetailMapper bookingDetailMapper;

    @Transactional
    public List<BookingDetailDTO> updateBookingDetails(Long bookingId, List<BookingDetailUpdateRequest> requestItems) {
        // 1. Kiểm tra xem Booking cha có tồn tại không.
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXISTED));

        // --- TỐI ƯU HÓA TRUY VẤN ---

        // 2. Lấy tất cả ID cần thiết từ danh sách request để truy vấn một lần
        List<Long> roomTypeIds = requestItems.stream()
                .map(BookingDetailUpdateRequest::getRoomTypeId)
                .distinct()
                .collect(Collectors.toList());

        List<Long> existingDetailIds = requestItems.stream()
                .filter(req -> req.getId() != null)
                .map(BookingDetailUpdateRequest::getId)
                .collect(Collectors.toList());

        // 3. Truy vấn DB MỘT LẦN DUY NHẤT cho mỗi loại đối tượng và đưa vào Map để tra
        // cứu nhanh
        Map<Long, RoomType> roomTypeMap = roomTypeRepository.findAllById(roomTypeIds).stream()
                .collect(Collectors.toMap(RoomType::getId, rt -> rt));

        Map<Long, BookingDetail> existingDetailsMap = bookingDetailRepository.findAllById(existingDetailIds).stream()
                .collect(Collectors.toMap(BookingDetail::getId, bd -> bd));

        // --- KẾT THÚC TỐI ƯU HÓA ---

        List<BookingDetail> bookingDetailsToSave = new ArrayList<>();

        // 4. Lặp qua danh sách request để xử lý logic
        for (BookingDetailUpdateRequest req : requestItems) {
            RoomType roomType = roomTypeMap.get(req.getRoomTypeId());
            if (roomType == null) {
                throw new AppException(ErrorCode.ROOMTYPE_NOT_EXISTED);
            }

            BookingDetail bookingDetail;

            if (req.getId() != null) { // --- Xử lý UPDATE ---
                bookingDetail = existingDetailsMap.get(req.getId());
                if (bookingDetail == null) {
                    throw new AppException(ErrorCode.BOOKINGDETAIL_NOT_EXISTED);
                }
            } else { // --- Xử lý CREATE ---
                bookingDetail = new BookingDetail();
                bookingDetail.setBooking(booking);
            }

            // Cập nhật các trường chung
            bookingDetail.setRoomType(roomType);
            bookingDetail.setQuantity(req.getQuantity());

            bookingDetailsToSave.add(bookingDetail);
        }

        // 5. Gọi saveAll MỘT LẦN DUY NHẤT để lưu tất cả thay đổi
        List<BookingDetail> savedEntities = bookingDetailRepository.saveAll(bookingDetailsToSave);

        // 6. Chuyển đổi kết quả sang DTO để trả về
        return savedEntities.stream()
                .map(bookingDetailMapper::toResponse)
                .collect(Collectors.toList());
    }
}