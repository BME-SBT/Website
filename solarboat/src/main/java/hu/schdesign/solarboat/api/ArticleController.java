package hu.schdesign.solarboat.api;

import hu.schdesign.solarboat.Exceptions.CustomMessageApiException;
import hu.schdesign.solarboat.model.Article;
import hu.schdesign.solarboat.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequestMapping("api/article")
@RestController
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }
    @Secured("ROLE_EDITOR")
    @PostMapping(consumes = "application/json")
    public Article addArticle(@Valid @RequestBody Article article, Errors errors) {
        if (errors.hasErrors()) {
            // title (min=2, max=50)
            if (article.getTitle_hu().length() > 50 || article.getTitle_hu().length() < 2
                    || article.getTitle_en().length() > 50 || article.getTitle_en().length() < 2) {
                throw new CustomMessageApiException("A magyar és angol címnek minimum 2 és maximum 50 karakter hosszúnak kell lennie.");
            }
            // content (min=15, max=100 000 000)
            if (article.getContent_hu().length() > 100000000 || article.getContent_hu().length() < 15
                    || article.getContent_en().length() > 100000000 || article.getContent_en().length() < 15) {
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
        return articleService.addArticle(article);
    }

    @GetMapping
    public List<Article> getAllNews() {

        Iterable<Article> it = articleService.getAllNews();
        List<Article> list = new ArrayList<>();
        for (Article s : it) {
            list.add(s);
        }
        return list;
    }

    @GetMapping(path = "{id}")
    public Optional<Article> getNewsById(@PathVariable("id") Long id) {
        return articleService.getArticleById(id);
    }

    @GetMapping(path = "page/{pageNum}")
    public Page<Article> getPage(@PathVariable int pageNum) {
        return articleService.getPage(pageNum, 20);
    }

    @GetMapping(path = "mainpage")
    public Page<Article> getMainPage() {
        return articleService.getPage(0, 2);
    }

    @Secured("ROLE_EDITOR")
    @DeleteMapping(path = "{id}")
    public void deleteNewsById(@PathVariable("id") Long id) {
        articleService.deleteArticleById(id);
    }

    @Secured("ROLE_EDITOR")
    @PutMapping()
    public void updateNewsById(@Valid @RequestBody Article article) {
        articleService.updateArticle(article);
    }
}