package katecam.luvicookie.ditto.domain.notice.dto;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record NoticeListResponse(
        List<NoticeResponse> notices,
        Boolean hasNextPage,
        Integer currentPage,
        Integer maxPage,
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
