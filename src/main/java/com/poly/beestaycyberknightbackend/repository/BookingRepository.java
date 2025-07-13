package com.poly.beestaycyberknightbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.poly.beestaycyberknightbackend.domain.Booking;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByBookingStatus(String status);

    long count();

    long countByCheckInDateBetween(LocalDateTime start, LocalDateTime end);

    long countByCheckOutDateBetween(LocalDateTime start, LocalDateTime end);

    List<Booking> findByCheckInDateBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = """
                    SELECT YEAR(b.booking_date) AS BookingYear, NULL AS BookingMonth, SUM(b.total_amount) AS Revenue, 'TOTAL YEAR' AS Type FROM Bookings b
            WHERE YEAR(b.booking_date) = :year
            GROUP BY YEAR(b.booking_date)
            UNION ALL
            SELECT YEAR(b.booking_date) AS BookingYear, MONTH(b.booking_date) AS BookingMonth, SUM(b.total_amount) AS TOTAL,'TOTAL MONTH' AS Type FROM Bookings b
            WHERE YEAR(b.booking_date) = :year
            GROUP BY MONTH(b.booking_date), YEAR(b.booking_date)
            ORDER BY BookingMonth;
                """, nativeQuery = true)
    List<Object[]> getRevenueByYearAndMonthForYear(String year);

    @Query(value = """
                 SELECT COALESCE((SUM(DISTINCT rt.price * bd.quantity) * b.numberOfNights), 0) + COALESCE(SUM(DISTINCT f.price * bf.quanlity), 0) AS totalamount
                 FROM RoomTypes rt JOIN BookingDetail bd ON rt.id = bd.room_type_id
            JOIN Bookings b ON bd.booking_id = b.id
            LEFT JOIN BookingFacilities bf ON b.id = bf.booking_id
            LEFT JOIN Facilities f ON bf.facility_id = f.id
            WHERE b.id = :bookingId
            GROUP BY b.id, b.numberOfNights
                                   """, nativeQuery = true)
    Integer sumTotalPrice(Long bookingId);

    @Query(value = """
            SELECT COUNT(DISTINCT r.id) FROM Rooms r JOIN RoomTypes rt on r.roomtype_id = rt.id
            	LEFT JOIN Stays s on r.id = s.room_id
            	LEFT JOIN Bookings b on s.booking_id = b.id
            	WHERE rt.name LIKE CONCAT('%',:nameRoomType,'%')
            	AND (:date NOT BETWEEN b.check_in_date  AND b.check_out_date  OR b.id IS NULL)
            	AND NOT EXISTS(SELECT 1 FROM Stays s JOIN Bookings b on s.booking_id = b.id
            					WHERE s.room_id = r.id AND (:date BETWEEN s.actualcheckin  AND s.actualcheckout))
            	AND r.roomstatus NOT LIKE '%FIX%'
            """, nativeQuery = true)
    long countAvailableRoomsByRoomTypeAndDate(String nameRoomType, LocalDateTime date);

    @Query(value = """
            DECLARE @fromDate DATETIME = :checkIn;
            DECLARE @toDate DATETIME = :checkOut;
            -- 1. Phòng đã bị đặt bởi booking CONFIRMED trùng ngày
            WITH BookedRooms AS (
                SELECT
                    bd.room_type_id AS roomtype_id,
                    SUM(bd.quantity) AS booked_quantity
                FROM Bookings b
                JOIN BookingDetail bd ON b.id = bd.booking_id
                WHERE
                    b.e_booking_status = 'CONFIRMED'  OR b.e_booking_status = 'LATE'
                    AND @fromDate <= b.check_out_date
                    AND @toDate >= b.check_in_date
                GROUP BY bd.room_type_id
            ),

            -- 2. Phòng đang ở NOW, trùng ngày, không nằm trong booking CONFIRMED
            StayNowRooms AS (
                SELECT
                    r.roomtype_id,
                    COUNT(*) AS staynow_quantity
                FROM Rooms r
                JOIN Stays s ON s.room_id = r.id
                WHERE
                    s.staystatus = 'NOW'
                    AND @fromDate <= s.actualcheckout
                    AND @toDate >= s.actualcheckin
                    AND NOT EXISTS (
                        SELECT 1 FROM Bookings b
                        JOIN BookingDetail bd ON b.id = bd.booking_id
                        WHERE
                            b.e_booking_status = 'CONFIRMED'
                            AND bd.room_type_id = r.roomtype_id
                            AND @fromDate <= b.check_out_date
                            AND @toDate >= b.check_in_date
                    )
                GROUP BY r.roomtype_id
            ),

            -- 3. Tổng tất cả phòng theo loại
            AllRooms AS (
                SELECT roomtype_id, COUNT(*) AS total_rooms
                FROM Rooms
                GROUP BY roomtype_id
            ),

            -- 4. Phòng đang bị FIX
            FixRooms AS (
                SELECT roomtype_id, COUNT(*) AS fix_rooms
                FROM Rooms
                WHERE roomstatus = 'FIX'
                GROUP BY roomtype_id
            ),

            -- 5. Phòng đã bị chiếm
            UsedRooms AS (
                SELECT roomtype_id, SUM(booked_quantity) AS used_quantity
                FROM BookedRooms
                GROUP BY roomtype_id

                UNION ALL

                SELECT roomtype_id, SUM(staynow_quantity)
                FROM StayNowRooms
                GROUP BY roomtype_id
            ),

            -- 6. Gộp tổng phòng bị dùng
            UsedRoomSum AS (
                SELECT roomtype_id, SUM(used_quantity) AS total_used_rooms
                FROM UsedRooms
                GROUP BY roomtype_id
            )

            -- 7. Kết quả
            SELECT
                rt.id AS roomtype_id,
                rt.name AS nameroomtype,
                rt.price AS price,
                rt.people_about AS peopleabout,
                rt.size AS size,
                ISNULL(ar.total_rooms, 0) AS total_rooms,
                ISNULL(fr.fix_rooms, 0) AS fix_rooms,
                ISNULL(ur.total_used_rooms, 0) AS used_rooms,
                ISNULL(ar.total_rooms, 0) - ISNULL(fr.fix_rooms, 0) - ISNULL(ur.total_used_rooms, 0) AS available_rooms
            FROM RoomTypes rt
            LEFT JOIN AllRooms ar ON rt.id = ar.roomtype_id
            LEFT JOIN FixRooms fr ON rt.id = fr.roomtype_id
            LEFT JOIN UsedRoomSum ur ON rt.id = ur.roomtype_id;
                                                """, nativeQuery = true)
    List<Object[]> getAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut);

    @Query(value = """
            SELECT DISTINCT rt.name, bd.quantity FROM RoomTypes rt join BookingDetail bd ON rt.id = bd.room_type_id
            join Bookings b ON bd.booking_id = b.id
            left join BookingFacilities bf ON b.id = bf.booking_id
            left join Facilities f ON bf.facility_id = f.id
                  WHERE b.id = :bookingId;
            """, nativeQuery = true)
    List<Object[]> getRoomTypeBooking(long bookingId);

    @Query(value = """
            SELECT DISTINCT f.facilityName, bf.quanlity FROM RoomTypes rt join BookingDetail bd ON rt.id = bd.room_type_id
            join Bookings b ON bd.booking_id = b.id
            left join BookingFacilities bf ON b.id = bf.booking_id
            left join Facilities f ON bf.facility_id = f.id
                  WHERE b.id = :bookingId;
            """, nativeQuery = true)
    List<Object[]> getFacilitiesBooking(long bookingId);

    @Query(value = """
            SELECT COALESCE(SUM(f.price * bf.quanlity), 0) FROM Facilities f JOIN BookingFacilities bf on f.id = bf.facility_id
            					JOIN Bookings b on bf.booking_id = b.id
            				    WHERE b.id = :bookingId;
            """, nativeQuery = true)
    Integer totalPriceFacilitiesByBookingId(Long bookingId);

    @Query(value = """
            SELECT SUM(rt.price) * DATEDIFF(DAY,b.check_in_date,b.check_out_date) FROM RoomTypes rt JOIN BookingDetail bd on rt.id = bd.room_type_id
            						 JOIN Bookings b on bd.booking_id = b.id
            						 WHERE b.id = :bookingId
            						 GROUP BY b.check_in_date, b.check_out_date
            """, nativeQuery = true)
    Integer totalPriceBookingByBookingId(Long bookingId);

    @Query(value = """
                SELECT  COALESCE( SUM( rt.price * (d.discountValue/100) ), 0) FROM Discounts d JOIN discounts_roomtypes drt on d.id = drt.discounts_id
            JOIN RoomTypes rt on drt.roomtype_id = rt.id
            JOIN BookingDetail bd on rt.id = bd.room_type_id
            JOIN Bookings b on bd.booking_id = b.id
            WHERE b.id = :bookingId AND d.startDate < b.booking_date AND b.booking_date < d.endDate AND d.status LIKE 'ACTIVE'
               """, nativeQuery = true)
    Integer totalPriceDiscountEachRoomType(Long bookingId);

    @Query(value = """
               SELECT SUM(rt.price) * DATEDIFF(DAY,s.actualcheckin,s.actualcheckout) FROM RoomTypes rt JOIN Rooms r on rt.id = r.roomtype_id
            JOIN Stays s on r.id = s.room_id
            JOIN Bookings b on s.booking_id = b.id
            WHERE b.id = :bookingId
            GROUP BY s.actualcheckin, s.actualcheckout
               """, nativeQuery = true)
    Integer totalPriceBookingActual(Long bookingId);

    @Query(value = """
            DECLARE @today DATE = :date;
            SELECT b.id FROM Bookings b WHERE CAST(b.check_in_date AS DATE) = DATEADD(DAY, -1, @today) AND b.e_booking_status LIKE 'CONFIRMED'
            """, nativeQuery = true)
    List<Object[]> bookingCheckinLate(LocalDate date);

    @Query(value = """
            SELECT SUM(p.amount) FROM Payment p JOIN Bookings b ON p.booking_id = b.id
					  WHERE b.id = :bookingId
            """, nativeQuery = true)
    Integer totalPaymentofBooking(Long bookingId);

    @Query(value = """
            DECLARE @today1 DATETIME = :today
            SELECT b.* FROM Rooms r JOIN Stays s on r.id = s.room_id
					    JOIN Bookings b on s.booking_id = b.id
		   WHERE b.check_in_date <= @today1 AND @today1 <= b.check_out_date
				 AND r.id = :roomId
            """, nativeQuery = true)
    List<Booking> findBookingByRoomId(Long roomId, LocalDateTime today);
}
