package hu.schdesign.solarboat.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class ImageGroup {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String name_hu;
    @NotNull
    private String name_en;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
    private Long coverImageId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoLink> videos;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date = LocalDate.now();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName_hu() {
        return name_hu;
    }

    public void setName_hu(String groupName) {
        this.name_hu = groupName;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
    public void addImage (Image image){
        if(this.images == null){
            this.images = new ArrayList<Image>();
        }
        images.add(image);
    }

    public List<VideoLink> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoLink> videos) {
        this.videos = videos;
    }

    public Long getCoverImageId() {
        return coverImageId;
    }

    public void setCoverImageId(Long coverImageId) {
        this.coverImageId = coverImageId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
