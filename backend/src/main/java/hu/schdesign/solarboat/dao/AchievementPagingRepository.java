package hu.schdesign.solarboat.dao;

import hu.schdesign.solarboat.model.Achievement;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementPagingRepository extends PagingAndSortingRepository<Achievement, Long> {
}
