package katecam.luvicookie.ditto.domain.study.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record StudyRankListResponse(
        @JsonProperty("study_rank_list")
        List<StudyRankResponse> studyRankings,

        @JsonProperty("has_next_page")
        Boolean hasNextPage,

        @JsonProperty("current_page")
        Integer currentPage,

        @JsonProperty("max_page")
        Integer maxPage,

        @JsonProperty("total_item_count")
        Integer totalItemCount
) {

    public static StudyRankListResponse from(Page<StudyRankResponse> studyRankResponses) {
        return StudyRankListResponse.builder()
                .studyRankings(studyRankResponses.toList())
                .hasNextPage(studyRankResponses.hasNext())
                .currentPage(studyRankResponses.getNumber())
                .maxPage(studyRankResponses.getTotalPages())
                .totalItemCount(studyRankResponses.getNumberOfElements())
                .build();
    }
}
