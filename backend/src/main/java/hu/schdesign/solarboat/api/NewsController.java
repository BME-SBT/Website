package hu.schdesign.solarboat.api;

import hu.schdesign.solarboat.Exceptions.CustomMessageApiException;
import hu.schdesign.solarboat.UploadFileResponse;
import hu.schdesign.solarboat.dao.NewsRepository;
import hu.schdesign.solarboat.model.News;
import hu.schdesign.solarboat.service.FileStorageService;
import hu.schdesign.solarboat.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin(origins = "*")
@RequestMapping("api/news")
@RestController
public class NewsController {
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Secured("ROLE_EDITOR")
    @PostMapping(consumes = "application/json")
    public News addNews(@Valid @RequestBody News news, Errors errors) {
        if (errors.hasErrors()) {
            // title (min=2, max=50)
            if (news.getTitle_hu().length() > 50 || news.getTitle_hu().length() < 2
                    || news.getTitle_en().length() > 50 || news.getTitle_en().length() < 2) {
                throw new CustomMessageApiException("A magyar és angol címnek minimum 2 és maximum 50 karakter hosszúnak kell lennie.");
            }
            // content (min=15, max=100 000 000)
            if (news.getContent_hu().length() > 100000000 || news.getContent_hu().length() < 15
                    || news.getContent_en().length() > 100000000 || news.getContent_en().length() < 15) {
                throw new CustomMessageApiException("A hír magyar és angol tartalmának minimum 15 és maximum 100 000 000 karakter hosszúnak kell lennie.");
            }
        }
//        //dátum ellenőrzése
//        try {
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = formatter.parse(news.getDate());
//        } catch (ParseException e) {
//            throw new CustomMessageApiException("Érvénytelen dátum");
//        }
        return newsService.addNews(news);
}

    @GetMapping
    public List<News> getAllNews() {

        Iterable<News> it = newsService.getAllNews();
        List<News> list = new ArrayList<>();
        for (News s : it) {
            list.add(s);
        }
        return list;
    }

    @GetMapping(path = "{id}")
    public Optional<News> getNewsById(@PathVariable("id") Long id) {
        return newsService.getNewsById(id);
    }

    @GetMapping(path = "page/{pageNum}")
    public Page<News> getPage(@PathVariable int pageNum) {
        return newsService.getPage(pageNum, 10);
    }

    @GetMapping(path = "mainpage")
    public Page<News> getMainPage() {
        return newsService.getPage(0, 2);
    }

    @Secured("ROLE_EDITOR")
    @DeleteMapping(path = "{id}")
    public void deleteNewsById(@PathVariable("id") Long id) {
        newsService.deleteNewsById(id);
    }

    @Secured("ROLE_EDITOR")
    @PutMapping()
    public void updateNewsById(@Valid @RequestBody News news) {
        newsService.updateNews(news);
    }
}
