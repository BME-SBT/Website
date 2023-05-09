package hu.schdesign.solarboat.service;

import hu.schdesign.solarboat.dao.MemberRepository;
import hu.schdesign.solarboat.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final FileStorageService fileStorageService;
    private final String PATH = "members";


    @Autowired
    MemberService(MemberRepository memberRepository, FileStorageService fileStorageService){
        this.memberRepository = memberRepository;
        this.fileStorageService = fileStorageService;
    }

    public Member addMember(Member member) { return memberRepository.save(member); }
    public Iterable<Member> getAllMembers() { return memberRepository.findAll(); }
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }
    public Member updateMember(Member member) { return memberRepository.save(member); }
    public void deleteMemberById(Long id) throws Exception {
        Member member = memberRepository.findById(id).orElseThrow(() -> new Exception("Nincs ilyen csapattag"));
        fileStorageService.deleteFile(member.getPicture(), this.PATH);
        memberRepository.deleteById(id); }
}
