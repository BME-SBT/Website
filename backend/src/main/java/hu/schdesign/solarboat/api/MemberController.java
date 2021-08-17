package hu.schdesign.solarboat.api;


import hu.schdesign.solarboat.model.Member;
import hu.schdesign.solarboat.model.Team;
import hu.schdesign.solarboat.service.MemberService;
import hu.schdesign.solarboat.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequestMapping("api/member")
@RestController
public class MemberController {
    private final MemberService memberService;
    private final TeamService teamService;

    @Autowired
    public MemberController(MemberService memberService, TeamService teamService) {
        this.memberService = memberService;
        this.teamService = teamService;
    }

    @GetMapping
    public List<Member> getAllMembers() {
        Iterable<Member> it = memberService.getAllMembers();
        List<Member> list = new ArrayList<>();
        for (Member s : it) {
            list.add(s);
        }
        return list;
    }

    @GetMapping(path = "{id}")
    public Optional<Member> getMemberById(@PathVariable("id") Long id) {
        return memberService.getMemberById(id);
    }

    @Secured("ROLE_EDITOR")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public Member addMember(@RequestBody Member member) {
        return memberService.addMember(member);
    }

    @Secured("ROLE_EDITOR")
    @PutMapping(path = "{id}")
    public Member updateMember(@RequestBody Member member) {
        return memberService.updateMember(member);
    }

    @Secured("ROLE_EDITOR")
    @DeleteMapping(path = "{id}")
    public void deleteMemberById(@PathVariable("id") Long id) {
        teamService.deleteMemberFromEveryTeamIfNotLeader(id);
        memberService.deleteMemberById(id);
    }

}
