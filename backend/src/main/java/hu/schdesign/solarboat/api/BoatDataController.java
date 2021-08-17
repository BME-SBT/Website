package hu.schdesign.solarboat.api;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import hu.schdesign.solarboat.model.BoatData;
import hu.schdesign.solarboat.model.News;
import hu.schdesign.solarboat.service.BoatDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequestMapping("api/boatdata")
@RestController
public class BoatDataController {
    private final BoatDataService boatDataService;
    @Autowired
    BoatDataController(BoatDataService boatDataService){
        this.boatDataService = boatDataService;
    }
   /* @PostMapping(consumes = "application/json", produces = "application/json")
    public BoatData addData(@RequestBody BoatData boatData){
        return boatDataService.postData(boatData);
    }

    */
   @Secured("ROLE_USER")
    @GetMapping
    public List<BoatData> getAllData(){

        Iterable<BoatData> it = boatDataService.getAllData();
        List<BoatData> list = new ArrayList<>();
        for(BoatData s : it){
            list.add(s);
        }
        return list;
    }

    @GetMapping("/last")
    public Optional<BoatData> getTheLastData() {
        return boatDataService.getTheLastData();
    }




        //create a csv writer
       /* StatefulBeanToCsv<BoatData> writer = new StatefulBeanToCsvBuilder<BoatData>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        //write all users to csv file
        Iterable<BoatData> it = boatDataService.getAllData();
        ArrayList<BoatData> list = new ArrayList<>();
        for(BoatData b : it) {
            list.add(b);
        }
        writer.write(list);



    }

        */

}
