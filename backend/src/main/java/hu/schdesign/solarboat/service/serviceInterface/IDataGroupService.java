package hu.schdesign.solarboat.service.serviceInterface;

import hu.schdesign.solarboat.model.BoatData;
import hu.schdesign.solarboat.model.DataGroup;
import hu.schdesign.solarboat.model.ResponseBoatData;
import hu.schdesign.solarboat.model.DataPair;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public interface IDataGroupService {
    DataGroup startDataGroup();
    DataGroup closeDataGroup();
    Iterable<DataGroup> getAllDataGroups();
    ResponseBoatData getLastClosedDataGroup();
    ResponseBoatData getActiveDataGroup();
    ResponseBoatData getDataGroupById(Long id);
    Optional<DataGroup> getDataGroupByDate(LocalDateTime date);
    ResponseBoatData getLastDataGroup();
    ArrayList<DataPair<Long, String>> getDatesAndIds();
    void deleteAll();
    void deleteFirst();
    void deleteById(Long id);
    DataGroup addBoatData(BoatData boatData);
    void exportAll(HttpServletResponse response) throws Exception;
    void exportById(Long id, HttpServletResponse response) throws Exception;
    void exportLast(HttpServletResponse response) throws Exception;
    void exportCSV(HttpServletResponse response) throws Exception;
    Optional<DataGroup> findById(Long id);
}
