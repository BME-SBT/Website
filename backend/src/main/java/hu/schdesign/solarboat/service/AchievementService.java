package hu.schdesign.solarboat.service;

import hu.schdesign.solarboat.dao.AchievementPagingRepository;
import hu.schdesign.solarboat.dao.AchievementRepository;
import hu.schdesign.solarboat.dao.NewsPagingRepository;
import hu.schdesign.solarboat.model.Achievement;
import hu.schdesign.solarboat.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AchievementService {

    private AchievementRepository achievementRepository;
    private AchievementPagingRepository achievementPagingRepository;
    @Autowired
    AchievementService(AchievementRepository achievementRepository, AchievementPagingRepository achievementPagingRepository){
        this.achievementRepository = achievementRepository;
        this.achievementPagingRepository = achievementPagingRepository;
    }
    public Iterable<Achievement> getAllAchievements(){

        return achievementRepository.findAll(Sort.by("date").descending());
    }
    public Achievement addAchievement(Achievement achievement) {
        return this.achievementRepository.save(achievement);
    }
   /* public Optional<Achievement> getAchievementById(long id){
        return achievementRepository.findById(id);
    }*/

    public Achievement updateAchievement(Achievement achievement){
        return achievementRepository.save(achievement);
    }
    public void deleteAchievementById(long id){
        achievementRepository.deleteById(id);
    }
    public Page<Achievement> getPage(int pageNo, int pageSize){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("date").descending());
        Page<Achievement> pagedResult = achievementPagingRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult;
        } else {
            return null;
        }
    }


}
