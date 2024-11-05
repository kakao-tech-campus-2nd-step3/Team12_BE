package katecam.luvicookie.ditto.domain.notice.dto;

import katecam.luvicookie.ditto.domain.notice.domain.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
@Setter
public class NoticeResponse {
    private Integer id;
    private String title;
    private String nickName;
    private String content;
    private String createdAt;

    public static NoticeResponse from(Notice notice) {
        return NoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .nickName(notice.getMember().getNickname())
                .build();

    }

}
