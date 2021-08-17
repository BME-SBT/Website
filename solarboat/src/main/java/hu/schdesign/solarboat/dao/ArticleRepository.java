package hu.schdesign.solarboat.dao;

import hu.schdesign.solarboat.model.Article;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {
}
