package katecam.luvicookie.ditto.domain.notice.dto;

import katecam.luvicookie.ditto.domain.notice.domain.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class NoticeResponse {
    private Integer id;
    private String title;
    private String nickName;
    private String content;
    private LocalDateTime createdAt;

    public static NoticeResponse from(Notice notice) {
        return NoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .nickName(notice.getTeamMate().getMember().getNickName())
                .build();

    }

}
