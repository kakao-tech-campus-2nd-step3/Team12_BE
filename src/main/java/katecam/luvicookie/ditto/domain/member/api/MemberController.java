package katecam.luvicookie.ditto.domain.member.api;

import katecam.luvicookie.ditto.domain.assignment.application.AssignmentService;
import katecam.luvicookie.ditto.domain.login.annotation.LoginUser;
import katecam.luvicookie.ditto.domain.member.domain.Member;
import katecam.luvicookie.ditto.domain.member.dto.memberRequestDTO;
import katecam.luvicookie.ditto.domain.member.dto.memberResponseDTO;
import katecam.luvicookie.ditto.domain.member.dto.profileImageDTO;
import katecam.luvicookie.ditto.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/user/login/kakao")
    public String login(){
        log.info("로그인");
        return "oauthLogin";
        //"/oauth2/authorization/kakao"
    }

    @ResponseBody
    @PostMapping("/api/auth")
    public memberResponseDTO signup(@RequestBody memberRequestDTO memberRequestDTO){
        Member member = memberService.registerMember(memberRequestDTO);
        return new memberResponseDTO(member);
    }

    @ResponseBody
    @GetMapping("/api/users")
    public memberResponseDTO getUserInfo(@LoginUser Member member){
        return new memberResponseDTO(member);
    }

    @ResponseBody
    @PutMapping("/api/users")
    public memberResponseDTO updateUserInfo(@LoginUser Member member, @RequestBody memberRequestDTO memberRequestDTO){
        Member updateMember = memberService.updateMember(memberRequestDTO, member);
        return new memberResponseDTO(updateMember);
    }

    @ResponseBody
    @PostMapping("/api/users/profileImage")
    public ResponseEntity<?> updateProfileImage(
            @LoginUser Member member,
            @RequestPart MultipartFile profileImage){
        memberService.updateProfileImage(profileImage, member.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
