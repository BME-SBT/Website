package hu.schdesign.solarboat.service;

import hu.schdesign.solarboat.dao.NewsRepository;
import hu.schdesign.solarboat.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final FileStorageService fileStorageService;
    private final String PATH = "news";

    @Autowired
    public NewsService(NewsRepository newsRepository, FileStorageService fileStorageService) {
        this.newsRepository = newsRepository;
        this.fileStorageService = fileStorageService;
    }

    public News addNews(News news) {

        return newsRepository.save(news);
    }

    public Iterable<News> getAllNews() {
        return newsRepository.findAll(Sort.by("date").descending());
    }

    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    public void deleteNewsById(Long id) throws Exception {
        News news = newsRepository.findById(id).orElseThrow(() -> new Exception("Nincs ilyen hír"));
        for (String picture : news.getPictures()) {
            fileStorageService.deleteFile(picture, this.PATH);
        }
        newsRepository.deleteById(id);
    }

    public void updateNews(News news) throws Exception {
        News originalNews = newsRepository.findById(news.getId()).orElseThrow(() -> new Exception("Nincs ilyen hír"));
        for (String picture : originalNews.getPictures()) {
            fileStorageService.deleteFile(picture, this.PATH);
        }
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
