package katecam.luvicookie.ditto.domain.attendance.application;

import katecam.luvicookie.ditto.domain.attendance.dao.AttendanceDatesRepository;
import katecam.luvicookie.ditto.domain.attendance.dao.AttendanceRepository;
import katecam.luvicookie.ditto.domain.attendance.domain.Attendance;
import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDates;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.repository.MemberRepository;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceDatesRepository attendanceDatesRepository;
    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private static final int ATTENDANCE_MINUTE_INTERVAL = 1;

    public void createAttendance(Integer studyId, Integer memberId) {
        AttendanceDates attendanceDates = getAttendanceDatesByStudyId(studyId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        Attendance attendance = Attendance.builder()
                .attendanceDates(attendanceDates)
                .member(member)
                .build();

        attendanceRepository.save(attendance);
    }

    private AttendanceDates getAttendanceDatesByStudyId(Integer studyId) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(ATTENDANCE_MINUTE_INTERVAL);
        return attendanceDatesRepository.findByStudy_IdAndAttendanceDateBetween(studyId, start, end)
                .orElseThrow(() -> new GlobalException(ErrorCode.DATE_UNABLE_TO_ATTEND));
    }

    public void createAttendanceDate(Integer studyId, LocalDateTime attendanceDate) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));

        AttendanceDates attendanceDates = AttendanceDates.builder()
                .study(study)
                .attendanceDate(attendanceDate)
                .build();

        attendanceDatesRepository.save(attendanceDates);
    }

    public void deleteAttendanceDate(Integer studyId, LocalDateTime attendanceDate) {
        AttendanceDates attendanceDates = attendanceDatesRepository.findByStudy_IdAndAttendanceDateBetween(studyId, attendanceDate, attendanceDate.plusMinutes(ATTENDANCE_MINUTE_INTERVAL))
                .orElseThrow(() -> new GlobalException(ErrorCode.DATE_UNABLE_TO_ATTEND));

        attendanceDatesRepository.delete(attendanceDates);
    }

}
