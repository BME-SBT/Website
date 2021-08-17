package hu.schdesign.solarboat.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Entity
@Table
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotNull
    @Size(min = 2, max = 50)
    private String title_hu;
    @NotNull
    @Size(min = 2, max = 50)
    private String title_en;
    @Size(min = 2, max = 2000)
    private String description_hu;
    @Size(min = 2, max = 2000)
    private String description_en;
    @NotNull
    @Size(min = 2, max = 50)
    private String location_hu;
    @NotNull
    @Size(min = 2, max = 50)
    private String location_en;
    private String picture;
    private boolean isLast = false;
    private String place_hu;
    private String place_en;


    public Achievement(@JsonProperty("title_hu") String title_hu,
                       @JsonProperty("title_en") String title_en,
                       @JsonProperty("description_hu") String description_hu,
                       @JsonProperty("location_hu") String location_hu,
                       @JsonProperty("description_en") String description_en,
                       @JsonProperty("location_en") String location_en,
                       @JsonProperty("picture") String picture,
                       @JsonProperty("date") LocalDate date,
                       @JsonProperty("place_hu") String place_hu,
                       @JsonProperty("place_en") String place_en,
                       @JsonProperty("isLast") boolean isLast) {

        this.title_hu = title_hu;
        this.title_en = title_en;
        this.description_hu = description_hu;
        this.location_hu = location_hu;
        this.description_en = description_en;
        this.location_en = location_en;
        this.picture = picture;
        this.place_hu = place_hu;
        this.place_en = place_en;
        this.isLast = isLast;
        this.date = LocalDate.now();
        if (date != null) {
            this.date = date;
        }

    }

    public Achievement() {

    }

    public LocalDate getDate() {
        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        //return dtf.format(date);
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription_hu() {
        return description_hu;
    }

    public void setDescription_hu(String description_hu) {
        this.description_hu = description_hu;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getLocation_hu() {
        return location_hu;
    }

    public void setLocation_hu(String location_hu) {
        this.location_hu = location_hu;
    }

    public String getLocation_en() {
        return location_en;
    }

    public void setLocation_en(String location_en) {
        this.location_en = location_en;
    }

    public long getId() {
        return id;
    }

    public String getTitle_hu() {
        return title_hu;
    }

    public void setTitle_hu(String title_hu) {
        this.title_hu = title_hu;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }

    public String getPlace_hu() {
        return place_hu;
    }

    public void setPlace_hu(String place_hu) {
        this.place_hu = place_hu;
    }

    public String getPlace_en() {
        return place_en;
    }

    public void setPlace_en(String place_en) {
        this.place_en = place_en;
    }
}
