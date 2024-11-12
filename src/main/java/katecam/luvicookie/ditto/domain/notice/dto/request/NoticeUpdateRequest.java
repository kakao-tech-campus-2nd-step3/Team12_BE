package katecam.luvicookie.ditto.domain.notice.dto.request;

import lombok.Getter;

@Getter
public record NoticeUpdateRequest (
        String title,
        String content
){}
