package katecam.luvicookie.ditto.domain.study.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record StudyListResponse(
        @JsonProperty("study_list")
        List<StudyResponse> studies,

        @JsonProperty("has_next_page")
        Boolean hasNextPage,

        @JsonProperty("current_page")
        Integer currentPage,

        @JsonProperty("max_page")
        Integer maxPage,

        @JsonProperty("total_item_count")
        Integer totalItemCount
) {

    public static StudyListResponse from(Page<StudyResponse> studyResponses) {
        return StudyListResponse.builder()
                .studies(studyResponses.toList())
                .hasNextPage(studyResponses.hasNext())
                .currentPage(studyResponses.getNumber())
                .maxPage(studyResponses.getTotalPages())
                .totalItemCount(studyResponses.getNumberOfElements())
                .build();
    }

}