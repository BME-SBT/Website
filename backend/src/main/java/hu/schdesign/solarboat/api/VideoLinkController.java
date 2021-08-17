package hu.schdesign.solarboat.api;

import hu.schdesign.solarboat.model.VideoLink;
import hu.schdesign.solarboat.service.VideoLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/videolink")
public class VideoLinkController {

    VideoLinkService videoLinkService;

    @Autowired
    public VideoLinkController(VideoLinkService videoLinkService) {
        this.videoLinkService = videoLinkService;
    }

    @GetMapping
    public ResponseEntity<List<VideoLink>> getAllLinks(){
        return ResponseEntity.ok(videoLinkService.getAllLinks());
    }

    @Secured("ROLE_EDITOR")
    @PostMapping
    public ResponseEntity<VideoLink>  addVideoLink(@RequestBody VideoLink link){
        return ResponseEntity.ok(videoLinkService.addVideoLink(link));

    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoLink> findById(@PathVariable Long id) {
        Optional<VideoLink> link = videoLinkService.findLinkById(id);
        if (!link.isPresent()) {
            System.out.println("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(link.get());
    }
    @Secured("ROLE_EDITOR")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!videoLinkService.findLinkById(id).isPresent()) {
            System.out.println("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        videoLinkService.deleteVideoLinkById(id);

        return ResponseEntity.ok().build();
    }

}
