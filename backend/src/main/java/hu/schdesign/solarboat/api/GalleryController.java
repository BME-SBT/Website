package hu.schdesign.solarboat.api;

import hu.schdesign.solarboat.model.GalleryPicture;
import hu.schdesign.solarboat.service.FileStorageService;
import hu.schdesign.solarboat.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequestMapping("api/gallery")
@RestController
public class GalleryController {
    private final GalleryService galleryService;
    @Autowired
    GalleryController(GalleryService galleryService, FileStorageService fileStorageService){
        this.galleryService = galleryService;
    }

/*    @Secured("ROLE_ADMIN")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public GalleryPicture addGalleryPicture(@Validated @RequestBody GalleryPicture galleryPicture){
        return galleryService.addGalleryPicture(galleryPicture);
    }*/
    @Secured("ROLE_EDITOR")
    @PostMapping()//(consumes = "application/json", produces = "application/json")
    public ResponseEntity<GalleryPicture> addPicture(@RequestParam("file") MultipartFile file, @RequestParam("title_hu") String titleHu, @RequestParam("title_en")String titleEn) throws URISyntaxException {
        if(file == null){
             if (!file.getContentType().contains("image")){
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(new URI("/uploadFile"));
            responseHeaders.set("Error", "The file is not an image");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.BAD_REQUEST);
            }
        }
        GalleryPicture picture = null;
        try {
            picture = galleryService.addPicture(file, titleHu, titleEn);
        } catch (IOException e) {
            e.printStackTrace();
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(new URI("/uploadFile"));
            responseHeaders.set("Error", "The file is not an image");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.BAD_REQUEST);

        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(new URI("uploadFile"));
        return new ResponseEntity<>(picture, responseHeaders ,HttpStatus.CREATED);
    }
    @Secured("ROLE_EDITOR")
    @PutMapping
    public GalleryPicture updatePicture(@Validated @RequestBody GalleryPicture galleryPicture){
        return galleryService.updatePicture(galleryPicture);
    }
    @GetMapping
    public Iterable<GalleryPicture> getAllPictures(){
        return galleryService.getAllPictures();
    }
    @GetMapping(path = "{id}")
    public Optional<GalleryPicture> getPictureById(@PathVariable("id") Long id){
        return galleryService.getPictureById(id);
    }
    @Secured("ROLE_EDITOR")
    @DeleteMapping(path = "{id}")
    public void deletePicture(@PathVariable("id") Long id){
        galleryService.deletePictureById(id);
    }
}
