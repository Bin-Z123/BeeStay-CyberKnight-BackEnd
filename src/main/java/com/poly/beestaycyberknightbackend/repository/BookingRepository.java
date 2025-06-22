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
                        SELECT (SUM(rt.price) + SUM(f.price)) AS TOTAL FROM RoomTypes rt join BookingDetail bd ON rt.id = bd.room_type_id
            						join Bookings b ON bd.booking_id = b.id
            						join BookingFacilities bf ON b.id = bf.booking_id
            						join Facilities f ON bf.facility_id = f.id
                        WHERE b.id = :bookingId;
                        """, nativeQuery = true)
    int sumTotalPrice(Long bookingId);


    @Query(value = """
        SELECT COUNT(DISTINCT r.id) FROM Rooms r JOIN RoomTypes rt on r.roomtype_id = rt.id
									LEFT JOIN Stays s on r.id = s.room_id
									LEFT JOIN Bookings b on s.booking_id = b.id
									WHERE rt.name LIKE CONCAT('%',:nameRoomType,'%') 
									AND (:date NOT BETWEEN CAST(b.check_in_date AS DATE) AND CAST(b.check_out_date AS DATE) OR b.id IS NULL)
									AND NOT EXISTS(SELECT 1 FROM Stays s JOIN Bookings b on s.booking_id = b.id 
													WHERE s.room_id = r.id AND (:date BETWEEN CAST(s.actualcheckin AS DATE) AND CAST(s.actualcheckout AS DATE)))
									AND r.roomstatus NOT LIKE '%FIX%'
        """, nativeQuery = true)
    long countAvailableRoomsByRoomTypeAndDate(String nameRoomType, LocalDate date);
}
