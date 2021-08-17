package hu.schdesign.solarboat.service;

import hu.schdesign.solarboat.dao.BoatDataRepository;
import hu.schdesign.solarboat.model.BoatData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BoatDataService {
    private BoatDataRepository boatDataRepository;
    @Autowired
    BoatDataService(BoatDataRepository boatDataRepository){
        this.boatDataRepository = boatDataRepository;
    }
    public BoatData postData(BoatData boatData){return boatDataRepository.save(boatData);}
    public Optional<BoatData> getTheLastData(){
        return boatDataRepository.findTopByOrderByIdDesc();
    }
    public Iterable<BoatData> getAllData(){
        return boatDataRepository.findAll();
    }


}
