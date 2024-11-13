package katecam.luvicookie.ditto.domain.assignment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record AssignmentResponse (
        Integer id,
        String title,
        String content,
        @JsonProperty("study_id")
        Integer studyId,
        @JsonProperty("created_at")
        String createdAt,
        String deadline
){
    public static AssignmentResponse from(Assignment assignment) {
        return AssignmentResponse.builder()
                .id(assignment.getId())
                .title(assignment.getTitle())
                .content(assignment.getContent())
                .createdAt(assignment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .studyId(assignment.getStudy().getId())
                .deadline(assignment.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}
