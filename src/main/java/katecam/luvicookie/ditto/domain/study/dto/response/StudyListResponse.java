package katecam.luvicookie.ditto.domain.study.dto.response;

import java.util.List;

public record StudyListResponse(List<StudyResponse> studies) {

    public static StudyListResponse toResponse(List<StudyResponse> studies) {
        return new StudyListResponse(studies);
    }

}
