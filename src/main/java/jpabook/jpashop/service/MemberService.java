package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 회원가입
    @Transactional
    public Long create(Member member) {
        validateDuplicateMember(member);
        return this.memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = this.memberRepository.findByName(member.getUsername());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    @Transactional
    public void patch(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setUsername(name);
    }

    // 회원 전체 조회

    public List<Member> findMembers() {
        return this.memberRepository.findAll();
    }

    public Member findOne(Long id) {
        return this.memberRepository.findOne(id);

    }
}
