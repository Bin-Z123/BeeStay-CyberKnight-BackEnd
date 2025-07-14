package com.poly.beestaycyberknightbackend.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.beestaycyberknightbackend.domain.Stay;

@Repository
public interface StayRepository extends JpaRepository<Stay, Integer> {
    @Query("SELECT s FROM Stay s WHERE s.booking.id = :bId")
    List<Stay> listStayOfBooking(@Param("bId") Long bId);

    @Query(value = """
            DECLARE @today DATETIME = :today
            SELECT s.* FROM Rooms r JOIN Stays s on r.id = s.room_id
		   WHERE s.actualcheckin <= @today AND @today <= s.actualcheckout
				 AND r.id = :roomId
            """, nativeQuery = true)
    List<Stay> findStayByRoomId(Long roomId, LocalDateTime today);
}
