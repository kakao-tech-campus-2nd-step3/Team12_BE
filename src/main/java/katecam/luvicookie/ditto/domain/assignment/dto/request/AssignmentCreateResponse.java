package katecam.luvicookie.ditto.domain.assignment.dto.request;

import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import lombok.Builder;
import lombok.Getter;

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
