package hu.schdesign.solarboat.model;

public class ResponseImageGroup {
    private long id;
    private String name_hu;
    private String name_en;
    private Image coverImage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName_hu() {
        return name_hu;
    }

    public void setName_hu(String name_hu) {
        this.name_hu = name_hu;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public Image getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
    }
}
