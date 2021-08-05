package hu.schdesign.solarboat.service;

import hu.schdesign.solarboat.dao.NewsPagingRepository;
import hu.schdesign.solarboat.dao.NewsRepository;
import hu.schdesign.solarboat.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    public NewsService() {
    }

//    @Autowired
//    public NewsService(NewsRepository newsRepository){
//        this.newsRepository = newsRepository;
//    }

    public News addNews(News news) {

        return newsRepository.save(news);
    }

    public Iterable<News> getAllNews() {
        return newsRepository.findAll(Sort.by("date").descending());
    }

    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    public void deleteNewsById(Long id) {
        newsRepository.deleteById(id);
    }

    public void updateNews(News news) {
        newsRepository.save(news);
    }

    public Page<News> getPage(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("date").descending());
        Page<News> pagedResult = newsRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult;
        } else {
            return null;
        }
    }
}
