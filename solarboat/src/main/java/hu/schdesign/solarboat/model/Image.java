package hu.schdesign.solarboat.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;

@Entity
@Table
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String image;
    @NotNull
    private String smallImage;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String picture) {
        this.image = picture;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallPicture) {
        this.smallImage = smallPicture;
    }


}
