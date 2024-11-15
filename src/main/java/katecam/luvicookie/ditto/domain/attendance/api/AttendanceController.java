package katecam.luvicookie.ditto.domain.attendance.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import katecam.luvicookie.ditto.domain.attendance.application.AttendanceService;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceCreateRequest;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceDateCreateRequest;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceDateDeleteRequest;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceDateUpdateRequest;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceUpdateRequest;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceCodeResponse;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceDateListResponse;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceListResponse;
import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Tag(name = "출석", description = "스터디 멤버의 출석 관련 API입니다.")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @Operation(summary = "생성", description = "스터디 멤버들이 출석 해야 할 일자를 생성합니다.")
    public ResponseEntity<Void> createAttendance(
            @LoginUser Member member,
            @RequestParam("study_id") Integer studyId,
            @RequestBody @Valid AttendanceCreateRequest request
    ) {
        attendanceService.createAttendance(member, studyId, request.code(), request.dateId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    @Operation(summary = "조회 - 페이지", description = "스터디 멤버의 출석을 페이지로 조회합니다.")
    public ResponseEntity<AttendanceListResponse> getAttendanceList(
            @LoginUser Member member,
            @RequestParam("study_id") Integer studyId,
            @RequestParam(name = "member_id", required = false) Integer memberId
    ) {
        return ResponseEntity.ok(attendanceService.getAttendanceList(member, studyId, memberId));
    }

    @PutMapping
    @Operation(summary = "수정", description = "출석 여부를 수정합니다.")
    public ResponseEntity<Void> updateAttendance(
            @LoginUser Member member,
            @RequestParam("study_id") Integer studyId,
            @RequestParam("member_id") Integer memberId,
            @RequestBody @Valid AttendanceUpdateRequest request
    ) {
        attendanceService.updateAttendance(member, studyId, memberId, request.dateId(), request.isAttended());
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/date")
    @Operation(summary = "조회", description = "등록된 게시물을 조회합니다.")
    public ResponseEntity<Void> createAttendanceDate(
            @LoginUser Member member,
            @RequestParam("study_id") Integer studyId,
            @RequestBody @Valid AttendanceDateCreateRequest request
    ) {
        attendanceService.createAttendanceDate(member, studyId, request.startTime(), request.intervalMinutes());
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/date")
    public ResponseEntity<AttendanceDateListResponse> getAttendanceDateList(
            @LoginUser Member member,
            @RequestParam("study_id") Integer studyId
    ) {
        return ResponseEntity.ok(attendanceService.getAttendanceDateList(member, studyId));
    }

    @PutMapping("/date")
    public ResponseEntity<Void> updateAttendanceDate(
            @LoginUser Member member,
            @RequestParam("study_id") Integer studyId,
            @RequestBody @Valid AttendanceDateUpdateRequest request
    ) {
        attendanceService.updateAttendanceDate(member, studyId, request.dateId(), request.startTime(), request.intervalMinutes());
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/date")
    public ResponseEntity<Void> deleteAttendanceDate(
            @LoginUser Member member,
            @RequestParam("study_id") Integer studyId,
            @RequestBody @Valid AttendanceDateDeleteRequest request
    ) {
        attendanceService.deleteAttendanceDate(member, studyId, request.startTime());
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/code")
    public ResponseEntity<AttendanceCodeResponse> requestAttendanceCode(
            @LoginUser Member member,
            @RequestParam("study_id") Integer studyId,
            @RequestParam("date_id") Integer dateId
    ) {
        return ResponseEntity.ok(attendanceService.getAttendanceCode(member, studyId, dateId));
    }

}
