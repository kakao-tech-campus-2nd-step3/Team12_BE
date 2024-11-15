package katecam.luvicookie.ditto.domain.notice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.notice.application.NoticeService;
import katecam.luvicookie.ditto.domain.notice.dto.request.NoticeCreateRequest;
import katecam.luvicookie.ditto.domain.notice.dto.request.NoticeUpdateRequest;
import katecam.luvicookie.ditto.domain.notice.dto.response.NoticeListResponse;
import katecam.luvicookie.ditto.domain.notice.dto.response.NoticeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@Tag(name = "공지 게시판", description = "스터디의 공지 게시판 관련 API 입니다.")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping()
    @Operation(summary = "생성", description = "공지를 등록합니다.")
    public ResponseEntity<Void> createNotice(
            @RequestParam("studyId") Integer studyId,
            @LoginUser Member member,
            @RequestBody @Valid NoticeCreateRequest noticeCreateRequest){

        noticeService.create(noticeCreateRequest, studyId, member);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/{noticeId}")
    @Operation(summary = "조회 - ID", description = "공지 ID로 공지를 조회합니다.")
    public ResponseEntity<NoticeResponse> getNotice(@PathVariable Integer noticeId){
        NoticeResponse noticeResponse = noticeService.getNotice(noticeId);
        return ResponseEntity.ok(noticeResponse);
    }

    @GetMapping()
    @Operation(summary = "조회 - 페이지", description = "해당 스터디에 있는 공지를 페이지로 조회합니다.")
    public ResponseEntity<NoticeListResponse> getNoticeList(
            @PageableDefault Pageable pageable,
            @RequestParam("studyId") Integer studyId
            ){
        NoticeListResponse noticeListResponse = noticeService.getNotices(pageable, studyId);
        return ResponseEntity.ok(noticeListResponse);
    }

    @PutMapping("/{noticeId}")
    @Operation(summary = "수정", description = "공지의 정보를 수정합니다.")
    public ResponseEntity<NoticeResponse> updateNotice(
            @PathVariable Integer noticeId,
            @LoginUser Member member,
            @RequestBody NoticeUpdateRequest noticeUpdateRequest){

        NoticeResponse noticeResponse = noticeService.updateNotice(noticeId, noticeUpdateRequest, member);
        return ResponseEntity.ok(noticeResponse);
    }

    @DeleteMapping("/{noticeId}")
    @Operation(summary = "삭제", description = "공지를 삭제합니다.")
    public ResponseEntity<Void> deleteNotice(
            @PathVariable Integer noticeId,
            @LoginUser Member member){

        noticeService.deleteNotice(noticeId, member);
        return ResponseEntity.noContent()
                .build();
    }
}
