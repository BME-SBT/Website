package hu.schdesign.solarboat.service;

import hu.schdesign.solarboat.dao.ArticleRepository;
import hu.schdesign.solarboat.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public Article addArticle(Article article) {

        return articleRepository.save(article);
    }

    public Iterable<Article> getAllNews() {
        return articleRepository.findAll(Sort.by("date").descending());
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public void deleteArticleById(Long id) {
        articleRepository.deleteById(id);
    }

    public void updateArticle(Article article) {
        articleRepository.save(article);
    }

    public Page<Article> getPage(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("date").descending());
        Page<Article> pagedResult = articleRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult;
        } else {
            return null;
        }
    }
}
