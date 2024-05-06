package hello.login.web.member;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
	private final MemberRepository memberRepository;

	@GetMapping("/add")
	public String addForm(@ModelAttribute("member") Member member){
		return "members/addMemberForm";
	}

	@PostMapping("/add")
	public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult){
		if(bindingResult.hasErrors()){
			// 회원가입시 member 객체에 에러가 있으면 입력 Form 화면(addMemberForm)으로 돌아간다.
			return "members/addMemberForm";
		}

		memberRepository.save(member);
		return "redirect:/"; // 회원가입 성공시 home 화면으로 redirect
	}

}
