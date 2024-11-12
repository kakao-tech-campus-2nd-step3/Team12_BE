package katecam.luvicookie.ditto.domain.assignment.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record AssignmentListResponse(
        List<AssignmentResponse> assignments,
        Boolean hasNextPage,
        Integer currentPage,
        Integer maxPage,
        Integer totalItemCount
) {

    public static AssignmentListResponse from(Page<AssignmentResponse> assignmentResponses) {
        return AssignmentListResponse.builder()
                .assignments(assignmentResponses.toList())
                .hasNextPage(assignmentResponses.hasNext())
                .currentPage(assignmentResponses.getNumber())
                .maxPage(assignmentResponses.getTotalPages())
                .totalItemCount(assignmentResponses.getNumberOfElements())
                .build();
    }

}
