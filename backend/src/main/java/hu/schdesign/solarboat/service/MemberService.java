package hu.schdesign.solarboat.service;

import hu.schdesign.solarboat.dao.MemberRepository;
import hu.schdesign.solarboat.model.Member;
import hu.schdesign.solarboat.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    @Autowired
    MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public Member addMember(Member member) { return memberRepository.save(member); }
    public Iterable<Member> getAllMembers() { return memberRepository.findAll(); }
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }
    public Member updateMember(Member member) { return memberRepository.save(member); }
    public void deleteMemberById(Long id) {

        memberRepository.deleteById(id); }
}
