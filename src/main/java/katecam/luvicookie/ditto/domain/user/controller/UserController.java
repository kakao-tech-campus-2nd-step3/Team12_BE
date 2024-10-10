package katecam.luvicookie.ditto.domain.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class UserController {

    @GetMapping("/user/login/kakao")
    public String login(){

        log.info("로그인");
        return "oauthLogin";
    }


    @GetMapping("/user/info")
    public String signup(@RequestParam String socialId, Model model){
        model.addAttribute("socialId", socialId);
        log.info("signup");

        return "signup";
    }



}
