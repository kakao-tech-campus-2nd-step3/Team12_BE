package katecam.luvicookie.ditto.domain.attendance.dao;

import katecam.luvicookie.ditto.domain.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findAllByMember_IdOrderByIdAsc(Integer memberId);

    Optional<Attendance> findByAttendanceDate_IdAndMember_Id(Integer dateId, Integer memberId);

    Boolean existsByAttendanceDate_IdAndMember_Id(Integer dateId, Integer memberId);
}
