package katecam.luvicookie.ditto.domain.assignment.dto.request;

import jakarta.validation.constraints.NotBlank;
import katecam.luvicookie.ditto.domain.assignment.domain.Assignment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record AssignmentRequest (

    @NotBlank(message = "과제명을 입력해주세요")
    String title,
    @NotBlank(message = "설명을 입력해주세요")
    String content,
    @NotBlank(message = "마감기한을 입력해주세요")
    String deadline
){
    public Assignment toEntity(){
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime strDeadline = LocalDateTime.parse(deadline, format1);

        return Assignment.builder()
                .title(title)
                .content(content)
                .deadline(strDeadline)
                .build();
    }
}
