package hu.schdesign.solarboat.dao;

import hu.schdesign.solarboat.model.Image;
import hu.schdesign.solarboat.model.ImageGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageGroupRepository extends CrudRepository<ImageGroup, Long> {
    Iterable<ImageGroup> findByOrderByDateDesc();

}
