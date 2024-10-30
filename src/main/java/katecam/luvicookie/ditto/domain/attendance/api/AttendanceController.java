package katecam.luvicookie.ditto.domain.attendance.api;

import jakarta.validation.Valid;
import katecam.luvicookie.ditto.domain.attendance.application.AttendanceService;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceDateRequest;
import katecam.luvicookie.ditto.domain.attendance.dto.request.AttendanceUpdateRequest;
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
            @RequestParam("studyId") Integer studyId,
            @RequestParam("memberId") Integer memberId
    ) {
        attendanceService.createAttendance(studyId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    public ResponseEntity<Void> getAttendanceList(
            @RequestParam("studyId") Integer studyId,
            @RequestParam("memberId") Integer memberId
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping
    public ResponseEntity<Void> updateAttendance(
            @RequestParam("studyId") Integer studyId,
            @RequestParam("memberId") Integer memberId,
            @RequestBody @Valid AttendanceUpdateRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/date")
    public ResponseEntity<Void> createAttendanceDate(
            @RequestParam("studyId") Integer studyId,
            @RequestBody @Valid AttendanceDateRequest request
    ) {
        attendanceService.createAttendanceDate(studyId, request.dateTime());
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/date")
    public ResponseEntity<Void> getAttendanceDateList(
            @RequestParam("studyId") Integer studyId
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/date")
    public ResponseEntity<Void> deleteAttendanceDate(
            @RequestParam("studyId") Integer studyId,
            @RequestBody @Valid AttendanceDateRequest request
    ) {
        attendanceService.deleteAttendanceDate(studyId, request.dateTime());
        return ResponseEntity.noContent()
                .build();
    }

}
