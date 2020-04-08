package hu.schdesign.solarboat.dao;

import hu.schdesign.solarboat.model.BoatData;
import hu.schdesign.solarboat.model.DataGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface DataGroupRepository extends CrudRepository<DataGroup, Long> {
    Optional<DataGroup> findByDate(LocalDateTime date);
    Optional<DataGroup> findTopByOrderByIdDesc();
   // Iterable<DataGroup> findTop5ByOrderByIdDesc();
    Optional<DataGroup> deleteTopByOrderByIdAsc();
}
