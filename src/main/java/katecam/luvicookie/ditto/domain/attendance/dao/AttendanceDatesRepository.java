package katecam.luvicookie.ditto.domain.attendance.dao;

import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceDatesRepository extends JpaRepository<AttendanceDates, Integer> {
    List<AttendanceDates> findAllByStudy_IdOrderByAttendanceDateAsc(Integer studyId);
    Optional<AttendanceDates> findByStudy_IdAndAttendanceDateBetween(Integer studyId, LocalDateTime start, LocalDateTime end);
}
