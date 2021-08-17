package hu.schdesign.solarboat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class UploadGalleryPictureReply {
    private String pictureTitle;
    private String smallPictureTitle;

    public UploadGalleryPictureReply() {
    }

    public String getPictureTitle() {
        return pictureTitle;
    }

    public void setPictureTitle(String pictureTitle) {
        this.pictureTitle = pictureTitle;
    }

    public String getSmallPictureTitle() {
        return smallPictureTitle;
    }

    public void setSmallPictureTitle(String smallPictureTitle) {
        this.smallPictureTitle = smallPictureTitle;
    }
}