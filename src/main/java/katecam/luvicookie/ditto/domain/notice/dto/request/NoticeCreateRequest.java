package katecam.luvicookie.ditto.domain.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import katecam.luvicookie.ditto.domain.notice.domain.Notice;

public record NoticeCreateRequest (
        @NotBlank(message = "공지 제목을 입력해주세요.")
        String title,
        @NotBlank(message = "내용을 입력해주세요.")
        String content
){
    public Notice toEntity(){
        return Notice.builder()
                .title(title)
                .content(content)
                .build();
    }
}
