package katecam.luvicookie.ditto.domain.studymember.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StudyInviteResponse(
    @JsonProperty("study_id")
    Integer studyId,
    @JsonProperty("token")
    String token
) {
}
