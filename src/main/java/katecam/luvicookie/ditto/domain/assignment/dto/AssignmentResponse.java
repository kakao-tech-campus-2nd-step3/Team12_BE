package katecam.luvicookie.ditto.domain.assignment.dto;

import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class AssignmentResponse {
    public Integer id;
    public String title;
    public String content;
    public Integer studyId;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;

    public static AssignmentResponse from(Assignment assignment) {
        return AssignmentResponse.builder()
                .id(assignment.getId())
                .title(assignment.getTitle())
                .content(assignment.getContent())
                .createdAt(assignment.getCreatedAt())
                .studyId(assignment.getStudy().getId())
                .build();
    }
}
