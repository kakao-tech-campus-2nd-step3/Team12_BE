package katecam.luvicookie.ditto.domain.study.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record StudyRankInfo (
        String name,

        @JsonProperty("total_assignment_count")
        Integer totalAssignmentCount,

        @JsonProperty("completed_assignment_count")
        Integer completedAssignmentCount,

        @JsonProperty("total_attendance_date_count")
        Integer totalAttendanceDateCount,

        @JsonProperty("attended_date_count")
        Integer attendedDateCount
) {
}
