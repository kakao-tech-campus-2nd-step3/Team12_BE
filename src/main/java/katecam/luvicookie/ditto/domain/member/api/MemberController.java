package katecam.luvicookie.ditto.domain.member.api;

import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.application.MemberService;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.dto.request.memberCreateRequest;
import katecam.luvicookie.ditto.domain.member.dto.response.memberResponse;
import katecam.luvicookie.ditto.domain.member.dto.response.memberUpdateRequest;
import katecam.luvicookie.ditto.domain.study.application.StudyService;
import katecam.luvicookie.ditto.domain.study.dto.response.StudyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final StudyService studyService;

    // TODO
    @GetMapping("/user/login/kakao")
    public String login(){
        log.info("로그인");
        return "oauthLogin";
    }

    @ResponseBody
    @PostMapping("/api/auth")
    public ResponseEntity<memberResponse> signup(@RequestBody memberCreateRequest memberCreateRequest){
        Member member = memberService.registerMember(memberCreateRequest);
        return ResponseEntity.ok(new memberResponse(member));
    }

    @ResponseBody
    @GetMapping("/api/users")
    public ResponseEntity<memberResponse> getUserInfo(@LoginUser Member member){
        return ResponseEntity.ok(new memberResponse(member));
    }

    @ResponseBody
    @PutMapping("/api/users")
    public ResponseEntity<memberResponse> updateUserInfo(@LoginUser Member member, @RequestBody memberUpdateRequest memberDTO){
        Member updateMember = memberService.updateMember(memberDTO, member);
        return ResponseEntity.ok(new memberResponse(updateMember));
    }

    @ResponseBody
    @PutMapping("/api/users/profileImage")
    public ResponseEntity<?> updateProfileImage(
            @LoginUser Member member,
            @RequestPart MultipartFile profileImage){
        memberService.updateProfileImage(profileImage, member.getId());
        return ResponseEntity.ok(new ResponseEntity<>(HttpStatus.OK));
    }

    @ResponseBody
    @GetMapping("/api/users/studies")
    public ResponseEntity<List<StudyResponse>> getUserStudyList(
            @LoginUser Member member
    ) {
        return ResponseEntity.ok(studyService.getStudyListByMemberId(member.getId()));
    }

}
