package katecam.luvicookie.ditto.domain.notice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record NoticeListResponse(
        List<NoticeResponse> notices,
        @JsonProperty("has_next_page")
        Boolean hasNextPage,
        @JsonProperty("current_page")
        Integer currentPage,
        @JsonProperty("max_page")
        Integer maxPage,
        @JsonProperty("total_item_count")
        Integer totalItemCount
) {

    public static NoticeListResponse from(Page<NoticeResponse> noticeResponses) {
        return NoticeListResponse.builder()
                .notices(noticeResponses.toList())
                .hasNextPage(noticeResponses.hasNext())
                .currentPage(noticeResponses.getNumber())
                .maxPage(noticeResponses.getTotalPages())
                .totalItemCount(noticeResponses.getNumberOfElements())
                .build();
    }

}
