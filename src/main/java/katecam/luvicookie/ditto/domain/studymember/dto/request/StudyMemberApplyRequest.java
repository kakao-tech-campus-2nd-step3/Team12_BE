package katecam.luvicookie.ditto.domain.studymember.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudyMemberApplyRequest(
        @NotNull
        String message
) {

}
