package katecam.luvicookie.ditto.domain.notice.dto;

import katecam.luvicookie.ditto.domain.notice.domain.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class NoticeResponse {
    private Integer id;
    private String title;
    private Integer writer_id;
    private String content;
    private LocalDate createdAt;

    public static NoticeResponse from(Notice notice) {
        return NoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .writer_id(notice.getWriter_id())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .build();

    }
}
