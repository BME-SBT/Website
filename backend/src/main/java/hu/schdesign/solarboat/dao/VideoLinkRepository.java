package hu.schdesign.solarboat.dao;

import hu.schdesign.solarboat.model.VideoLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoLinkRepository extends JpaRepository<VideoLink, Long> {

}
