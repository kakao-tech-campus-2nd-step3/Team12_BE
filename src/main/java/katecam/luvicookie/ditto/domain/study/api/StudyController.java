package katecam.luvicookie.ditto.domain.study.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.study.application.StudyService;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCriteria;
import katecam.luvicookie.ditto.domain.study.dto.request.StudyCreateRequest;
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
public class StudyController {

    private final StudyService studyService;

    @GetMapping
    public ResponseEntity<StudyListResponse> getStudyList(
            @PageableDefault Pageable pageable,
            @ModelAttribute @Valid StudyCriteria studyCriteria
    ) {
        return ResponseEntity.ok(studyService.getStudyList(pageable, studyCriteria));
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<StudyResponse> getStudy(
            @PathVariable Integer studyId
    ) {
        return ResponseEntity.ok(studyService.getStudy(studyId));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> createStudy(
            @LoginUser Member member,
            @RequestPart @Valid StudyCreateRequest request,
            @RequestPart
            @JsonProperty("profile_image")
            MultipartFile profileImage
    ) {
        studyService.create(member, request, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{studyId}")
    public ResponseEntity<Void> deleteStudy(
            @LoginUser Member member,
            @PathVariable Integer studyId
    ) {
        studyService.delete(member, studyId);
        return ResponseEntity.noContent()
                .build();
    }

    @PutMapping("/{studyId}")
    public ResponseEntity<Void> updateStudy(
            @LoginUser Member member,
            @PathVariable Integer studyId,
            @RequestBody @Valid StudyUpdateRequest request
    ) {
        studyService.update(member, studyId, request);
        return ResponseEntity.noContent()
                .build();
    }

    @PutMapping("/{studyId}/profileImage")
    public ResponseEntity<Void> updateStudyProfileImage(
            @LoginUser Member member,
            @PathVariable Integer studyId,
            @RequestPart
            @JsonProperty("profile_image")
            MultipartFile profileImage
    ) {
        studyService.updateProfileImage(member, studyId, profileImage);
        return ResponseEntity.noContent()
                .build();
    }

}
