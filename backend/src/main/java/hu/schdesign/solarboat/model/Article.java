package hu.schdesign.solarboat.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;

@Entity
@Table
public class Article {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
    @NotNull
    @Size(min=2, max=50)
    private String title_hu;
    @NotNull
    @Size(min=15, max=100000000)
    private String content_hu;
    @NotNull
    @Size(min=2, max=50)
    private String title_en;
    @NotNull
    @Size(min=15, max=100000000)
    private String content_en;
    @NotNull
    private String link;

    public Article(){}
    public Article(@JsonProperty("date") LocalDate date, @JsonProperty("title_hu") String title_hu, @JsonProperty("content_hu") String content_hu, @JsonProperty("title_en") String title_en, @JsonProperty("content_en") String content_en, @JsonProperty("link") String link){
        this.title_hu = title_hu;
        this.content_hu = content_hu;
        this.title_en = title_en;
        this.content_en = content_en;
        this.link = link;
        this.date = LocalDate.now();
        if(date!= null){
            this.date = date;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle_hu() {
        return title_hu;
    }

    public void setTitle_hu(String title_hu) {
        this.title_hu = title_hu;
    }

    public String getContent_hu() {
        return content_hu;
    }

    public void setContent_hu(String content_hu) {
        this.content_hu = content_hu;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getContent_en() {
        return content_en;
    }

    public void setContent_en(String content_en) {
        this.content_en = content_en;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
