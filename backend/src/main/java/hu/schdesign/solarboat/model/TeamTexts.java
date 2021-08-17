package hu.schdesign.solarboat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class TeamTexts {
    @NotNull
    private String text_hu;
    @NotNull
    private String text_en;


    public TeamTexts(@JsonProperty("text_hu") String text_hu, @JsonProperty("text_en") String text_en) {

        this.text_hu = text_hu;
        this.text_en = text_en;
    }

    public TeamTexts(){

    }

    public String getText_hu() {
        return text_hu;
    }

    public void setText_hu(String text_hu) {
        this.text_hu = text_hu;
    }

    public String getText_en() {
        return text_en;
    }

    public void setText_en(String text_en) {
        this.text_en = text_en;
    }
}
