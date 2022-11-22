package hu.schdesign.solarboat.api;

import hu.schdesign.solarboat.model.*;
import hu.schdesign.solarboat.service.GalleryImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequestMapping("api/gallery")
@RestController
public class GalleryImageController {
    private final GalleryImageService galleryImageService;

    @Autowired
    public GalleryImageController(GalleryImageService galleryImageService) {
        this.galleryImageService = galleryImageService;
    }

    @Secured("ROLE_EDITOR")
    @PostMapping()
    public ImageGroup addImageGroup(@RequestBody ImageGroup imageGroup){
        return galleryImageService.createImageGroup(imageGroup);
    }

    @Secured("ROLE_EDITOR")
    @DeleteMapping(path = "{id}")
    public void deleteImageGroup(@PathVariable("id") Long id){
         galleryImageService.deleteImageGroupById(id);
    }

    @GetMapping(path = "{id}")
    public Optional<ImageGroup> getImageGroup(@PathVariable("id") Long id){
        return galleryImageService.getImageGroupById(id);
    }
    @GetMapping()
    public Iterable<ResponseImageGroup> getAllImageGroups(){
        return galleryImageService.getImageGroups();
    }
    @Secured("ROLE_EDITOR")
    @PatchMapping(path = "cover/{imageGroupId}")
    public ImageGroup setImageGroupCover(@PathVariable("imageGroupId") Long imageGroupId, @RequestBody Image image){
        return galleryImageService.setCoverImage(imageGroupId, image.getId());
    }
    @Secured("ROLE_EDITOR")
    @PostMapping("/")//(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Image> addImage(@RequestParam("file") MultipartFile file, @RequestParam("imageGroupId") Long groupId) throws URISyntaxException {
        if(file != null){
            if (!Objects.requireNonNull(file.getContentType()).contains("image")){
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.setLocation(new URI("/uploadFile"));
                responseHeaders.set("Error", "The file is not an image");
                return new ResponseEntity<>(null, responseHeaders, HttpStatus.BAD_REQUEST);
            }
        }
        Image image;
        try {
            image = galleryImageService.addImages(file, groupId);
        } catch (IOException e) {
            e.printStackTrace();
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(new URI("/uploadFile"));
            responseHeaders.set("Error", "The file is not an image");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.BAD_REQUEST);

        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(new URI("uploadFile"));
        return new ResponseEntity<>(image, responseHeaders ,HttpStatus.CREATED);
    }
    @Secured("ROLE_EDITOR")
    @PostMapping("/images")//(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ImageGroup> addImages(@RequestParam("files") MultipartFile[] files, @RequestParam("imageGroupId") Long groupId) throws URISyntaxException {
        for(MultipartFile file : files ){

        if(file != null){
            if (!Objects.requireNonNull(file.getContentType()).contains("image")){
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.setLocation(new URI("/uploadFile"));
                responseHeaders.set("Error", "The file is not an image");
                return new ResponseEntity<>(null, responseHeaders, HttpStatus.BAD_REQUEST);
            }
        }
        }
        ImageGroup imageGroup;
        try {
            imageGroup = galleryImageService.addImages(files, groupId);
        } catch (IOException e) {
            e.printStackTrace();
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(new URI("/uploadFile"));
            responseHeaders.set("Error", "The file is not an image");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.BAD_REQUEST);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(new URI("uploadFile"));
        return new ResponseEntity<>(imageGroup, responseHeaders ,HttpStatus.CREATED);
    }
    @Secured("ROLE_EDITOR")
    @DeleteMapping(path = "{imageGroupId}/{imageId}")
    public void deleteImage(@PathVariable("imageGroupId") Long imageGroupId, @PathVariable("imageId") Long imageId){
        galleryImageService.deleteImageById(imageGroupId, imageId);
    }
    @Secured("ROLE_EDITOR")
    @PostMapping(path = "/video/{imageGroupId}")
    public ResponseEntity<VideoLink>  addVideoLink(@RequestBody VideoLink link, @PathVariable("imageGroupId") Long imageGroupId){
        return ResponseEntity.ok(galleryImageService.addVideo(link, imageGroupId));
    }

    @Secured("ROLE_EDITOR")
    @DeleteMapping(path = "/video/{imageGroupId}/{videoId}")
    public void deleteVideo(@PathVariable("imageGroupId") Long imageGroupId, @PathVariable("videoId") Long videoId){
        galleryImageService.deleteVideoById(imageGroupId, videoId);
    }








}
