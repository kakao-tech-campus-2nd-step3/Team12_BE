package katecam.luvicookie.ditto.domain.member.controller;

import katecam.luvicookie.ditto.domain.member.dto.memberDTO;
import katecam.luvicookie.ditto.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
@Slf4j
public class MemberController {

    @GetMapping("/user/login/kakao")
    public String login(){

        log.info("로그인");
        return "oauthLogin";
    }


    @GetMapping("/api/auth")
    public String signup(@RequestParam String socialId, Model model){
        model.addAttribute("socialId", socialId);
        log.info("signup");

        return "signup";
    }

    /*@PostMapping("/api/auth")
    public ResponseEntity<Object> SignUp(@RequestBody HashMap<String, String> map) {
        log.info("save user");
        Integer id = Integer.parseInt(map.get("id"));
        String nickname = map.get("nickname");
        String email = map.get("email");
        String contact = map.get("contact");
        String description = map.get("description");
        String profileImage = map.get("profileImage");
        memberDTO memberDTO = new memberDTO(email, profileImage);
        MemberService.updateMember(memberDTO, id);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }*/


}
