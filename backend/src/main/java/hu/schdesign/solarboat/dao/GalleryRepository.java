package hu.schdesign.solarboat.dao;

import hu.schdesign.solarboat.model.GalleryPicture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends CrudRepository<GalleryPicture, Long> {
}
