package katecam.luvicookie.ditto.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.application.MemberService;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.dto.request.MemberCreateRequest;
import katecam.luvicookie.ditto.domain.member.dto.request.MemberUpdateRequest;
import katecam.luvicookie.ditto.domain.member.dto.response.MemberResponse;
import katecam.luvicookie.ditto.domain.study.application.StudyService;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 관리용 API 입니다.")
public class MemberController {
    private final MemberService memberService;
    private final StudyService studyService;

    @PostMapping("/api/auth")
    @Operation(summary = "회원가입", description = "사용자 정보를 입력받아 회원 등록을 합니다.")
    public ResponseEntity<MemberResponse> signup(@RequestBody MemberCreateRequest memberCreateRequest){
        Member member = memberService.registerMember(memberCreateRequest);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @GetMapping("/api/users")
    @Operation(summary = "조회", description = "현재 로그인한 회원 정보를 조회합니다.")
    public ResponseEntity<MemberResponse> getUserInfo(@LoginUser Member member){
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PutMapping("/api/users")
    @Operation(summary = "수정", description = "현재 로그인한 회원 정보를 수정합니다.")
    public ResponseEntity<MemberResponse> updateUserInfo(@LoginUser Member member, @RequestBody MemberUpdateRequest memberDTO){
        Member updateMember = memberService.updateMember(memberDTO, member);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PutMapping("/api/users/profileImage")
    @Operation(summary = "수정 - 프로필 이미지", description = "현재 로그인한 회원 프로필 이미지를 수정합니다.")
    public ResponseEntity<?> updateProfileImage(
            @LoginUser Member member,
            @RequestPart MultipartFile profileImage){
        memberService.updateProfileImage(profileImage, member.getId());
        return ResponseEntity.ok(new ResponseEntity<>(HttpStatus.OK));
    }

    @GetMapping("/api/users/studies")
    @Operation(summary = "조회 - 페이지", description = "회원이 가입한 스터디를 페이지로 조회합니다.")
    public ResponseEntity<List<StudyResponse>> getUserStudyList(
            @LoginUser Member member
    ) {
        return ResponseEntity.ok(studyService.getStudyListByMemberId(member.getId()));
    }

}
