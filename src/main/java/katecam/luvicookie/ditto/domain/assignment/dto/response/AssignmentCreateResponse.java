package katecam.luvicookie.ditto.domain.assignment.dto.response;

import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import lombok.Builder;

@Builder
public record AssignmentCreateResponse (
    Integer id
){
    public static AssignmentCreateResponse from(Assignment assignment) {
        return AssignmentCreateResponse.builder()
                .id(assignment.getId())
                .build();
    }
}
