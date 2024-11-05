package katecam.luvicookie.ditto.domain.attendance.application;

import katecam.luvicookie.ditto.domain.attendance.dao.AttendanceDateRepository;
import katecam.luvicookie.ditto.domain.attendance.dao.AttendanceRepository;
import katecam.luvicookie.ditto.domain.attendance.domain.Attendance;
import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceUpdateRequest;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceDateListResponse;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceListResponse;
import katecam.luvicookie.ditto.domain.attendance.dto.response.MemberAttendanceResponse;
import katecam.luvicookie.ditto.domain.member.dao.MemberRepository;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceDateRepository attendanceDateRepository;
    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    public void createAttendance(Integer studyId, Integer memberId) {
        AttendanceDate attendanceDate = getAttendanceDatesByStudyIdAndTime(studyId, LocalDateTime.now());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        Attendance attendance = Attendance.builder()
                .attendanceDate(attendanceDate)
                .member(member)
                .build();

        attendanceRepository.save(attendance);
    }

    public AttendanceListResponse getAttendanceList(Integer studyId, Integer memberId) {
        List<Integer> memberIds = Arrays.asList(memberId);
        if (memberId == null) {
            memberIds.clear();
            // Todo - 같은 스터디 멤버 ids 삽입
            // Teammate 테이블 필요
        }

        List<AttendanceDate> attendanceDateList = attendanceDateRepository.findAllByStudy_IdOrderByAttendanceTimeAsc(studyId);
        Map<Integer, MemberAttendanceResponse> memberAttendances = new HashMap<>();
        for (Integer id : memberIds) {
            // 해당 스터디에 대한 회원 출석 필터링
            List<Attendance> memberAttendanceList = attendanceRepository.findAllByMember_IdOrderByIdAsc(memberId)
                    .stream()
                    .filter(attendance -> attendanceDateList.contains(attendance.getAttendanceDate()))
                    .toList();

            // Todo - attendanceDatesList를 사용자가 스터디에 가입한 날짜 이후로 필터링
            memberAttendances.put(id, MemberAttendanceResponse.from(attendanceDateList, memberAttendanceList));
        }

        return AttendanceListResponse.from(attendanceDateList, memberAttendances);
    }

    public void updateAttendance(Integer studyId, Integer memberId, LocalDateTime dateTime, Boolean isAttended) {
        AttendanceDate attendanceDate = getAttendanceDatesByStudyIdAndTime(studyId, dateTime);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        Attendance attendance = Attendance.builder()
                .attendanceDate(attendanceDate)
                .member(member)
                .build();

        if (isAttended) {
            attendanceRepository.save(attendance);
            return;
        }

        attendanceRepository.delete(attendance);
    }

    public AttendanceDateListResponse getAttendanceDateList(Integer studyId) {
        List<AttendanceDate> attendanceDateList = attendanceDateRepository.findAllByStudy_IdOrderByAttendanceTimeAsc(studyId);
        return AttendanceDateListResponse.from(attendanceDateList);
    }

    private AttendanceDate getAttendanceDatesByStudyIdAndTime(Integer studyId, LocalDateTime attendanceTime) {
        return attendanceDateRepository.findByStudy_IdAndAttendanceTime(studyId, attendanceTime)
                .orElseThrow(() -> new GlobalException(ErrorCode.DATE_UNABLE_TO_ATTEND));
    }

    public void createAttendanceDate(Integer studyId, LocalDateTime attendanceTime, Integer intervalMinutes) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));

        LocalDateTime attendanceDeadline = attendanceTime.plusMinutes(intervalMinutes);

        AttendanceDate attendanceDate = AttendanceDate.builder()
                .study(study)
                .attendanceTime(attendanceTime)
                .attendanceDeadline(attendanceDeadline)
                .build();

        attendanceDateRepository.save(attendanceDate);
    }

    public void deleteAttendanceDate(Integer studyId, LocalDateTime attendanceDate) {
        AttendanceDate attendanceDates = getAttendanceDatesByStudyIdAndTime(studyId, attendanceDate);
        attendanceDateRepository.delete(attendanceDates);
    }

}
