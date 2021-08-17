package hu.schdesign.solarboat.dao;

import hu.schdesign.solarboat.model.Sponsor;
import hu.schdesign.solarboat.model.SponsorGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface SponsorRepository extends CrudRepository<Sponsor, Long> {
    Optional<Sponsor> getTopByGroupIdOrderByOrderNumberDesc(SponsorGroup groupId);
    ArrayList<Sponsor> findAllByOrderByOrderNumberAsc();
}
