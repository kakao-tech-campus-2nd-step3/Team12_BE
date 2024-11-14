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

        // 스터디원 출석 필터링
        // 스터디에 참여한 날짜 이후 출석일자만 처리
        for (StudyMember studyMember : studyMemberList) {
            Integer targetMemberId = studyMember.getMember()
                    .getId();

            List<LocalDateTime> memberAttendanceList = attendanceRepository.findAllByMember_IdOrderByIdAsc(targetMemberId)
                    .stream()
                    .filter(attendance -> attendanceDateList.contains(attendance.getAttendanceDate()))
                    .filter(attendance -> studyMember.getJoinedAt()
                            .isBefore(attendance.getCreatedAt()))
                    .map(attendance -> attendance.getAttendanceDate()
                            .getStartTime())
                    .toList();

            memberAttendances.put(targetMemberId, MemberAttendanceResponse.from(attendanceDateList, memberAttendanceList));
        }

        return AttendanceListResponse.from(attendanceDateList, memberAttendances);
    }

    @Transactional
    public void updateAttendance(Member member, Integer studyId, Integer memberId, Integer dateId, Boolean isAttended) {
        studyMemberService.validateStudyLeader(studyId, member);

        Member targetMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND));

        studyMemberService.validateStudyMember(studyId, targetMember);

        AttendanceDate attendanceDate = attendanceDateRepository.findById(dateId)
                .orElseThrow(() -> new GlobalException(ErrorCode.DATE_NOT_FOUND));

        if (isAttended) {
            validateDuplicateAttendance(attendanceDate.getId(), memberId);

            Attendance attendance = Attendance.builder()
                    .attendanceDate(attendanceDate)
                    .member(targetMember)
                    .build();

            attendanceRepository.save(attendance);
            return;
        }

        // 출석 여부 확인
        Attendance attendance = attendanceRepository.findByAttendanceDate_IdAndMember_Id(attendanceDate.getId(), memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_ATTENDED));

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

    @Transactional
    public void createAttendanceDate(Member member, Integer studyId, LocalDateTime startTime, Integer intervalMinutes) {
        studyMemberService.validateStudyLeader(studyId, member);

        validateAttendanceDateTime(startTime);

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

    private void validateAttendanceDateTime(LocalDateTime startTime) {
        boolean isBeforeNow = startTime.isBefore(LocalDateTime.now());
        if (isBeforeNow) {
            throw new GlobalException(ErrorCode.INVALID_ATTENDANCE_DATE);
        }
    }

    @Transactional
    public void updateAttendanceDate(Member member, Integer studyId, Integer dateId, LocalDateTime startTime, Integer intervalMinutes) {
        studyMemberService.validateStudyLeader(studyId, member);

        validateAttendanceDateTime(startTime);

        AttendanceDate attendanceDate = attendanceDateRepository.findById(dateId)
                .orElseThrow(() -> new GlobalException(ErrorCode.DATE_NOT_FOUND));

        attendanceDate.update(startTime, startTime.plusMinutes(intervalMinutes));
    }

    @Transactional
    public void deleteAttendanceDate(Member member, Integer studyId, LocalDateTime startTime) {
        studyMemberService.validateStudyLeader(studyId, member);

        AttendanceDate attendanceDate = attendanceDateRepository.findByStudy_IdAndDateTime(studyId, startTime)
                .orElseThrow(() -> new GlobalException(ErrorCode.DATE_UNABLE_TO_ATTEND));
        attendanceDateRepository.delete(attendanceDate);
    }

    public AttendanceCodeResponse getAttendanceCode(Member member, Integer studyId, Integer dateId) {
        studyMemberService.validateStudyLeader(studyId, member);

        AttendanceDate attendanceDate = attendanceDateRepository.findById(dateId)
                .orElseThrow(() -> new GlobalException(ErrorCode.DATE_NOT_FOUND));

        return AttendanceCodeResponse.from(attendanceDate.getCode());
    }

    public Integer getStudyAttendanceCount(Integer studyId) {
        List<StudyMember> studyMemberList = studyMemberRepository.findAllByStudyIdAndRoleIn(studyId, Arrays.asList(StudyMemberRole.LEADER, StudyMemberRole.MEMBER));
        List<AttendanceDate> attendanceDateList = attendanceDateRepository.findAllByStudy_IdOrderByStartTimeAsc(studyId);
        List<AttendanceDate> studyAttendanceDateList = List.copyOf(attendanceDateList);

        // 스터디 구성원들이 모두 출석한 일자 필터링
        for (StudyMember studyMember : studyMemberList) {
            Integer memberId = studyMember.getMember()
                    .getId();

            List<AttendanceDate> memberAttendanceDateList = attendanceRepository.findAllByMember_IdOrderByIdAsc(memberId)
                    .stream()
                    .filter(attendance -> attendanceDateList.contains(attendance.getAttendanceDate()))
                    .filter(attendance -> studyMember.getJoinedAt()
                            .isBefore(attendance.getCreatedAt()))
                    .map(Attendance::getAttendanceDate)
                    .toList();

            studyAttendanceDateList = studyAttendanceDateList.stream()
                    .filter(memberAttendanceDateList::contains)
                    .toList();
        }

        return studyAttendanceDateList.size();
    }

    public double getStudyAttendanceRate(Integer studyId) {
        Integer studyAttendanceCount = getStudyAttendanceCount(studyId);
        Integer studyAttendanceDateCount = attendanceDateRepository.findAllByStudy_IdOrderByStartTimeAsc(studyId)
                .size();

        if (studyAttendanceDateCount.equals(0) || studyAttendanceCount.equals(0)) {
            return 0.0;
        }

        return (double) studyAttendanceCount / studyAttendanceDateCount;
    }

}
