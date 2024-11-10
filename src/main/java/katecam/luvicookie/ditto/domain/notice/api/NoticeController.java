package katecam.luvicookie.ditto.domain.notice.api;

import jakarta.validation.Valid;
import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.notice.application.NoticeService;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeCreateRequest;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeListResponse;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeResponse;
import katecam.luvicookie.ditto.domain.notice.dto.NoticeUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping()
    public ResponseEntity<Void> createNotice(
            @RequestParam("studyId") Integer studyId,
            @LoginUser Member member,
            @RequestBody @Valid NoticeCreateRequest noticeCreateRequest){

        noticeService.create(noticeCreateRequest, studyId, member);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getNotice(@PathVariable Integer noticeId){
        NoticeResponse noticeResponse = noticeService.getNotice(noticeId);
        return ResponseEntity.ok(noticeResponse);
    }

    @GetMapping()
    public ResponseEntity<NoticeListResponse> getNoticeList(
            @PageableDefault Pageable pageable,
            @RequestParam("studyId") Integer studyId
            ){
        NoticeListResponse noticeListResponse = noticeService.getNotices(pageable, studyId);
        return ResponseEntity.ok(noticeListResponse);
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> updateNotice(
            @PathVariable Integer noticeId,
            @LoginUser Member member,
            @RequestBody NoticeUpdateRequest noticeUpdateRequest){

        NoticeResponse noticeResponse = noticeService.updateNotice(noticeId, noticeUpdateRequest, member);
        return ResponseEntity.ok(noticeResponse);
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(
            @PathVariable Integer noticeId,
            @LoginUser Member member){

        noticeService.deleteNotice(noticeId, member);
        return ResponseEntity.noContent()
                .build();
    }
}
