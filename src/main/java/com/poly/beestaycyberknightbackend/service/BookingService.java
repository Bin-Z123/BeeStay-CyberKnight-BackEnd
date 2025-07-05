package com.poly.beestaycyberknightbackend.service;

import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import com.poly.beestaycyberknightbackend.domain.Booking;
import com.poly.beestaycyberknightbackend.domain.BookingDetail;
import com.poly.beestaycyberknightbackend.domain.BookingFacility;
import com.poly.beestaycyberknightbackend.domain.GuestBooking;
import com.poly.beestaycyberknightbackend.domain.InfoGuest;
import com.poly.beestaycyberknightbackend.domain.Stay;
import com.poly.beestaycyberknightbackend.domain.User;
import com.poly.beestaycyberknightbackend.dto.request.BookingDetailRequest;
import com.poly.beestaycyberknightbackend.dto.request.BookingFacilityRequest;
import com.poly.beestaycyberknightbackend.dto.request.BookingRequest;
import com.poly.beestaycyberknightbackend.dto.request.GuestBookingRequest;
import com.poly.beestaycyberknightbackend.dto.request.StayRequest;
import com.poly.beestaycyberknightbackend.dto.response.AvailableRoomDTO;
import com.poly.beestaycyberknightbackend.dto.response.AvailableTypeRoomDTO;
import com.poly.beestaycyberknightbackend.dto.response.BookingDTO;
import com.poly.beestaycyberknightbackend.dto.response.BookingResponse;
import com.poly.beestaycyberknightbackend.dto.response.RoomImageResponse;
import com.poly.beestaycyberknightbackend.exception.AppException;
import com.poly.beestaycyberknightbackend.exception.ErrorCode;
import com.poly.beestaycyberknightbackend.mapper.BookingDetailMapper;
import com.poly.beestaycyberknightbackend.mapper.BookingFacilityMapper;
import com.poly.beestaycyberknightbackend.mapper.BookingMapper;
import com.poly.beestaycyberknightbackend.mapper.GuestBookingMapper;
import com.poly.beestaycyberknightbackend.mapper.InfoGuestMapper;
import com.poly.beestaycyberknightbackend.mapper.RoomImageMapper;
import com.poly.beestaycyberknightbackend.mapper.StayMapper;
import com.poly.beestaycyberknightbackend.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookingService {

    InfoGuestRepository infoGuestRepository;
    BookingRepository bookingRepository;
    BookingMapper bookingMapper;
    GuestBookingMapper guestBookingMapper;
    GuestBookingRepository guestBookingRepository;
    UserRepository userRepository;
    BookingDetailMapper bookingDetailMapper;
    BookingDetailRepository bookingDetailRepository;
    BookingFacilityMapper bookingFacilityMapper;
    BookingFacilityRepository bookingFacilityRepository;
    FacilityRepository facilityRepository;
    StayMapper stayMapper;
    StayRepository stayRepository;
    RoomRepository roomRepository;
    RoomTypeRepository roomTypeRepository;
    InfoGuestMapper infoGuestMapper;
    RoomImageRepository roomImageRepository;
    RoomImageMapper roomImageMapper;

    public List<BookingDTO> getAllBookings() {
        List<Booking> listEntity = bookingRepository.findAll();
        List<BookingDTO> listResponse = listEntity.stream().map(
                list -> bookingMapper.toResponse(list)).collect(Collectors.toList());
        return listResponse;
    }

    @Transactional
    public Booking orderBooking(GuestBookingRequest guestBookingRequest, BookingRequest bookingRequest,
            List<BookingDetailRequest> bookingDetailRequest, List<BookingFacilityRequest> bookingFacilityRequest,
            List<StayRequest> stayRequest) {

        User user = null;
        GuestBooking guestBooking = null;

        if (guestBookingRequest != null) { // Kiểm tra xem có thông tin khách đặt phòng không
            if (guestBookingRepository.findByPhone(guestBookingRequest.getPhone()) != null) {
                guestBooking = guestBookingRepository.findByPhone(guestBookingRequest.getPhone());
            } else {
                guestBooking = guestBookingRepository.save(guestBookingMapper.toGuestBooking(guestBookingRequest));
            }
        } else if (bookingRequest.getUserId() != null) { // Nếu không có thông tin khách đặt phòng, kiểm tra xem có
                                                         // thông tin người dùng không
            user = userRepository.findById(bookingRequest.getUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        } else {
            throw new AppException(ErrorCode.NO_USER_INFORMATION);
        }

        Booking booking = bookingMapper.toBooking(bookingRequest); // Chuyển đổi BookingRequest thành Booking entity
        if (guestBooking != null) { // Nếu có thông tin khách đặt phòng, gán vào booking
            booking.setGuestBooking(guestBooking);
        } else {
            guestBooking = null;
        }

        if (user != null) { // Nếu không có thông tin khách đặt phòng, gán thông tin người dùng vào booking
            booking.setUser(user);
        } else {
            user = null;
        }
        Booking booking1 = bookingRepository.save(booking); // Lưu booking vào cơ sở dữ liệu

        List<BookingDetail> bookingDetails = new ArrayList<>();
        if (bookingDetailRequest != null) { // Kiểm tra xem có thông tin booking detail không
            bookingDetails = bookingDetailRequest.stream() // Sài lambda đổi danh sách BookingDetailRequest thành danh
                                                           // sách BookingDetail
                    .map(detailRequest -> {
                        BookingDetail detail = bookingDetailMapper.toEntity(detailRequest);
                        detail.setBooking(booking1);
                        detail.setRoomType(roomTypeRepository.findById(detailRequest.getRoomTypeId())
                                .orElseThrow(() -> new AppException(ErrorCode.ROOMTYPE_NOT_EXISTED))); // Gán bookingID
                                                                                                       // cho từng
                                                                                                       // booking detail
                        return detail;
                    })
                    .collect(Collectors.toList());
        }
        bookingDetailRepository.saveAll(bookingDetails); // Lưu tất cả cái đó vào cơ sở dữ liệu

        List<BookingFacility> bookingFacilities = new ArrayList<>();
        if (bookingFacilityRequest != null) { // Kiểm tra xem có thông tin booking facility không
            // Sài lambda đổi danh sách BookingFacilityRequest thành danh sách
            // BookingFacility
            bookingFacilities = bookingFacilityRequest.stream()
                    .map(facilityRequest -> {
                        BookingFacility facility = bookingFacilityMapper.toEntity(facilityRequest);
                        facility.setBooking(booking1);
                        facility.setFacility(facilityRepository.findById(facilityRequest.getFacilityId())
                                .orElseThrow(() -> new AppException(ErrorCode.FACILITIES_NOT_EXISTED))); // Lấy facility
                                                                                                         // từ cơ sở dữ
                                                                                                         // liệu
                        return facility;
                    })
                    .collect(Collectors.toList());
            bookingFacilityRepository.saveAll(bookingFacilities);// Lưu tất cả dịch vụ blabla bla vào cơ sở dữ liệu
        }

        if (stayRequest != null) {
            stayRequest.forEach(stayRequestItem -> {
                Stay stayEntity = stayMapper.toEntity(stayRequestItem);
                stayEntity.setBooking(booking1);
                stayEntity.setRoom(roomRepository.findByRoomNumber(stayRequestItem.getRoomNumber()));
                stayEntity.setStayStatus("NOW");
                // Set InfoGuests vào Stay trước khi save
                List<InfoGuest> infoGuests = stayRequestItem.getInfoGuests().stream()
                        .map(infoGuestRequest -> {
                            InfoGuest infoGuest = infoGuestMapper.toEntity(infoGuestRequest);
                            infoGuest.setStay(stayEntity); // Gán lại quan hệ ngược vì Stay đang sài cascade =
                                                           // CascadeType.ALL
                            return infoGuest;
                        }).toList();

                stayEntity.setInfoGuests(infoGuests);

                stayRepository.save(stayEntity); // Lưu Stay vào cơ sở dữ liệu, không cần phải lưu lại
                                                 // InfoGuest vì đã gán quan hệ ngược, và trong Stay đang sài
                                                 // cascade = CascadeType.ALL nên sẽ tự động lưu InfoGuest
            });
        }
        
        //⦁	Tổng tiền booking mới đặt = tổng tiền dịch vụ (nullable) + tổng tiền ở - tiền giảm giá của từng loại phòng / đêm đầu tiên (nullable)
        Integer totalFacilities = bookingRepository.totalPriceFacilitiesByBookingId(booking1.getId());
        Integer totalPriceBooking = bookingRepository.totalPriceBookingByBookingId(booking1.getId());
        Integer totalDiscount = bookingRepository.totalPriceDiscountEachRoomType(booking1.getId());


       Integer totalPrice = totalFacilities + totalPriceBooking - totalDiscount;
       
       Booking booking2 = bookingRepository.findById(booking1.getId())
               .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXISTED));
       booking2.setTotalAmount(totalPrice); // Cập nhật tổng tiền cho booking

       bookingRepository.save(booking2); // Lưu lại booking đã cập nhật tổng tiền
       bookingRepository.flush(); // Đẩy xuống lưu vào trước để lấy lên lại liền


        return bookingRepository.findById(booking2.getId()) // Trả về booking đã được cập nhật
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_EXISTED));
    }

    public List<Booking> getBookingByCheckInDate(LocalDate checkInDate) {
        LocalDateTime startDateTime = checkInDate.atTime(0, 0, 0, 0);
        LocalDateTime end = startDateTime.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        return bookingRepository.findByCheckInDateBetween(startDateTime, end);
    }

    public Long countAvailableRoomsByRoomTypeAndDate(String nameRoomType, LocalDateTime date) {
        return bookingRepository.countAvailableRoomsByRoomTypeAndDate(nameRoomType, date);
    }

    public List<AvailableTypeRoomDTO> getAvailableRooms(LocalDateTime fromDate, LocalDateTime toDate){
        List<Object[]> objs = bookingRepository.getAvailableRooms(fromDate, toDate);

        List<AvailableTypeRoomDTO> roomDTOs = objs.stream().map((Object[] row) -> {
            Long typeId = Long.parseLong(( row[0]).toString());
            String nameRoomType = ((String) row[1]).toString();
            Integer price = Integer.parseInt((row[2]).toString());
            Integer peopleAbout = Integer.parseInt((row[3]).toString());
            Integer size = Integer.parseInt((row[4]).toString());
            Integer totalRooms = Integer.parseInt((row[5]).toString());
            Integer fixRooms = Integer.parseInt((row[6]).toString());
            Integer usedRooms = Integer.parseInt((row[7]).toString());
            Integer availableRooms = Integer.parseInt((row[8]).toString());
            
            List<Object[]> objrooms = roomRepository.getRoomsAvailable(typeId.intValue());
            
                List<AvailableRoomDTO> rooms = objrooms.stream().map((Object[] ObjRoom)-> {
                    String nameRoomType1 =  ((String) ObjRoom[0]).toString();
                    long id = Long.parseLong(( ObjRoom[1]).toString());
                    String roomNumber = ((String) ObjRoom[2]).toString();
                    int roomTypeId = Integer.parseInt((ObjRoom[3]).toString());
                    String roomStatus = ((String) ObjRoom[4]).toString();
                    int floor = Integer.parseInt((ObjRoom[5]).toString());
                    List<RoomImageResponse> roomImage = roomImageRepository.findByRoomId(id).stream().map(image -> roomImageMapper.toRoomImageResponse(image)).collect(Collectors.toList());
                    return new AvailableRoomDTO( nameRoomType1, id, roomNumber, roomTypeId, roomStatus, floor, roomImage);                                                                       
                                                                                        }
                                                                    ).collect(Collectors.toList());
            return new AvailableTypeRoomDTO(typeId, nameRoomType, price, peopleAbout, size , totalRooms, fixRooms, usedRooms, availableRooms, rooms);
        }).collect(Collectors.toList());

        return roomDTOs;
    }

    public BookingDTO getBooking(Long bookingId){
        Booking entity = bookingRepository.findById(bookingId).orElseThrow(()-> new AppException(ErrorCode.BOOKING_NOT_EXISTED));
        BookingDTO resp = bookingMapper.toResponse(entity);

        return resp;
    }

    public BookingDTO updateTotalPriceBooking(Long bookingId){
        Integer totalPriceFacilites = bookingRepository.totalPriceFacilitiesByBookingId(bookingId);
        Integer totalPriceBookingActual = bookingRepository.totalPriceBookingActual(bookingId);
        Integer totalPriceDiscount = bookingRepository.totalPriceDiscountEachRoomType(bookingId);

        if(totalPriceBookingActual == null){
            totalPriceBookingActual = 0;
        }

        Integer totalPrice = totalPriceFacilites + totalPriceBookingActual - totalPriceDiscount;

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()-> new AppException(ErrorCode.BOOKING_NOT_EXISTED));
        booking.setTotalAmount(totalPrice);

        bookingRepository.save(booking);

        return bookingMapper.toResponse(booking);
    }

    @Transactional
    public BookingDTO updateTotalPriceBookingAfter(Long id){
        Integer totalFacilites = bookingRepository.totalPriceFacilitiesByBookingId(id);
        Integer totalBooking = bookingRepository.totalPriceBookingByBookingId(id);
        Integer totalDiscount = bookingRepository.totalPriceDiscountEachRoomType(id);

        Integer TotalPrice = totalFacilites + totalBooking - totalDiscount;

        Booking booking = bookingRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.BOOKING_NOT_EXISTED));
        booking.setTotalAmount(TotalPrice);

        return bookingMapper.toResponse(booking);

    }

}