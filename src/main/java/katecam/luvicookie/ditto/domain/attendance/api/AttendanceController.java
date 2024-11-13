package katecam.luvicookie.ditto.domain.attendance.api;

import jakarta.validation.Valid;
import katecam.luvicookie.ditto.domain.attendance.application.AttendanceService;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceCodeRequest;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceDateCreateRequest;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceDateDeleteRequest;
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
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<Void> createAttendance(
            @LoginUser Member member,
            @RequestParam("studyId") Integer studyId,
            @RequestBody @Valid AttendanceCodeRequest request
    ) {
        attendanceService.createAttendance(member, studyId, request.code(), request.dateId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    public ResponseEntity<AttendanceListResponse> getAttendanceList(
            @LoginUser Member member,
            @RequestParam("studyId") Integer studyId,
            @RequestParam(name = "memberId", required = false) Integer memberId
    ) {
        return ResponseEntity.ok(attendanceService.getAttendanceList(member, studyId, memberId));
    }

    @PutMapping
    public ResponseEntity<Void> updateAttendance(
            @LoginUser Member member,
            @RequestParam("studyId") Integer studyId,
            @RequestParam("memberId") Integer memberId,
            @RequestBody @Valid AttendanceUpdateRequest request
    ) {
        attendanceService.updateAttendance(member, studyId, memberId, request.dateTime(), request.isAttended());
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/date")
    public ResponseEntity<Void> createAttendanceDate(
            @LoginUser Member member,
            @RequestParam("studyId") Integer studyId,
            @RequestBody @Valid AttendanceDateCreateRequest request
    ) {
        attendanceService.createAttendanceDate(member, studyId, request.startTime(), request.intervalMinutes());
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/date")
    public ResponseEntity<AttendanceDateListResponse> getAttendanceDateList(
            @LoginUser Member member,
            @RequestParam("studyId") Integer studyId
    ) {
        return ResponseEntity.ok(attendanceService.getAttendanceDateList(member, studyId));
    }

    @DeleteMapping("/date")
    public ResponseEntity<Void> deleteAttendanceDate(
            @LoginUser Member member,
            @RequestParam("studyId") Integer studyId,
            @RequestBody @Valid AttendanceDateDeleteRequest request
    ) {
        attendanceService.deleteAttendanceDate(member, studyId, request.startTime());
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/code")
    public ResponseEntity<AttendanceCodeResponse> requestAttendanceCode(
            @LoginUser Member member,
            @RequestParam("studyId") Integer studyId,
            @RequestParam("dateId") Integer dateId
    ) {
        return ResponseEntity.ok(attendanceService.getAttendanceCode(member, studyId, dateId));
    }

}
