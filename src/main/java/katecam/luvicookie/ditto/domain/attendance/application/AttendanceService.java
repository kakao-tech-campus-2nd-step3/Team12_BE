package katecam.luvicookie.ditto.domain.attendance.application;

import katecam.luvicookie.ditto.domain.attendance.dao.AttendanceDateRepository;
import katecam.luvicookie.ditto.domain.attendance.dao.AttendanceRepository;
import katecam.luvicookie.ditto.domain.attendance.domain.Attendance;
import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceCodeResponse;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceDateListResponse;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceListResponse;
import katecam.luvicookie.ditto.domain.attendance.dto.response.MemberAttendanceResponse;
import katecam.luvicookie.ditto.domain.member.dao.MemberRepository;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.domain.studymember.application.StudyMemberService;
import katecam.luvicookie.ditto.domain.studymember.dao.StudyMemberRepository;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMember;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMemberRole;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {

    private final AttendanceDateRepository attendanceDateRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyMemberService studyMemberService;
    private final MemberRepository memberRepository;

    @Transactional
    public void createAttendance(Member member, Integer studyId, String code, Integer dateId) {
        studyMemberService.validateStudyMember(studyId, member);

        validateDuplicateAttendance(dateId, member.getId());

        AttendanceDate attendanceDate = attendanceDateRepository.findById(dateId)
                .orElseThrow(() -> new GlobalException(ErrorCode.DATE_NOT_FOUND));

        // 출석 시간대 확인
        if (attendanceDate.isUnableToAttend()) {
            throw new GlobalException(ErrorCode.DATE_UNABLE_TO_ATTEND);
        }

        // 출석 코드 확인
        if (attendanceDate.isDifferentCode(code)) {
            throw new GlobalException(ErrorCode.CODE_MISMATCH);
        }

        Attendance attendance = Attendance.builder()
                .attendanceDate(attendanceDate)
                .member(member)
                .build();

        attendanceRepository.save(attendance);
    }

    public AttendanceListResponse getAttendanceList(Member member, Integer studyId, Integer memberId) {
        studyMemberService.validateStudyLeader(studyId, member);

        List<StudyMember> studyMemberList = new ArrayList<>();

        // 조회할 회원이 없으면 스터디원 전체를 조회
        if (memberId == null) {
            studyMemberList = studyMemberRepository.findAllByStudyIdAndRoleIn(studyId, Arrays.asList(StudyMemberRole.LEADER, StudyMemberRole.MEMBER));
        }
        else {
            studyMemberList.add(
                    studyMemberRepository.findByStudyIdAndMember_Id(studyId, memberId)
                            .orElseThrow(() -> new GlobalException(ErrorCode.STUDY_MEMBER_NOT_FOUND))
            );
        }

        List<AttendanceDate> attendanceDateList = attendanceDateRepository.findAllByStudy_IdOrderByStartTimeAsc(studyId);
        Map<Integer, MemberAttendanceResponse> memberAttendances = new HashMap<>();

        // 해당 스터디에 대한 회원 출석 필터링
        for (StudyMember studyMember : studyMemberList) {
            List<LocalDateTime> memberAttendanceList = attendanceRepository.findAllByMember_IdOrderByIdAsc(memberId)
                    .stream()
                    .filter(attendance -> attendanceDateList.contains(attendance.getAttendanceDate()))
                    .map(Attendance::getCreatedAt)
                    .filter(dateTime -> studyMember.getJoinedAt()
                            .isBefore(dateTime))
                    .toList();

            Integer foundMemberId = studyMember.getMember()
                    .getId();
            memberAttendances.put(foundMemberId, MemberAttendanceResponse.from(attendanceDateList, memberAttendanceList));
        }

        return AttendanceListResponse.from(attendanceDateList, memberAttendances);
    }

    @Transactional
    public void updateAttendance(Member member, Integer studyId, Integer memberId, LocalDateTime startTime, Boolean isAttended) {
        studyMemberService.validateStudyLeader(studyId, member);

        Member targetMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        studyMemberService.validateStudyMember(studyId, targetMember);

        AttendanceDate attendanceDate = getAttendanceDateByStudyIdAndTime(studyId, startTime);

        Attendance attendance = Attendance.builder()
                .attendanceDate(attendanceDate)
                .member(targetMember)
                .build();

        if (isAttended) {
            validateDuplicateAttendance(attendanceDate.getId(), memberId);
            attendanceRepository.save(attendance);
            return;
        }

        // 출석 여부 확인
        if (!attendanceRepository.existsByAttendanceDate_IdAndMember_Id(attendanceDate.getId(), memberId)) {
            throw new GlobalException(ErrorCode.NOT_ATTENDED);
        }

        attendanceRepository.delete(attendance);
    }

    private void validateDuplicateAttendance(Integer dateId, Integer memberId) {
        Boolean isAttended = attendanceRepository.existsByAttendanceDate_IdAndMember_Id(dateId, memberId);
        if (isAttended) {
            throw new GlobalException(ErrorCode.ALREADY_ATTENDED);
        }
    }

    public AttendanceDateListResponse getAttendanceDateList(Member member, Integer studyId) {
        studyMemberService.validateStudyMember(studyId, member);
        List<AttendanceDate> attendanceDateList = attendanceDateRepository.findAllByStudy_IdOrderByStartTimeAsc(studyId);
        return AttendanceDateListResponse.from(attendanceDateList);
    }

    private AttendanceDate getAttendanceDateByStudyIdAndTime(Integer studyId, LocalDateTime startTime) {
        return attendanceDateRepository.findByStudy_IdAndDateTime(studyId, startTime)
                .orElseThrow(() -> new GlobalException(ErrorCode.DATE_UNABLE_TO_ATTEND));
    }

    @Transactional
    public void createAttendanceDate(Member member, Integer studyId, LocalDateTime startTime, Integer intervalMinutes) {
        studyMemberService.validateStudyLeader(studyId, member);

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new GlobalException(ErrorCode.STUDY_NOT_FOUND));

        LocalDateTime deadline = startTime.plusMinutes(intervalMinutes);

        AttendanceDate attendanceDate = AttendanceDate.builder()
                .study(study)
                .startTime(startTime)
                .deadline(deadline)
                .build();

        attendanceDateRepository.save(attendanceDate);
    }

    @Transactional
    public void deleteAttendanceDate(Member member, Integer studyId, LocalDateTime startTime) {
        studyMemberService.validateStudyLeader(studyId, member);

        AttendanceDate attendanceDate = getAttendanceDateByStudyIdAndTime(studyId, startTime);
        attendanceDateRepository.delete(attendanceDate);
    }

    public AttendanceCodeResponse getAttendanceCode(Member member, Integer studyId, Integer dateId) {
        studyMemberService.validateStudyLeader(studyId, member);

        AttendanceDate attendanceDate = attendanceDateRepository.findById(dateId)
                .orElseThrow(() -> new GlobalException(ErrorCode.DATE_NOT_FOUND));

        return AttendanceCodeResponse.from(attendanceDate.getCode());
    }

}
