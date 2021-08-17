package hu.schdesign.solarboat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 1, max = 20)
    private String firstName;
    @NotNull
    @Size(min = 1, max = 20)
    private String lastName;
    @Size(min = 1, max = 50)
    private String job_hu;
    @Size(min = 1, max = 50)
    private String job_en;
    @NotNull
    private String picture;
    private String linkedin;

    public Member(@JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("job_hu") String job_hu,
                  @JsonProperty("job_en") String job_en, @JsonProperty("picture") String picture, @JsonProperty("linkedin") String linkedin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.job_hu = job_hu;
        this.job_en = job_en;
        this.picture = picture;
        this.linkedin = linkedin;
    }

    public Member() {
        this.firstName = null;
        this.lastName = null;
        this.job_hu = null;
        this.job_en = null;
        this.picture = null;
        this.linkedin = null;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJob_hu() {
        return job_hu;
    }

    public void setJob_hu(String job_hu) {
        this.job_hu = job_hu;
    }

    public String getJob_en() {
        return job_en;
    }

    public void setJob_en(String job_en) {
        this.job_en = job_en;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }


}
