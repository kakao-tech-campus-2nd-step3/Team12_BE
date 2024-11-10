package katecam.luvicookie.ditto.domain.member.api;

import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.application.MemberService;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.dto.memberCreateRequestDTO;
import katecam.luvicookie.ditto.domain.member.dto.memberResponseDTO;
import katecam.luvicookie.ditto.domain.member.dto.memberUpdateRequestDTO;
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

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // TODO
    @GetMapping("/user/login/kakao")
    public String login(){
        log.info("로그인");
        return "oauthLogin";
    }

    @ResponseBody
    @PostMapping("/api/auth")
    public ResponseEntity<memberResponseDTO> signup(@RequestBody memberCreateRequestDTO memberCreateRequestDTO){
        Member member = memberService.registerMember(memberCreateRequestDTO);
        return ResponseEntity.ok(new memberResponseDTO(member));
    }

    @ResponseBody
    @GetMapping("/api/users")
    public ResponseEntity<memberResponseDTO> getUserInfo(@LoginUser Member member){
        return ResponseEntity.ok(new memberResponseDTO(member));
    }

    @ResponseBody
    @PutMapping("/api/users")
    public ResponseEntity<memberResponseDTO> updateUserInfo(@LoginUser Member member, @RequestBody memberUpdateRequestDTO memberDTO){
        Member updateMember = memberService.updateMember(memberDTO, member);
        return ResponseEntity.ok(new memberResponseDTO(updateMember));
    }

    @ResponseBody
    @PutMapping("/api/users/profileImage")
    public ResponseEntity<?> updateProfileImage(
            @LoginUser Member member,
            @RequestPart MultipartFile profileImage){
        memberService.updateProfileImage(profileImage, member.getId());
        return ResponseEntity.ok(new ResponseEntity<>(HttpStatus.OK));
    }

}
