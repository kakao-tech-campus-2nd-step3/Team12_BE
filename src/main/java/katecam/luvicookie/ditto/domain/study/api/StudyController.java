package katecam.luvicookie.ditto.domain.study.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.application.StudyService;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCreateRequest;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCriteria;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyUpdateRequest;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyListResponse;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/studies")
@RequiredArgsConstructor
@Tag(name = "스터디", description = "스터디 관리 API 입니다.")
public class StudyController {

    private final StudyService studyService;

    @GetMapping
    @Operation(summary = "조회 - 페이지", description = "검색 기준에 따라 스터디를 페이지로 조회합니다.")
    public ResponseEntity<StudyListResponse> getStudyList(
            @PageableDefault Pageable pageable,
            @ModelAttribute @Valid StudyCriteria studyCriteria
    ) {
        return ResponseEntity.ok(studyService.getStudyList(pageable, studyCriteria));
    }

    @GetMapping("/{studyId}")
    @Operation(summary = "조회", description = "스터디 ID로 스터디를 조회합니다.")
    public ResponseEntity<StudyResponse> getStudy(
            @PathVariable Integer studyId
    ) {
        return ResponseEntity.ok(studyService.getStudy(studyId));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "생성", description = "스터디를 생성합니다.")
    public ResponseEntity<Void> createStudy(
            @LoginUser Member member,
            @RequestPart @Valid StudyCreateRequest request,
            @RequestPart("profile_image") MultipartFile profileImage
    ) {
        studyService.create(member, request, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{studyId}")
    @Operation(summary = "삭제", description = "스터디를 삭제합니다.")
    public ResponseEntity<Void> deleteStudy(
            @LoginUser Member member,
            @PathVariable Integer studyId
    ) {
        studyService.delete(member, studyId);
        return ResponseEntity.noContent()
                .build();
    }

    @PutMapping("/{studyId}")
    @Operation(summary = "수정", description = "스터디 정보를 수정합니다.")
    public ResponseEntity<Void> updateStudy(
            @LoginUser Member member,
            @PathVariable Integer studyId,
            @RequestBody @Valid StudyUpdateRequest request
    ) {
        studyService.update(member, studyId, request);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @PutMapping("/{studyId}/profileImage")
    @Operation(summary = "수정 - 프로필 이미지", description = "스터디의 프로필 이미지를 수정합니다.")
    public ResponseEntity<Void> updateStudyProfileImage(
            @LoginUser Member member,
            @PathVariable Integer studyId,
            @RequestPart("profile_image") MultipartFile profileImage
    ) {
        studyService.updateProfileImage(member, studyId, profileImage);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

}
