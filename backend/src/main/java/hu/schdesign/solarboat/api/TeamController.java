package hu.schdesign.solarboat.api;

import hu.schdesign.solarboat.model.News;
import hu.schdesign.solarboat.model.Team;
import hu.schdesign.solarboat.model.TeamTexts;
import hu.schdesign.solarboat.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequestMapping("api/team")
@RestController
public class TeamController {
    private final TeamService teamService;

    @Autowired
    TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public Iterable<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping(path = "{id}")
    public Optional<Team> getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }


    /*@Secured("ROLE_ADMIN")
    @PostMapping
    public Team addTeam(@RequestBody Team team) {
        return teamService.addTeam(team);
    }*/

    @Secured("ROLE_EDITOR")
    @PostMapping(path = "{teamId}/members/{memberId}")
    public Team addMember(@PathVariable("teamId") Long teamId, @PathVariable("memberId") Long memberId) {
        return this.teamService.addMember(teamId, memberId);
    }

    @Secured("ROLE_EDITOR")
    @DeleteMapping(path = "{teamId}/members/{memberId}")
    public Team deleteMember(@PathVariable("teamId") Long teamId, @PathVariable("memberId") Long memberId) {
        return this.teamService.deleteMember(teamId, memberId);
    }

    @Secured("ROLE_EDITOR")
    @PutMapping(path = "{teamId}/description")
    public Team updateDescription(@PathVariable("teamId") Long teamId, @Valid @RequestBody TeamTexts texts) {
        return this.teamService.updateDescription(teamId, texts.getText_hu(), texts.getText_en());
    }

    @Secured("ROLE_EDITOR")
    @PutMapping(path = "{teamId}/name")
    public Team updateTitle(@PathVariable("teamId") Long teamId, @Valid @RequestBody TeamTexts texts) {
        return this.teamService.updateTitle(teamId, texts.getText_hu(), texts.getText_en());
    }

    @Secured("ROLE_EDITOR")
    @PutMapping(path = "{teamId}/leader/{memberId}")
    public Team updateLeader(@PathVariable("teamId") Long teamId, @PathVariable("memberId") Long memberId) {
        return this.teamService.updateLeader(teamId, memberId);
    }

}
