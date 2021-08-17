package hu.schdesign.solarboat.dao;

import hu.schdesign.solarboat.model.News;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsPagingRepository extends PagingAndSortingRepository<News, Long> {
}
