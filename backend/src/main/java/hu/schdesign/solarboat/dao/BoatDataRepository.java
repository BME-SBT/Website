package hu.schdesign.solarboat.dao;

import hu.schdesign.solarboat.model.BoatData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoatDataRepository extends CrudRepository<BoatData, Long> {
    Optional<BoatData> findTopByOrderByIdDesc();
}
