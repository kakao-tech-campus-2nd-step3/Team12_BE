package katecam.luvicookie.ditto.fixture;

import katecam.luvicookie.ditto.domain.attendance.domain.AttendanceDate;
import katecam.luvicookie.ditto.domain.study.domain.Study;

import java.time.LocalDateTime;

public class AttendanceDateFixture extends AttendanceDate {

    private static final String TEST_TRUE_CODE = "true_";

    public AttendanceDateFixture(LocalDateTime startTime, LocalDateTime deadline) {
        super(Study.builder().build(), startTime, deadline);
    }

    @Override
    public boolean isDifferentCode(String code) {
        return !TEST_TRUE_CODE.equals(code);
    }

}
