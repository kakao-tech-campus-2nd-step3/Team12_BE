package katecam.luvicookie.ditto.domain.attendance.dao;

import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceDateRepository extends JpaRepository<AttendanceDate, Integer> {
    List<AttendanceDate> findAllByStudy_IdOrderByStartTimeAsc(Integer studyId);

    @Query(value = "select d from AttendanceDate d " +
            "where d.study.id = :studyId " +
            "and :dateTime = d.startTime ")
    Optional<AttendanceDate> findByStudy_IdAndDateTime(@Param("studyId") Integer studyId, @Param("dateTime") LocalDateTime dateTime);
}
