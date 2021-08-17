package hu.schdesign.solarboat.api;

import hu.schdesign.solarboat.model.BoatData;
import hu.schdesign.solarboat.model.DataGroup;
import hu.schdesign.solarboat.model.ResponseBoatData;
import hu.schdesign.solarboat.model.DataPair;
import hu.schdesign.solarboat.service.BoatDataService;
import hu.schdesign.solarboat.service.DataGroupService;
import hu.schdesign.solarboat.service.FileStorageService;
import hu.schdesign.solarboat.service.NotificationDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequestMapping("api/dataGroup")
@RestController
public class DataGroupController {
    private final DataGroupService dataGroupService;
    private final BoatDataService boatDataService;
    private final FileStorageService fileStorageService;
    private final NotificationDispatcher notificationDispatcher;
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    DataGroupController(DataGroupService dataGroupService, BoatDataService boatDataService,
                        FileStorageService fileStorageService, NotificationDispatcher notificationDispatcher) {
        this.dataGroupService = dataGroupService;
        this.boatDataService = boatDataService;
        this.fileStorageService = fileStorageService;
        this.notificationDispatcher = notificationDispatcher;
    }

    @PostMapping(path = "boatData", consumes = "application/json", produces = "application/json")
    public DataGroup addBoatData(@RequestBody BoatData boatData) {
        DataGroup dataGroup = dataGroupService.addBoatData(boatDataService.postData(boatData));
//        BoatDataConverter converter = new BoatDataConverter();
//        notificationDispatcher.dispatch(converter.convertBoatDataToResponseBoatData(dataGroup.getBoatDataList().get(dataGroup.getBoatDataList().size() - 1)));
        return dataGroup;
    }

    @PostMapping(path="start", consumes = "application/json", produces = "application/json")
    public DataGroup startDataGroup() {
        return dataGroupService.startDataGroup();
    }

    @PostMapping(path = "stop" ,consumes = "application/json", produces = "application/json")
    public void closeDataGroup() {
         dataGroupService.closeDataGroup();
    }

    /*
     @GetMapping
         public Iterable<DataGroup> getAllDataGroups(){
             return dataGroupService.getAllDataGroups();
         }
         */
    @Secured("ROLE_USER")
    @GetMapping(path = "lastclosed")
    public ResponseBoatData getLastClosedDataGroup() {
        return dataGroupService.getLastClosedDataGroup();
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "active")
    public ResponseBoatData getActiveDataGroup() {
        return dataGroupService.getActiveDataGroup();
    }

    @Secured("ROLE_USER")
    @GetMapping
    public ResponseBoatData getResponseBoatData() {
        return dataGroupService.getLastDataGroup();
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "response/{id}")
    public ResponseBoatData getResponseBoatData(@PathVariable("id") Long id) {
        return dataGroupService.getDataGroupById(id);
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "ids")
    public ArrayList<DataPair<Long, String>> getIds() {
        return dataGroupService.getDatesAndIds();
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "{id}")
    public ResponseBoatData getDataGroupById(@PathVariable("id") Long id) {
        return dataGroupService.getDataGroupById(id);
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "/date")
    public Optional<DataGroup> getDataGroupByDate(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime date) {
        return dataGroupService.getDataGroupByDate(date);
    }

    @Secured("ROLE_USER")
    @DeleteMapping
    public void deleteAllDataGroups() {
        dataGroupService.deleteAll();
    }

    @Secured("ROLE_USER")
    @DeleteMapping(path = "first")
    public void deleteFirstDataGroups() {
        dataGroupService.deleteFirst();
    }

    @Secured("ROLE_USER")
    @DeleteMapping(path = "{id}")
    public void deleteDataGroupById(@PathVariable("id") Long id) {
        dataGroupService.deleteById(id);
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "export/{id}")
    public ResponseEntity<Resource> exportById(HttpServletResponse response, HttpServletRequest request, @PathVariable("id") Long id) throws Exception {
        dataGroupService.exportById(id, response);
        return exportFile(request);
    }
//
//    @Secured({"ROLE_USER", "ROLE_ADMIN"})
//    @GetMapping(path = "export/lastfive")
//    public ResponseEntity<Resource> exportLast(HttpServletResponse response, HttpServletRequest request) throws Exception {
//        dataGroupService.exportLast(response);
//        return exportFile(request);
//    }

    @Secured("ROLE_USER")
    @GetMapping(path = "export")
    public ResponseEntity<Resource> exportAll(HttpServletResponse response, HttpServletRequest request) throws Exception {
        dataGroupService.exportAll(response);
        return exportFile(request);
    }

    private ResponseEntity<Resource> exportFile(HttpServletRequest request) throws Exception {
        Resource resource = fileStorageService.loadFileAsResource("boatdata.csv");

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
