package hu.schdesign.solarboat.service;

import hu.schdesign.solarboat.dao.GalleryRepository;
import hu.schdesign.solarboat.model.GalleryPicture;
import hu.schdesign.solarboat.model.UploadGalleryPictureReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class GalleryService {
    private final GalleryRepository galleryRepository;
    private final FileStorageService fileStorageService;
    private final String PATH = "gallery";
    private final int PICTURE_WIDTH = 1920;
    private final int SMALL_PICTURE_WIDTH = 400;

    @Autowired
    GalleryService(GalleryRepository galleryRepository,
                   FileStorageService fileStorageService){
        this.galleryRepository = galleryRepository;
        this.fileStorageService = fileStorageService;
    }
//    public GalleryPicture addGalleryPicture(GalleryPicture galleryPicture){
//        return galleryRepository.save(galleryPicture);
//    }
    public GalleryPicture addPicture(MultipartFile file, String titleHu, String titleEn) throws IOException {
        GalleryPicture newGalleryPicture = new GalleryPicture();
        newGalleryPicture.setTitle_en(titleEn);
        newGalleryPicture.setTitle_hu(titleHu);
        MultipartFile picture = null;
        MultipartFile smallPicture = null;

            picture = fileStorageService.resizeImage(file, this.PATH, this.PICTURE_WIDTH);
            smallPicture = fileStorageService.resizeImage(file, this.PATH, this.SMALL_PICTURE_WIDTH);

        newGalleryPicture.setPicture(fileStorageService.storeResizedFile(picture, "gallery", ""));
        newGalleryPicture.setSmallPicture(fileStorageService.storeResizedFile(smallPicture, "gallery", "small"));
        return galleryRepository.save(newGalleryPicture);
    }
    public void deletePictureById(Long id){
        //TODO magát a képet is kitörölni
        GalleryPicture gp = galleryRepository.findById(id).orElseThrow(() -> new RuntimeException("Nincs ilyen kép!"));
        fileStorageService.deleteFile(gp.getPicture(), PATH);
        fileStorageService.deleteFile(gp.getSmallPicture(), PATH);
        galleryRepository.deleteById(id);
    }
    public Iterable<GalleryPicture> getAllPictures(){
        return galleryRepository.findAll();
    }
    public Optional<GalleryPicture> getPictureById(Long id){
        return galleryRepository.findById(id);
    }
    public GalleryPicture updatePicture (GalleryPicture galleryPicture){
        return galleryRepository.save(galleryPicture);
    }
}
