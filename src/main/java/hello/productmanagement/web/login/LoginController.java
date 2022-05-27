package hello.productmanagement.web.login;

import hello.productmanagement.domain.login.LoginService;
import hello.productmanagement.domain.member.Member;
import hello.productmanagement.web.SessionConst;
import hello.productmanagement.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
//    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm){
        return "/login/loginForm";
    }

    @PostMapping("/login")
    public String loginForm(@Valid @ModelAttribute LoginForm form,
                            BindingResult result,
                            HttpServletRequest request){
        if (result.hasErrors()){
            return "/login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(),form.getPassword());
        log.info("login? {}",loginMember);

        if (loginMember == null){
            result.reject("loginFail","아이디 또는 비밀번호 맞지 않습니다.");
            return "/login/loginForm";
        }
        // 로그인 성공
        //1.세션이 있으면 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //2. 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "redirect:/";
    }



}
