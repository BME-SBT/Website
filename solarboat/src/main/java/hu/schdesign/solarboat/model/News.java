package hu.schdesign.solarboat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Table
public class News {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    //private Date date;
    private LocalDateTime date;
    private String title_hu;
    private String content_hu;
    private String title_en;
    private String content_en;
    private String picture;

    public News(){
        this.title_hu = null;
        this.content_hu = null;
        this.title_en = null;
        this.content_en = null;
        this.picture = null;
        this.date = null;

    }

    public News(@JsonProperty("title_hu")String title_hu, @JsonProperty("content_hu") String content_hu, @JsonProperty("title_en")String title_en, @JsonProperty("content_en") String content_en, @JsonProperty("picture") String picture){
        this.title_hu = title_hu;
        this.content_hu = content_hu;
        this.title_en = title_en;
        this.content_en = content_en;
        this.picture = picture;
       // this.date = new Date();
        this.date = LocalDateTime.now();

    }


    public Long getId() {
        return id;
    }

    /*public Date getDate() {
       return this.date;

    }

    public void setDate() {
        this.date = new Date();
    }

     */

    public void setDate() {
        this.date = LocalDateTime.now();
    }

    public String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return dtf.format(date);
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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
}
