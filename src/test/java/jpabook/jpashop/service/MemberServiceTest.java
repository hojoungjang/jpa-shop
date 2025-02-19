package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    // 회원가입
    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setUsername("Kim");

        // when
        Long memberId = memberService.create(member);

        // then
        // assertSame(member, memberRepository.findOne(memberId));
        assertEquals(member, memberRepository.findOne(memberId));
    }

    @Test()
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setUsername("Kim");

        Member member2 = new Member();
        member2.setUsername("Kim");

        // when
        memberService.create(member1);

        //then
        assertThrows(IllegalStateException.class, () -> memberService.create(member2));
        // fail("예외가 제대로 발생하지 않음");
    }
}