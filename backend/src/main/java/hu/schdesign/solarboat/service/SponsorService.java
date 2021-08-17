package hu.schdesign.solarboat.service;

import hu.schdesign.solarboat.dao.SponsorRepository;
import hu.schdesign.solarboat.model.AllSponsors;
import hu.schdesign.solarboat.model.Sponsor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class SponsorService {
    private final SponsorRepository sponsorRepository;

    @Autowired
    SponsorService(SponsorRepository sponsorRepository) {
        this.sponsorRepository = sponsorRepository;
    }

    public Sponsor addSponsor(Sponsor sponsor) {
        Optional<Sponsor> top = sponsorRepository.getTopByGroupIdOrderByOrderNumberDesc(sponsor.getGroupId());
        if (top.isPresent()) {
            sponsor.setOrderNumber(top.get().getOrderNumber() + 1);
        } else {
            sponsor.setOrderNumber(1);
        }
        return sponsorRepository.save(sponsor);
    }

    public AllSponsors getAllSponsors() {
        ArrayList<Sponsor> all = sponsorRepository.findAllByOrderByOrderNumberAsc();
        AllSponsors allSponsors = new AllSponsors();
        for (Sponsor s : all) {
            switch (s.getGroupId()) {
                case TOP:
                    allSponsors.getTop().add(s);
                    break;
                case MAIN:
                    allSponsors.getMain().add(s);
                    break;
                case PARTNER:
                    allSponsors.getPartner().add(s);
                    break;
                case UNI:
                    allSponsors.getUni().add(s);
                    break;
                case OTHER:
                    allSponsors.getOther().add(s);
                    break;
                default:
                    break;
            }
        }
        return allSponsors;
    }
//    public Iterable<Sponsor> getAllSponsors(){
//        return sponsorRepository.findAll();
//    }

    public void deleteSponsorById(Long id) {
        sponsorRepository.deleteById(id);
    }

    public Iterable<Sponsor> changeAllSponsors(Iterable<Sponsor> newSponsors) {
        sponsorRepository.deleteAll();
        return sponsorRepository.saveAll(newSponsors);
    }

    public AllSponsors updateOrder(ArrayList<Sponsor> sponsors){
      sponsorRepository.saveAll(sponsors);
      return this.getAllSponsors();

    }

}
