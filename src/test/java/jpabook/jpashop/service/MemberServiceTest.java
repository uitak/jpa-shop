package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;
    //@Autowired EntityManager em;

    @Test
    //@Rollback(false)
    public void join() throws Exception {
        // given(주어짐)
        Member member = new Member();
        member.setName("Kim");

        // when(실행)
        Long savedId = memberService.join(member);

        // then(결과)
        //em.flush();
        Assertions.assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void validateDuplicateMember() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("Kim");
        Member member2 = new Member();
        member2.setName("Kim");

        //when
        memberService.join(member1);
        memberService.join(member2);
//        try {
//            memberService.join(member2);
//        } catch(IllegalStateException e) {
//            return;
//        }

        //then
        Assert.fail("예외가 발생해야 한다.");
    }
}