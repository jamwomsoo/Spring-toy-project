package hello.productmanagement.web.member;

import hello.productmanagement.domain.item.Item;
import hello.productmanagement.domain.member.Member;
import hello.productmanagement.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final  MemberRepository memberRepository;
    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        Member member = new Member();
        member.setLoginId("test");
        member.setPassword("test!");
        member.setName("테스터");
        memberRepository.save(member);

    }
    @GetMapping("/add")
    public String addForm(@ModelAttribute Member member){
        return "/members/addMemberForm";
    }

    @PostMapping("/add")
    public String addForm(@Valid @ModelAttribute("member") Member member,
                          BindingResult result){
        if(result.hasErrors())
        {
            return "members/addMemberForm";
        }

        memberRepository.save(member);
        return "redirect:/";

    }

}
