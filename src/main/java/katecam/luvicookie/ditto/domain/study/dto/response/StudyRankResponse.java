package katecam.luvicookie.ditto.domain.study.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StudyRankResponse(
        Integer rank,

        @JsonProperty("study_rank_info")
        StudyRankInfo studyRankInfo
) {
}
