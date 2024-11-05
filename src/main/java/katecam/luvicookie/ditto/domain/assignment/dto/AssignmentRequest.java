package katecam.luvicookie.ditto.domain.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AssignmentRequest {

    @NotBlank(message = "과제명을 입력해주세요")
    private String title;
    @NotBlank(message = "설명을 입력해주세요")
    private String content;
    @NotBlank(message = "마감기한을 입력해주세요")
    private LocalDateTime deadline;

    public Assignment toEntity(){
        return Assignment.builder()
                .title(title)
                .content(content)
                .deadline(deadline)
                .build();
    }
}
