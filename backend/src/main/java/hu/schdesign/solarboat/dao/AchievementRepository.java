package hu.schdesign.solarboat.dao;

import hu.schdesign.solarboat.model.Achievement;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends PagingAndSortingRepository<Achievement, Long> {


}
