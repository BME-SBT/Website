package hu.schdesign.solarboat.api;

import hu.schdesign.solarboat.model.Achievement;
import hu.schdesign.solarboat.model.News;
import hu.schdesign.solarboat.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequestMapping("api/achievement")
@RestController
public class AchievementController {
    private AchievementService achievementService;
    @Autowired
    AchievementController(AchievementService achievementService){
        this.achievementService = achievementService;
    }
    @GetMapping
    public List<Achievement> getAllAchievements(){

        Iterable<Achievement> it = achievementService.getAllAchievements();
        List<Achievement> list = new ArrayList<>();
        for(Achievement s : it){
            list.add(s);
        }
        return list;
    }
    @Secured("ROLE_EDITOR")
    @DeleteMapping(path = "{id}")
    public void deleteAchievementById(@PathVariable("id") Long id){
        achievementService.deleteAchievementById(id);
    }
    @Secured("ROLE_EDITOR")
    @PutMapping()
    public void updateAchievementById(@Valid @RequestBody Achievement achievement){
        achievementService.updateAchievement(achievement);
    }
    @Secured("ROLE_EDITOR")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public Achievement addAchievement( @RequestBody Achievement achievement){
        return achievementService.addAchievement(achievement);
    }
    @GetMapping(path= "page/{pageNum}")
    public Page<Achievement> getPage(@PathVariable int pageNum){
        return achievementService.getPage(pageNum, 4);
    }
}
