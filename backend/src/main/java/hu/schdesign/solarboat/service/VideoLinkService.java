package hu.schdesign.solarboat.service;

import hu.schdesign.solarboat.dao.VideoLinkRepository;
import hu.schdesign.solarboat.model.VideoLink;
import org.hibernate.validator.internal.constraintvalidators.bv.time.pastorpresent.PastOrPresentValidatorForLocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoLinkService {

    VideoLinkRepository videoLinkRepository;

    @Autowired
    public VideoLinkService(VideoLinkRepository videoLinkRepository) {
        this.videoLinkRepository = videoLinkRepository;
    }

    public List<VideoLink> getAllLinks(){
        List<VideoLink> linkList = videoLinkRepository.findAll();
        if(linkList.size() > 0) {
            return linkList;
        } else {
            return new ArrayList<VideoLink>();
        }
    }
    public Optional<VideoLink> findLinkById(Long id){
        Optional<VideoLink> list = videoLinkRepository.findById(id);
        return list;
    }
    public VideoLink addVideoLink(VideoLink link){
        return videoLinkRepository.save(link);
    }

    public void deleteVideoLinkById(Long id){
        Optional<VideoLink> link = videoLinkRepository.findById(id);
        if(link.isPresent()){
            videoLinkRepository.deleteById(id);
        }
        else{
            System.out.println("nincs ilyen elem");
        }
    }
}
