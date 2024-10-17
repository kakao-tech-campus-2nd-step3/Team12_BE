package katecam.luvicookie.ditto.domain.member.controller;

import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.domain.PrincipalDetail;
import katecam.luvicookie.ditto.domain.member.dto.memberRequestDTO;
import katecam.luvicookie.ditto.domain.member.dto.memberResponseDTO;
import katecam.luvicookie.ditto.domain.member.dto.profileImageDTO;
import katecam.luvicookie.ditto.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/user/login/kakao")
    public String login(){
        log.info("로그인");
        return "oauthLogin";
    }

    @ResponseBody
    @PostMapping("/api/auth")
    public memberResponseDTO signup(@RequestBody memberRequestDTO memberRequestDTO){
        Member member = memberService.registerMember(memberRequestDTO);
        return new memberResponseDTO(member);
    }

    @ResponseBody
    @GetMapping("/api/users")
    public memberResponseDTO getUserInfo(@AuthenticationPrincipal PrincipalDetail user){
        return new memberResponseDTO(user.getUser());
    }

    @ResponseBody
    @PutMapping("/api/users/{userId}")
    public memberResponseDTO updateUserInfo(@PathVariable Integer userId, @RequestBody memberRequestDTO memberRequestDTO){
        Member member = memberService.updateMember(memberRequestDTO, userId);
        return new memberResponseDTO(member);
    }

    @ResponseBody
    @PostMapping("/api/users/profileImage")
    public ResponseEntity<?> updateProfileImage(@AuthenticationPrincipal PrincipalDetail user, @RequestBody profileImageDTO profileImageDTO){
        memberService.updateProfileImage(profileImageDTO, user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
