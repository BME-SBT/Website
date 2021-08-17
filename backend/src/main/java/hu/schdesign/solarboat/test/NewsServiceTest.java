package hu.schdesign.solarboat.test;

import hu.schdesign.solarboat.dao.NewsRepository;
import hu.schdesign.solarboat.model.News;
import hu.schdesign.solarboat.service.NewsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@WebAppConfiguration
@RunWith(SpringRunner.class)
public class NewsServiceTest {
    @TestConfiguration
    static class NewsServiceIntegrationTestContextConfiguration {
        @Bean
        public NewsService newsService() {
            return new NewsService();
        }
    }

    @Autowired
    private NewsService newsService;
    @MockBean
    private NewsRepository newsRepository;

    @Before
    public void setup() {
        News news = new News();
        news.setTitle_hu("Cim");
        news.setTitle_en("Title");
        news.setContent_en("Content");
        news.setContent_hu("Tartalom");

        Long newsId = (long) 1;
        Mockito.when(newsRepository.findById(newsId)).thenReturn(java.util.Optional.of(news));
    }

    @Test
    public void whenValidId_thenNewsShouldBeFound() {
        Long id = (long) 1;
        String Title_hu = "Cim";
        newsService.getNewsById(id).ifPresent(found -> assertThat(found.getTitle_hu(), is(Title_hu)));
    }
}
