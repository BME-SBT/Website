package hu.schdesign.solarboat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.schdesign.solarboat.SponsorEnumConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table()
public class Sponsor {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String link;
    private String picture;
    private  int rowNumb;
    private SponsorGroup groupId;


    public Sponsor(@JsonProperty("name") String name, @JsonProperty("link") String link,
                   @JsonProperty("group") SponsorGroup group, @JsonProperty("picture") String picture, @JsonProperty("row") int rowNumb){
        this.name = name;
        this.link = link;
        this.picture = picture;
        this.groupId = group;
        this.rowNumb = rowNumb;

    }

    public Sponsor() {
        this.name = null;
        this.link = null;
        this.picture = null;
        this.groupId = null;
        this.rowNumb = 0;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public SponsorGroup getGroup() {
        return groupId;
    }

    public void setGroup(SponsorGroup group) {
        this.groupId = group;
    }

    public int getRow() {
        return rowNumb;
    }


}
