package hu.schdesign.solarboat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table()
public class VideoLink {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Size(min=5, max=2000)
    private String link;

    public VideoLink(@JsonProperty("link") String link) {
        this.link = link;
    }

    public VideoLink() {
        link = null;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getId() {
        return id;
    }
}
