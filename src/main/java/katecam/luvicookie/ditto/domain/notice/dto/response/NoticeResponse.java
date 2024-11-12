package katecam.luvicookie.ditto.domain.notice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import katecam.luvicookie.ditto.domain.notice.domain.Notice;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record NoticeResponse (
        Integer id,
        String title,
        @JsonProperty("nick_name")
        String nickName,
        String content,
        @JsonProperty("created_at")
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
