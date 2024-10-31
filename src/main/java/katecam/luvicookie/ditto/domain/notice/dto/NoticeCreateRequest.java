package katecam.luvicookie.ditto.domain.notice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import katecam.luvicookie.ditto.domain.notice.domain.Notice;
import lombok.Getter;

import java.time.LocalDate;

import static katecam.luvicookie.ditto.domain.study.domain.Study.MAX_STUDY_TOPIC_LENGTH;

@Getter
public class NoticeCreateRequest {
    @NotBlank(message = "공지 제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    public Notice toEntity(){
        return Notice.builder()
                .title(title)
                .content(content)
                .build();
    }
}
