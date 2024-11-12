package katecam.luvicookie.ditto.domain.notice.dto.response;

import katecam.luvicookie.ditto.domain.notice.domain.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Builder
@Getter
@Setter
public record NoticeResponse (
        Integer id,
        String title,
        String nickName,
        String content,
        String createdAt
){
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
