package katecam.luvicookie.ditto.domain.assignment.dto;

import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
public class AssignmentResponse {
    public Integer id;
    public String title;
    public String content;
    public Integer studyId;
    public String createdAt;
    public String deadline;

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
