package hello.login.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MemberRepository {
	private static Map<Long, Member> store = new HashMap<>();
	private static long sequence = 0L;

	public Member save(Member member){
		member.setId(++sequence);
		log.info("save: member={}", member);
		store.put(member.getId(), member);

		return member;
	}

	public Member findById(Long id){
		return store.get(id);
	}

	public Optional<Member> findByLoginId(String loginId){ // 로그인 id로 찾기
		return findAll().stream()
			.filter(member -> loginId.equals(member.getLoginId()))
			.findFirst();
	}

	public List<Member> findAll(){
		// values() : store의 value만 모두 반환
		return new ArrayList<>(store.values());
	}

	public void clearStore(){
		store.clear();
	}
}
