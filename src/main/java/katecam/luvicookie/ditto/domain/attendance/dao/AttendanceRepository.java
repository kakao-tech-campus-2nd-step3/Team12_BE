package katecam.luvicookie.ditto.domain.attendance.dao;

import katecam.luvicookie.ditto.domain.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findAllByMember_IdOrderByIdAsc(Integer memberId);
}
