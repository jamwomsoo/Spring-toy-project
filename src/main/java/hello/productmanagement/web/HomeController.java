package hello.productmanagement.web;

import hello.productmanagement.domain.member.Member;
import hello.productmanagement.domain.member.MemberRepository;
import hello.productmanagement.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {


//    @GetMapping("/")
//    public String homeLogin(HttpServletRequest request, Model model){
//
//        //세션이 없으면 home
//        HttpSession session = request.getSession(false);
//        if (session == null){
//            return "home";
//        }
//
//        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
//        //세션에 회원 데이터가 없으면 home
//        if(loginMember ==null){
//            return "home";
//        }
//
//        //세션이 유지되면 로그인으로 이동
//        model.addAttribute("member",loginMember);
//        return "loginHome";
//    }

    @GetMapping("/")
    public String homeLogin(@SessionAttribute(name=SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){

        log.info("LogInMember = {}",loginMember);

        //세션에 회원 데이터가 없으면 home
        if(loginMember == null){
            log.info("NO SESSION NO MEMBER");
            return "home";}

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome";
    }
}
