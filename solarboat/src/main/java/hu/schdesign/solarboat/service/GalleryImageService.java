package hu.schdesign.solarboat.service;

import hu.schdesign.solarboat.dao.ImageGroupRepository;
import hu.schdesign.solarboat.dao.ImageRepository;
import hu.schdesign.solarboat.dao.VideoLinkRepository;
import hu.schdesign.solarboat.model.Image;
import hu.schdesign.solarboat.model.ImageGroup;
import hu.schdesign.solarboat.model.ResponseImageGroup;
import hu.schdesign.solarboat.model.VideoLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GalleryImageService {
    private final ImageRepository imageRepository;
    private final ImageGroupRepository imageGroupRepository;
    private final FileStorageService fileStorageService;
    private final VideoLinkRepository videoLinkRepository;
    private final String PATH = "gallery";
    private final int PICTURE_WIDTH = 1920;
    private final int SMALL_PICTURE_WIDTH = 480;


    @Autowired
    public GalleryImageService(ImageRepository imageRepository, ImageGroupRepository imageGroupRepository, FileStorageService fileStorageService, VideoLinkRepository videoLinkRepository) {
        this.imageRepository = imageRepository;
        this.imageGroupRepository = imageGroupRepository;
        this.fileStorageService = fileStorageService;
        this.videoLinkRepository = videoLinkRepository;
    }

    public ImageGroup createImageGroup(ImageGroup imageGroup) {
        imageGroup.setImages(new ArrayList<>());
        imageGroup.setVideos(new ArrayList<>());
        return imageGroupRepository.save(imageGroup);
    }

    public void deleteImageGroupById(Long id) {
        ImageGroup imageGroup = imageGroupRepository.findById(id).orElseThrow(() -> new RuntimeException("Nincs ilyen csoport"));
        for (Image i : imageGroup.getImages()) {
            fileStorageService.deleteFile(i.getImage(), PATH);
            fileStorageService.deleteFile(i.getSmallImage(), PATH);
        }
        imageGroupRepository.deleteById(id);
    }

    public Image addImages(MultipartFile file, Long imageGroupId) throws IOException {
        Image image = new Image();
        ImageGroup imageGroup = imageGroupRepository.findById(imageGroupId).orElseThrow(() -> new RuntimeException("Nincs ilyen csoport"));
        MultipartFile picture = null;
        MultipartFile smallPicture = null;

        picture = fileStorageService.resizeImage(file, this.PATH, this.PICTURE_WIDTH);
        smallPicture = fileStorageService.resizeImage(file, this.PATH, this.SMALL_PICTURE_WIDTH);
        image.setImage(fileStorageService.storeResizedFile(picture, this.PATH, "")[0]);
        image.setSmallImage(fileStorageService.storeResizedFile(smallPicture, this.PATH, "small")[0]);
        Image savedImage = imageRepository.save(image);
        imageGroup.addImage(savedImage);
        imageGroupRepository.save(imageGroup);
        return savedImage;

    }
    public ImageGroup addImages(MultipartFile[] files, Long imageGroupId) throws IOException {
        ImageGroup imageGroup = imageGroupRepository.findById(imageGroupId).orElseThrow(() -> new RuntimeException("Nincs ilyen csoport"));
        for(MultipartFile file : files){

            Image image = new Image();
            MultipartFile picture = null;
            MultipartFile smallPicture = null;

            picture = fileStorageService.resizeImage(file, this.PATH, this.PICTURE_WIDTH);
            smallPicture = fileStorageService.resizeImage(file, this.PATH, this.SMALL_PICTURE_WIDTH);
            image.setImage(fileStorageService.storeResizedFile(picture, this.PATH, "")[0]);
            image.setSmallImage(fileStorageService.storeResizedFile(smallPicture, this.PATH, "small")[0]);
            Image savedImage = imageRepository.save(image);
            imageGroup.addImage(savedImage);
        }
        return imageGroupRepository.save(imageGroup);

    }

    public void deleteImageById(Long imageGroupId, Long imageId) {
        ImageGroup ig = imageGroupRepository.findById(imageGroupId).orElseThrow(() -> new RuntimeException("Nincs ilyen csoport!"));
        Image image = ig.getImages().stream().filter(i -> i.getId() == imageId).findFirst().orElseThrow(() -> new RuntimeException("Nincs ilyen kép"));
        fileStorageService.deleteFile(image.getImage(), PATH);
        fileStorageService.deleteFile(image.getSmallImage(), PATH);
        ig.getImages().remove(image);
        imageGroupRepository.save(ig);
    }

    public Optional<ImageGroup> getImageGroupById(Long id) {
        return imageGroupRepository.findById(id);
    }
    public List<ResponseImageGroup> getImageGroups(){
        Iterable<ImageGroup>  imageGroups = imageGroupRepository.findByOrderByDateDesc();
        ArrayList<ResponseImageGroup> responseImageGroups = new ArrayList<>();
        for(ImageGroup imageGroup : imageGroups){
            ResponseImageGroup responseImageGroup = new ResponseImageGroup();
            responseImageGroup.setId(imageGroup.getId());
            if(imageGroup.getCoverImageId() != null){
                Optional<Image> coverImage = imageGroup.getImages().stream().filter(image -> image.getId() == imageGroup.getCoverImageId()).findFirst();
                coverImage.ifPresent(responseImageGroup::setCoverImage);
            }
            responseImageGroup.setName_hu(imageGroup.getName_hu());
            responseImageGroup.setName_en(imageGroup.getName_en());
            responseImageGroups.add(responseImageGroup);
        }
        return responseImageGroups;
    }

    public VideoLink addVideo(VideoLink link, Long imageGroupId) {
        ImageGroup imageGroup = imageGroupRepository.findById(imageGroupId).orElseThrow(() -> new RuntimeException("Nincs ilyen csoport"));
//        VideoLink videoLink = new VideoLink(link);
        VideoLink videoLink = videoLinkRepository.save(link);
        imageGroup.getVideos().add(videoLink);
        imageGroupRepository.save(imageGroup);
        return videoLink;
    }

    public ImageGroup setCoverImage(Long imageGroupId, Long imageId) {
        ImageGroup imageGroup = imageGroupRepository.findById(imageGroupId).orElseThrow(() -> new RuntimeException("Nincs ilyen csoport"));
//        VideoLink videoLink = new VideoLink(link);
        imageGroup.setCoverImageId(imageId);

        return imageGroupRepository.save(imageGroup);
    }

    public Image getCoverImage(Long imageGroupId) {
        ImageGroup imageGroup = imageGroupRepository.findById(imageGroupId).orElseThrow(() -> new RuntimeException("Nincs ilyen csoport"));
        return imageGroup.getImages().stream().filter(i -> i.getId() == imageGroup.getCoverImageId()).findFirst().orElseThrow(() -> new RuntimeException("Nincs ilyen kép"));
    }

    public Iterable<ImageGroup> getAllImageGroups(){
        return imageGroupRepository.findAll();
    }

    public void deleteVideoById(Long imageGroupId, Long videoId) {
        ImageGroup imageGroup = imageGroupRepository.findById(imageGroupId).orElseThrow(() -> new RuntimeException("Nincs ilyen csoport"));
        VideoLink videoLink = imageGroup.getVideos().stream().filter(i -> i.getId() == videoId).findFirst().orElseThrow(() -> new RuntimeException("Nincs ilyen videó"));
        imageGroup.getVideos().remove(videoLink);
        imageGroupRepository.save(imageGroup);
    }


}
