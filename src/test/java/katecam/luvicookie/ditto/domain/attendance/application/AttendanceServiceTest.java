package katecam.luvicookie.ditto.domain.attendance.application;

import katecam.luvicookie.ditto.domain.attendance.dao.AttendanceDateRepository;
import katecam.luvicookie.ditto.domain.attendance.dao.AttendanceRepository;
import katecam.luvicookie.ditto.domain.attendance.domain.Attendance;
import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceCodeResponse;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceDateListResponse;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceListResponse;
import katecam.luvicookie.ditto.domain.member.dao.MemberRepository;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.dao.StudyRepository;
import katecam.luvicookie.ditto.domain.study.domain.Study;
import katecam.luvicookie.ditto.domain.studymember.application.StudyMemberService;
import katecam.luvicookie.ditto.domain.studymember.dao.StudyMemberRepository;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMember;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMemberRole;
import katecam.luvicookie.ditto.fixture.AttendanceDateFixture;
import katecam.luvicookie.ditto.fixture.StudyMemberFixture;
import katecam.luvicookie.ditto.global.error.ErrorCode;
import katecam.luvicookie.ditto.global.error.GlobalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceDateRepository attendanceDateRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private StudyRepository studyRepository;

    @Mock
    private StudyMemberRepository studyMemberRepository;

    @Mock
    private StudyMemberService studyMemberService;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private static final String TEST_TRUE_CODE = "true_";

    @Test
    @DisplayName("스터디 출석 성공")
    void createAttendanceSuccess() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;
        Integer dateId = 3;

        LocalDateTime dateTime = LocalDateTime.now();
        int intervalMinutes = 5;

        AttendanceDate attendanceDate = new AttendanceDateFixture(dateTime.minusMinutes(intervalMinutes), dateTime.plusMinutes(intervalMinutes));

        given(attendanceDateRepository.findById(dateId))
                .willReturn(Optional.of(attendanceDate));

        assertDoesNotThrow(() -> attendanceService.createAttendance(member, studyId, TEST_TRUE_CODE, dateId));
        verify(studyMemberService).validateStudyMember(studyId, member);
        verify(attendanceDateRepository).findById(dateId);
        verify(attendanceRepository).save(any(Attendance.class));
    }

    @Test
    @DisplayName("스터디원이 아니면 출석 실패")
    void createAttendanceFailIfNotStudyMember() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;
        Integer dateId = 3;

        willThrow(new GlobalException(ErrorCode.NOT_STUDY_MEMBER))
                .given(studyMemberService).validateStudyMember(studyId, member);

        assertThatThrownBy(() -> attendanceService.createAttendance(member, studyId, TEST_TRUE_CODE, dateId))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NOT_STUDY_MEMBER.getMessage());
        verify(studyMemberService).validateStudyMember(studyId, member);
    }

    @Test
    @DisplayName("출석 시간대가 아니면 출석 실패")
    void createAttendanceFailDueToTime() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;
        Integer dateId = 3;

        int intervalMinutes = 5;
        LocalDateTime dateTime = LocalDateTime.now();

        AttendanceDate attendanceDate = new AttendanceDateFixture(dateTime.minusMinutes(intervalMinutes), dateTime);

        given(attendanceDateRepository.findById(dateId))
                .willReturn(Optional.of(attendanceDate));

        assertThatThrownBy(() -> attendanceService.createAttendance(member, studyId, TEST_TRUE_CODE, dateId))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.DATE_UNABLE_TO_ATTEND.getMessage());
        verify(studyMemberService).validateStudyMember(studyId, member);
        verify(attendanceDateRepository).findById(dateId);
    }

    @Test
    @DisplayName("출석 코드 불일치로 인한 출석 실패")
    void createAttendanceFailDueToCodeMismatch() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;
        Integer dateId = 3;

        String code = "code";

        LocalDateTime dateTime = LocalDateTime.now();
        int intervalMinutes = 5;

        AttendanceDate attendanceDate = new AttendanceDateFixture(dateTime.minusMinutes(intervalMinutes), dateTime.plusMinutes(intervalMinutes));

        given(attendanceDateRepository.findById(dateId))
                .willReturn(Optional.of(attendanceDate));

        assertThatThrownBy(() -> attendanceService.createAttendance(member, studyId, code, dateId))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.CODE_MISMATCH.getMessage());
        verify(studyMemberService).validateStudyMember(studyId, member);
        verify(attendanceDateRepository).findById(dateId);
    }

    @Test
    @DisplayName("스터디원 출석 여부 목록 조회 성공")
    void getAttendanceListSuccess() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;
        Integer memberId = 2;

        LocalDateTime testJoinedAt = LocalDateTime.of(2023, 1, 1, 12, 0);
        StudyMember studyMember = new StudyMemberFixture(testJoinedAt);

        given(studyMemberRepository.findByStudyIdAndMember_Id(studyId, memberId))
                .willReturn(Optional.of(studyMember));

        LocalDateTime startTime1 = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2024, 2, 2, 12, 0);

        int intervalMinutes = 5;

        AttendanceDate attendanceDate1 = new AttendanceDateFixture(startTime1, startTime1.plusMinutes(intervalMinutes));
        AttendanceDate attendanceDate2 = new AttendanceDateFixture(startTime2, startTime2.plusMinutes(intervalMinutes));

        List<AttendanceDate> attendanceDateList = Arrays.asList(attendanceDate1, attendanceDate2);

        given(attendanceDateRepository.findAllByStudy_IdOrderByStartTimeAsc(studyId))
                .willReturn(attendanceDateList);

        Attendance attendance = Attendance.builder()
                .attendanceDate(attendanceDate1)
                .build();
        attendance.initCreatedAt();

        List<Attendance> memberAttendanceList = Arrays.asList(attendance);

        given(attendanceRepository.findAllByMember_IdOrderByIdAsc(any()))
                .willReturn(memberAttendanceList);

        AttendanceListResponse attendanceList = attendanceService.getAttendanceList(member, studyId, memberId);
        assertThat(attendanceList).isNotNull();
        assertThat(attendanceList.attendanceDateList().size())
                .isEqualTo(attendanceDateList.size());
        assertThat(attendanceList.memberAttendances().size())
                .isEqualTo(memberAttendanceList.size());

        verify(studyMemberService).validateStudyLeader(studyId, member);
        verify(studyMemberRepository).findByStudyIdAndMember_Id(studyId, memberId);
        verify(attendanceDateRepository).findAllByStudy_IdOrderByStartTimeAsc(studyId);
        verify(attendanceRepository).findAllByMember_IdOrderByIdAsc(any());
    }

    @Test
    @DisplayName("memberId가 null일때 스터디원 전체 출석 여부 목록 조회 성공")
    void getAttendanceListSuccessWhenMemberIdIsNull() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;
        Integer memberId = null;

        LocalDateTime testJoinedAt = LocalDateTime.of(2023, 1, 1, 12, 0);
        StudyMember studyMember = new StudyMemberFixture(testJoinedAt);

        given(studyMemberRepository.findAllByStudyIdAndRoleIn(studyId, Arrays.asList(StudyMemberRole.LEADER, StudyMemberRole.MEMBER)))
                .willReturn(List.of(studyMember));

        LocalDateTime startTime1 = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2024, 2, 2, 12, 0);

        int intervalMinutes = 5;

        AttendanceDate attendanceDate1 = new AttendanceDateFixture(startTime1, startTime1.plusMinutes(intervalMinutes));
        AttendanceDate attendanceDate2 = new AttendanceDateFixture(startTime2, startTime2.plusMinutes(intervalMinutes));

        List<AttendanceDate> attendanceDateList = Arrays.asList(attendanceDate1, attendanceDate2);

        given(attendanceDateRepository.findAllByStudy_IdOrderByStartTimeAsc(studyId))
                .willReturn(attendanceDateList);

        Attendance attendance = Attendance.builder()
                .attendanceDate(attendanceDate1)
                .build();
        attendance.initCreatedAt();

        List<Attendance> memberAttendanceList = Arrays.asList(attendance);

        given(attendanceRepository.findAllByMember_IdOrderByIdAsc(any()))
                .willReturn(memberAttendanceList);

        AttendanceListResponse attendanceList = attendanceService.getAttendanceList(member, studyId, memberId);
        assertThat(attendanceList).isNotNull();
        assertThat(attendanceList.attendanceDateList().size())
                .isEqualTo(attendanceDateList.size());
        assertThat(attendanceList.memberAttendances().size())
                .isEqualTo(memberAttendanceList.size());

        verify(studyMemberService).validateStudyLeader(studyId, member);
        verify(studyMemberRepository).findAllByStudyIdAndRoleIn(studyId, Arrays.asList(StudyMemberRole.LEADER, StudyMemberRole.MEMBER));
        verify(attendanceDateRepository).findAllByStudy_IdOrderByStartTimeAsc(studyId);
        verify(attendanceRepository).findAllByMember_IdOrderByIdAsc(any());
    }

    @Test
    @DisplayName("스터디 출석 여부 목록 조회 실패")
    void getAttendanceListFail() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;
        Integer memberId = 2;

        willThrow(new GlobalException(ErrorCode.NOT_STUDY_LEADER))
                .given(studyMemberService).validateStudyLeader(studyId, member);

        assertThatThrownBy(() -> attendanceService.getAttendanceList(member, studyId, memberId))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NOT_STUDY_LEADER.getMessage());
        verify(studyMemberService).validateStudyLeader(studyId, member);
    }

    @Test
    @DisplayName("스터디 출석 확인 수정 성공")
    void updateAttendanceIsAttendedTrueSuccess() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Member targetMember = Member.builder()
                .name("스터디원")
                .build();

        Integer studyId = 1;
        Integer memberId = 4;

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(targetMember));

        LocalDateTime startTime = LocalDateTime.now();
        Boolean isAttended = true;
        int intervalMinutes = 5;

        AttendanceDate attendanceDate = new AttendanceDateFixture(startTime, startTime.plusMinutes(intervalMinutes));

        given(attendanceDateRepository.findByStudy_IdAndDateTime(studyId, startTime))
                .willReturn(Optional.of(attendanceDate));

        given(attendanceRepository.existsByAttendanceDate_IdAndMember_Id(any(), eq(memberId)))
                .willReturn(false);

        assertDoesNotThrow(() -> attendanceService.updateAttendance(member, studyId, memberId, startTime, isAttended));
        verify(studyMemberService).validateStudyLeader(studyId, member);
        verify(memberRepository).findById(memberId);
        verify(studyMemberService).validateStudyMember(studyId, targetMember);
        verify(attendanceDateRepository).findByStudy_IdAndDateTime(studyId, startTime);
        verify(attendanceRepository).existsByAttendanceDate_IdAndMember_Id(any(), eq(memberId));
        verify(attendanceRepository).save(any(Attendance.class));
    }

    @Test
    @DisplayName("스터디 출석 취소 수정 성공")
    void updateAttendanceIsAttendedFalseSuccess() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Member targetMember = Member.builder()
                .name("스터디원")
                .build();

        Integer studyId = 1;
        Integer memberId = 4;

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(targetMember));

        LocalDateTime startTime = LocalDateTime.now();
        Boolean isAttended = false;
        int intervalMinutes = 5;

        AttendanceDate attendanceDate = new AttendanceDateFixture(startTime, startTime.plusMinutes(intervalMinutes));

        given(attendanceDateRepository.findByStudy_IdAndDateTime(studyId, startTime))
                .willReturn(Optional.of(attendanceDate));

        given(attendanceRepository.existsByAttendanceDate_IdAndMember_Id(any(), eq(memberId)))
                .willReturn(true);

        assertDoesNotThrow(() -> attendanceService.updateAttendance(member, studyId, memberId, startTime, isAttended));
        verify(studyMemberService).validateStudyLeader(studyId, member);
        verify(memberRepository).findById(memberId);
        verify(studyMemberService).validateStudyMember(studyId, targetMember);
        verify(attendanceDateRepository).findByStudy_IdAndDateTime(studyId, startTime);
        verify(attendanceRepository).existsByAttendanceDate_IdAndMember_Id(any(), eq(memberId));
        verify(attendanceRepository).delete(any(Attendance.class));
    }

    @Test
    @DisplayName("중복 출석으로 인한 스터디 출석 확인 수정 실패")
    void updateAttendanceFailDueToDuplicateAttendance() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Member targetMember = Member.builder()
                .name("스터디원")
                .build();

        Integer studyId = 1;
        Integer memberId = 4;

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(targetMember));

        LocalDateTime startTime = LocalDateTime.now();
        Boolean isAttended = true;
        int intervalMinutes = 5;

        AttendanceDate attendanceDate = new AttendanceDateFixture(startTime, startTime.plusMinutes(intervalMinutes));

        given(attendanceDateRepository.findByStudy_IdAndDateTime(studyId, startTime))
                .willReturn(Optional.of(attendanceDate));

        given(attendanceRepository.existsByAttendanceDate_IdAndMember_Id(any(), eq(memberId)))
                .willReturn(true);

        assertThatThrownBy(() -> attendanceService.updateAttendance(member, studyId, memberId, startTime, isAttended))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.ALREADY_ATTENDED.getMessage());
        verify(studyMemberService).validateStudyLeader(studyId, member);
        verify(memberRepository).findById(memberId);
        verify(studyMemberService).validateStudyMember(studyId, targetMember);
        verify(attendanceDateRepository).findByStudy_IdAndDateTime(studyId, startTime);
        verify(attendanceRepository).existsByAttendanceDate_IdAndMember_Id(any(), eq(memberId));
    }

    @Test
    @DisplayName("출석하지 않았으면 스터디 출석 취소 수정 실패")
    void updateAttendanceFailIfNotAttended() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Member targetMember = Member.builder()
                .name("스터디원")
                .build();

        Integer studyId = 1;
        Integer memberId = 4;

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(targetMember));

        LocalDateTime startTime = LocalDateTime.now();
        Boolean isAttended = false;
        int intervalMinutes = 5;

        AttendanceDate attendanceDate = new AttendanceDateFixture(startTime, startTime.plusMinutes(intervalMinutes));

        given(attendanceDateRepository.findByStudy_IdAndDateTime(studyId, startTime))
                .willReturn(Optional.of(attendanceDate));

        given(attendanceRepository.existsByAttendanceDate_IdAndMember_Id(any(), eq(memberId)))
                .willReturn(false);

        assertThatThrownBy(() -> attendanceService.updateAttendance(member, studyId, memberId, startTime, isAttended))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NOT_ATTENDED.getMessage());
        verify(studyMemberService).validateStudyLeader(studyId, member);
        verify(memberRepository).findById(memberId);
        verify(studyMemberService).validateStudyMember(studyId, targetMember);
        verify(attendanceDateRepository).findByStudy_IdAndDateTime(studyId, startTime);
        verify(attendanceRepository).existsByAttendanceDate_IdAndMember_Id(any(), eq(memberId));
    }

    @Test
    @DisplayName("스터디장이 아니면 스터디 출석 수정 실패")
    void updateAttendanceFailIfNotStudyLeader() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;
        Integer memberId = 4;

        LocalDateTime startTime = LocalDateTime.now();
        Boolean isAttended = true;

        willThrow(new GlobalException(ErrorCode.NOT_STUDY_LEADER))
                .given(studyMemberService).validateStudyLeader(studyId, member);

        assertThatThrownBy(() -> attendanceService.updateAttendance(member, studyId, memberId, startTime, isAttended))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NOT_STUDY_LEADER.getMessage());
        verify(studyMemberService).validateStudyLeader(studyId, member);
    }

    @Test
    @DisplayName("스터디원이 아니면 스터디 출석 수정 실패")
    void updateAttendanceFailIfNotStudyMember() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Member targetMember = Member.builder()
                .name("스터디원")
                .build();

        Integer studyId = 1;
        Integer memberId = 4;

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(targetMember));

        LocalDateTime startTime = LocalDateTime.now();
        Boolean isAttended = true;

        willThrow(new GlobalException(ErrorCode.NOT_STUDY_MEMBER))
                .given(studyMemberService).validateStudyMember(studyId, targetMember);

        assertThatThrownBy(() -> attendanceService.updateAttendance(member, studyId, memberId, startTime, isAttended))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NOT_STUDY_MEMBER.getMessage());
        verify(studyMemberService).validateStudyLeader(studyId, member);
        verify(memberRepository).findById(memberId);
        verify(studyMemberService).validateStudyMember(studyId, targetMember);
    }

    @Test
    @DisplayName("출석일자 목록 조회 성공")
    void getAttendanceDateListSuccess() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;

        LocalDateTime startTime1 = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2024, 2, 2, 12, 0, 0);

        int intervalMinutes = 5;

        AttendanceDate attendanceDate1 = new AttendanceDateFixture(startTime1, startTime1.plusMinutes(intervalMinutes));
        AttendanceDate attendanceDate2 = new AttendanceDateFixture(startTime2, startTime2.plusMinutes(intervalMinutes));

        List<AttendanceDate> attendanceDateList = Arrays.asList(attendanceDate1, attendanceDate2);

        given(attendanceDateRepository.findAllByStudy_IdOrderByStartTimeAsc(studyId))
                .willReturn(attendanceDateList);

        AttendanceDateListResponse attendanceDateResponseList = attendanceService.getAttendanceDateList(member, studyId);

        assertThat(attendanceDateResponseList).isNotNull();
        assertThat(attendanceDateResponseList.attendanceDateList()
                .size()).isEqualTo(attendanceDateList.size());
        verify(studyMemberService).validateStudyMember(studyId, member);
        verify(attendanceDateRepository).findAllByStudy_IdOrderByStartTimeAsc(studyId);
    }

    @Test
    @DisplayName("출석일자 목록 조회 실패")
    void getAttendanceDateListFail() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;

        willThrow(new GlobalException(ErrorCode.NOT_STUDY_MEMBER))
                .given(studyMemberService).validateStudyMember(studyId, member);

        assertThatThrownBy(() -> attendanceService.getAttendanceDateList(member, studyId))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NOT_STUDY_MEMBER.getMessage());
        verify(studyMemberService).validateStudyMember(studyId, member);
    }

    @Test
    @DisplayName("출석일자 생성 성공")
    void createAttendanceDateSuccess() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;

        LocalDateTime startTime = LocalDateTime.now();

        Integer intervalMinutes = 5;

        Study study = Study.builder()
                .name("테스트 스터디")
                .topic("테스트 주제")
                .isOpen(true)
                .build();
        study.initCreatedAt();

        given(studyRepository.findById(studyId))
                .willReturn(Optional.of(study));

        assertDoesNotThrow(() -> attendanceService.createAttendanceDate(member, studyId, startTime, intervalMinutes));
        verify(studyMemberService).validateStudyLeader(studyId, member);
        verify(studyRepository).findById(studyId);
        verify(attendanceDateRepository).save(any(AttendanceDate.class));
    }

    @Test
    @DisplayName("출석일자 생성 실패")
    void createAttendanceDateFail() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;

        LocalDateTime startTime = LocalDateTime.now();

        Integer intervalMinutes = 5;

        willThrow(new GlobalException(ErrorCode.NOT_STUDY_LEADER))
                .given(studyMemberService).validateStudyLeader(studyId, member);

        assertThatThrownBy(() -> attendanceService.createAttendanceDate(member, studyId, startTime, intervalMinutes))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NOT_STUDY_LEADER.getMessage());
        verify(studyMemberService).validateStudyLeader(studyId, member);
    }

    @Test
    @DisplayName("출석일자 삭제 성공")
    void deleteAttendanceDateSuccess() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;

        LocalDateTime startTime = LocalDateTime.now();
        int intervalMinutes = 5;
        AttendanceDate attendanceDate = new AttendanceDateFixture(startTime, startTime.plusMinutes(intervalMinutes));

        given(attendanceDateRepository.findByStudy_IdAndDateTime(studyId, startTime))
                .willReturn(Optional.of(attendanceDate));

        assertDoesNotThrow(() -> attendanceService.deleteAttendanceDate(member, studyId, startTime));
        verify(studyMemberService).validateStudyLeader(studyId, member);
        verify(attendanceDateRepository).findByStudy_IdAndDateTime(studyId, startTime);
        verify(attendanceDateRepository).delete(any(AttendanceDate.class));
    }

    @Test
    @DisplayName("출석일자 삭제 실패")
    void deleteAttendanceDateFail() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;

        LocalDateTime startTime = LocalDateTime.now();

        willThrow(new GlobalException(ErrorCode.NOT_STUDY_LEADER))
                .given(studyMemberService).validateStudyLeader(studyId, member);

        assertThatThrownBy(() -> attendanceService.deleteAttendanceDate(member, studyId, startTime))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NOT_STUDY_LEADER.getMessage());
        verify(studyMemberService).validateStudyLeader(studyId, member);
    }

    @Test
    @DisplayName("출석 코드 조회 성공")
    void getAttendanceCodeSuccess() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;

        Integer dateId = 3;

        LocalDateTime startTime = LocalDateTime.now();
        int intervalMinutes = 5;
        AttendanceDate attendanceDate = new AttendanceDateFixture(startTime, startTime.plusMinutes(intervalMinutes));;

        given(attendanceDateRepository.findById(dateId))
                .willReturn(Optional.of(attendanceDate));

        AttendanceCodeResponse attendanceCode = attendanceService.getAttendanceCode(member, studyId, dateId);
        assertThat(attendanceCode.code()).isNull();
        verify(studyMemberService).validateStudyLeader(studyId, member);
        verify(attendanceDateRepository).findById(dateId);
    }

    @Test
    @DisplayName("출석 코드 조회 실패")
    void getAttendanceCodeFail() {
        Member member = Member.builder()
                .name("스터디 회원")
                .build();

        Integer studyId = 1;

        Integer dateId = 3;

        willThrow(new GlobalException(ErrorCode.NOT_STUDY_LEADER))
                .given(studyMemberService).validateStudyLeader(studyId, member);

        assertThatThrownBy(() -> attendanceService.getAttendanceCode(member, studyId, dateId))
                .isInstanceOf(GlobalException.class)
                .hasMessage(ErrorCode.NOT_STUDY_LEADER.getMessage());
        verify(studyMemberService).validateStudyLeader(studyId, member);
    }

}