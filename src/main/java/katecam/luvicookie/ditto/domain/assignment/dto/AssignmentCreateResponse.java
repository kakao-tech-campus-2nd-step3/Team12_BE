package katecam.luvicookie.ditto.domain.assignment.dto;

import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class AssignmentCreateResponse {
    Integer id;

    public static AssignmentCreateResponse from(Assignment assignment) {
        return AssignmentCreateResponse.builder()
                .id(assignment.getId())
                .build();
    }
}
