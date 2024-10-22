package katecam.luvicookie.ditto.domain.study.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record StudyListResponse(
        List<StudyResponse> studies,
        Boolean hasNextPage,
        Integer currentPage,
        Integer maxPage,
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