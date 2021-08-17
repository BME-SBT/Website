package hu.schdesign.solarboat.api;

import hu.schdesign.solarboat.model.AllSponsors;
import hu.schdesign.solarboat.model.Sponsor;
import hu.schdesign.solarboat.service.SponsorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("api/sponsor")
@RestController
public class SponsorController {
    private final SponsorService sponsorService;

    @Autowired
    SponsorController(SponsorService sponsorService){
        this.sponsorService = sponsorService;
    }
    @Secured("ROLE_EDITOR")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public Sponsor addSponsor(@Valid @RequestBody Sponsor sponsor){
        return sponsorService.addSponsor(sponsor);
    }
    @GetMapping
    public AllSponsors getAllSponsors(){
        return sponsorService.getAllSponsors();
    }
//    @GetMapping
//    public Iterable<Sponsor> getAllSponsors(){
//        return sponsorService.getAllSponsors();
//    }
    @Secured("ROLE_EDITOR")
    @DeleteMapping(path = "{id}")
    public void deleteSponsorById(@PathVariable("id") Long id){
        sponsorService.deleteSponsorById(id);
    }

    @Secured("ROLE_EDITOR")
    @PutMapping(consumes = "application/json", produces = "application/json")
    public Iterable<Sponsor> changeAllSponsors(@Valid @RequestBody Iterable<Sponsor> newSponsors){
        return this.sponsorService.changeAllSponsors(newSponsors);
    }
    @Secured("ROLE_EDITOR")
    @PutMapping(path = "updateorder")
    public AllSponsors updateOrder(@RequestBody ArrayList<Sponsor> sponsors){
        return sponsorService.updateOrder(sponsors);
    }
}
