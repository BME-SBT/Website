package hu.schdesign.solarboat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class GalleryPicture {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String picture;
    @NotNull
    private String smallPicture;
    private String title_hu;
    private String title_en;

    public GalleryPicture(@JsonProperty("title_hu")String title_hu,
                          @JsonProperty("title_en")String title_en,
                          @JsonProperty("picture") String picture,
                          @JsonProperty("smallPicture") String smallPicture) {
        this.picture = picture;
        this.smallPicture = smallPicture;
        this.title_hu = title_hu;
        this.title_en = title_en;
    }
    public GalleryPicture() {
    }

    public long getId() {
        return id;
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

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getSmallPicture() {
        return smallPicture;
    }

    public void setSmallPicture(String smallPicture) {
        this.smallPicture = smallPicture;
    }
}
