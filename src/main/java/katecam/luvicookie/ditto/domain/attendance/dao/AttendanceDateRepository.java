package katecam.luvicookie.ditto.domain.attendance.dao;

import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceDateRepository extends JpaRepository<AttendanceDate, Integer> {
    List<AttendanceDate> findAllByStudy_IdOrderByAttendanceTimeAsc(Integer studyId);

    @Query(value = "select d from AttendanceDate d " +
            "where d.study.id = :studyId " +
            "and :dateTime >= d.attendanceTime " +
            "and :dateTime <= d.attendanceDeadline ")
    Optional<AttendanceDate> findByStudy_IdAndAttendanceTime(@Param("studyId") Integer studyId, @Param("dateTime") LocalDateTime dateTime);
}
