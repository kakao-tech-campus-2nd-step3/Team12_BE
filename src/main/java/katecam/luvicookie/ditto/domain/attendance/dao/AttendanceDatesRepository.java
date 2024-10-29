package katecam.luvicookie.ditto.domain.attendance.dao;

import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceDatesRepository extends JpaRepository<AttendanceDates, Integer> {
}
