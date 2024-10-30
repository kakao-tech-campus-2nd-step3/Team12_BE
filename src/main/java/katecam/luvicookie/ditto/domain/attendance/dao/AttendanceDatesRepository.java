package katecam.luvicookie.ditto.domain.attendance.dao;

import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AttendanceDatesRepository extends JpaRepository<AttendanceDates, Integer> {
    Optional<AttendanceDates> findByStudy_IdAndAttendanceDateBetween(Integer studyId, LocalDateTime start, LocalDateTime end);
}
