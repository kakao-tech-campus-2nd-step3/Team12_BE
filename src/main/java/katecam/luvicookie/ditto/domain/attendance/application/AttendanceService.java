package katecam.luvicookie.ditto.domain.attendance.application;

import katecam.luvicookie.ditto.domain.attendance.dao.AttendanceDatesRepository;
import katecam.luvicookie.ditto.domain.attendance.dao.AttendanceRepository;
import katecam.luvicookie.ditto.domain.attendance.domain.Attendance;
import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDates;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceDateListResponse;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.repository.MemberRepository;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceDatesRepository attendanceDatesRepository;
    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private static final int ATTENDANCE_INTERVAL_MINUTE = 1;

    public void createAttendance(Integer studyId, Integer memberId) {
        AttendanceDates attendanceDates = getAttendanceDatesByStudyIdAndDate(studyId, LocalDateTime.now());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        Attendance attendance = Attendance.builder()
                .attendanceDates(attendanceDates)
                .member(member)
                .build();

        attendanceRepository.save(attendance);
    }

    public void updateAttendance(Integer studyId, Integer memberId, AttendanceUpdateRequest request) {
        AttendanceDates attendanceDates = getAttendanceDatesByStudyIdAndDate(studyId, request.dateTime());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        Attendance attendance = Attendance.builder()
                .attendanceDates(attendanceDates)
                .member(member)
                .build();

        if (request.isAttended()) {
            attendanceRepository.save(attendance);
            return;
        }

        attendanceRepository.delete(attendance);
    }

    public AttendanceDateListResponse getAttendanceDateList(Integer studyId) {
        List<AttendanceDates> attendanceDatesList = attendanceDatesRepository.findAllByStudy_Id(studyId);
        return AttendanceDateListResponse.from(attendanceDatesList);
    }

    private AttendanceDates getAttendanceDatesByStudyIdAndDate(Integer studyId, LocalDateTime attendanceDate) {
        return attendanceDatesRepository.findByStudy_IdAndAttendanceDateBetween(studyId, attendanceDate, attendanceDate.plusMinutes(ATTENDANCE_INTERVAL_MINUTE))
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
        AttendanceDates attendanceDates = getAttendanceDatesByStudyIdAndDate(studyId, attendanceDate);
        attendanceDatesRepository.delete(attendanceDates);
    }

}
