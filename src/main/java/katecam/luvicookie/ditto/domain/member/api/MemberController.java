package katecam.luvicookie.ditto.domain.member.api;

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
public class MemberController {
    private final MemberService memberService;
    private final StudyService studyService;

    @PostMapping("/api/auth")
    public ResponseEntity<MemberResponse> signup(@RequestBody MemberCreateRequest memberCreateRequest){
        Member member = memberService.registerMember(memberCreateRequest);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @GetMapping("/api/users")
    public ResponseEntity<MemberResponse> getUserInfo(@LoginUser Member member){
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PutMapping("/api/users")
    public ResponseEntity<MemberResponse> updateUserInfo(@LoginUser Member member, @RequestBody MemberUpdateRequest memberDTO){
        Member updateMember = memberService.updateMember(memberDTO, member);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PutMapping("/api/users/profileImage")
    public ResponseEntity<?> updateProfileImage(
            @LoginUser Member member,
            @RequestPart MultipartFile profileImage){
        memberService.updateProfileImage(profileImage, member.getId());
        return ResponseEntity.ok(new ResponseEntity<>(HttpStatus.OK));
    }

    @GetMapping("/api/users/studies")
    public ResponseEntity<List<StudyResponse>> getUserStudyList(
            @LoginUser Member member
    ) {
        return ResponseEntity.ok(studyService.getStudyListByMemberId(member.getId()));
    }

}
