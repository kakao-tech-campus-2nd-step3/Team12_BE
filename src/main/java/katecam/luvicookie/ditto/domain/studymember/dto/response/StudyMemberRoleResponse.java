package katecam.luvicookie.ditto.domain.studymember.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.studymember.domain.StudyMemberRole;

public record StudyMemberRoleResponse(
        @JsonProperty("role")
        String role
) {

    public StudyMemberRoleResponse(StudyMemberRole role) {
        this(role.getValue());
    }
}
