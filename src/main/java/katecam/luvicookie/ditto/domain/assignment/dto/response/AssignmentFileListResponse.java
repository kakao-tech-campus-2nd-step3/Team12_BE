package katecam.luvicookie.ditto.domain.assignment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record AssignmentFileListResponse(
        @JsonProperty("assignment_files")
        List<AssignmentFileResponse> assignmentFiles,
        @JsonProperty("has_next_page")
        Boolean hasNextPage,
        @JsonProperty("current_page")
        Integer currentPage,
        @JsonProperty("max_page")
        Integer maxPage,
        @JsonProperty("total_item_count")
        Integer totalItemCount
) {

    public static AssignmentFileListResponse from(Page<AssignmentFileResponse> assignmentFileResponses) {
        return AssignmentFileListResponse.builder()
                .assignmentFiles(assignmentFileResponses.toList())
                .hasNextPage(assignmentFileResponses.hasNext())
                .currentPage(assignmentFileResponses.getNumber())
                .maxPage(assignmentFileResponses.getTotalPages())
                .totalItemCount(assignmentFileResponses.getNumberOfElements())
                .build();
    }

}
