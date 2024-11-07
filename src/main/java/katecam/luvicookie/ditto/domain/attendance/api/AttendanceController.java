package katecam.luvicookie.ditto.domain.attendance.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import katecam.luvicookie.ditto.domain.attendance.application.AttendanceService;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceDateCreateRequest;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceUpdateRequest;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceDateListResponse;
import katecam.luvicookie.ditto.domain.attendance.dto.response.AttendanceListResponse;
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

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<Void> createAttendance(
            @RequestParam("studyId") Integer studyId,
            @RequestParam("memberId") Integer memberId
    ) {
        attendanceService.createAttendance(studyId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    public ResponseEntity<AttendanceListResponse> getAttendanceList(
            @RequestParam("studyId") Integer studyId,
            @RequestParam(name = "memberId", required = false) Integer memberId
    ) {
        return ResponseEntity.ok(attendanceService.getAttendanceList(studyId, memberId));
    }

    @PutMapping
    public ResponseEntity<Void> updateAttendance(
            @RequestParam("studyId") Integer studyId,
            @RequestParam("memberId") Integer memberId,
            @RequestBody @Valid AttendanceUpdateRequest request
    ) {
        attendanceService.updateAttendance(studyId, memberId, request.dateTime(), request.isAttended());
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/date")
    public ResponseEntity<Void> createAttendanceDate(
            @RequestParam("studyId") Integer studyId,
            @RequestBody @Valid AttendanceDateCreateRequest request
    ) {
        attendanceService.createAttendanceDate(studyId, request.startTime(), request.intervalMinutes());
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/date")
    public ResponseEntity<AttendanceDateListResponse> getAttendanceDateList(
            @RequestParam("studyId") Integer studyId
    ) {
        return ResponseEntity.ok(attendanceService.getAttendanceDateList(studyId));
    }

    @DeleteMapping("/date")
    public ResponseEntity<Void> deleteAttendanceDate(
            @RequestParam("studyId") Integer studyId,
            @RequestBody
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
            LocalDateTime attendanceTime
    ) {
        attendanceService.deleteAttendanceDate(studyId, attendanceTime);
        return ResponseEntity.noContent()
                .build();
    }

}
