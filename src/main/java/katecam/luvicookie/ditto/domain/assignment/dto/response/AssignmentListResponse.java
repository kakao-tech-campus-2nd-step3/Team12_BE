package katecam.luvicookie.ditto.domain.assignment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record AssignmentListResponse(
        List<AssignmentResponse> assignments,
        @JsonProperty("has_next_page")
        Boolean hasNextPage,
        @JsonProperty("current_page")
        Integer currentPage,
        @JsonProperty("max_page")
        Integer maxPage,
        @JsonProperty("total_item_count")
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
