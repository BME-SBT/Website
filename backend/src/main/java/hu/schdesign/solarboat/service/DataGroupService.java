package hu.schdesign.solarboat.service;

import hu.schdesign.solarboat.Converter.BoatDataConverter;
import hu.schdesign.solarboat.dao.DataGroupRepository;
import hu.schdesign.solarboat.model.BoatData;
import hu.schdesign.solarboat.model.DataGroup;
import hu.schdesign.solarboat.model.ResponseBoatData;
import hu.schdesign.solarboat.model.DataPair;
import hu.schdesign.solarboat.service.serviceInterface.IDataGroupService;
import hu.schdesign.solarboat.service.serviceInterface.INotificationDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class DataGroupService implements IDataGroupService {
    @Autowired
    private DataGroupRepository dataGroupRepository;
    private ArrayList<DataGroup> exportList;
    @Autowired
    private INotificationDispatcher notificationDispatcher;

    public DataGroupService() {

    }

    @Override
    public DataGroup startDataGroup() {
        DataGroup dataGroup = new DataGroup();
        dataGroup.setActive(true);
        notificationDispatcher.dispatchBoatActivity(true);
        return dataGroupRepository.save(dataGroup);
    }

    @Override
    public DataGroup closeDataGroup() {
        DataGroup lastGroup = dataGroupRepository.findTopByIsActiveIsTrueOrderByIdDesc().orElseThrow(() -> new RuntimeException("Nincs aktív datacsoport"));
        lastGroup.setActive(false);
        notificationDispatcher.dispatchBoatActivity(false);
        return dataGroupRepository.save(lastGroup);
    }

    @Override
    public Iterable<DataGroup> getAllDataGroups() {
        return dataGroupRepository.findAll();
    }

    @Override
    public Optional<DataGroup> findById(Long id) {
        return dataGroupRepository.findById(id);
    }

    @Override
    public ResponseBoatData getLastClosedDataGroup() {
        Optional<DataGroup> lastGroup = dataGroupRepository.findTopByIsActiveIsFalseOrderByIdDesc();
        BoatDataConverter converter = new BoatDataConverter();
        return lastGroup.map(converter::convertDataGroupToResponseBoatData).orElse(null);
    }

    @Override
    public ResponseBoatData getActiveDataGroup() {
        Optional<DataGroup> lastGroup = dataGroupRepository.findTopByIsActiveIsTrueOrderByIdDesc();
        BoatDataConverter converter = new BoatDataConverter();
        return lastGroup.map(converter::convertDataGroupToResponseBoatData).orElse(null);
    }

    @Override
    public ResponseBoatData getDataGroupById(Long id) {
        BoatDataConverter boatDataConverter = new BoatDataConverter();
        DataGroup group = dataGroupRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Nincs ilyen adat"));
        return boatDataConverter.convertDataGroupToResponseBoatData(group);
    }

    @Override
    public Optional<DataGroup> getDataGroupByDate(LocalDateTime date) {
        return dataGroupRepository.findByDate(date);
    }

    @Transactional
    @Override
    public ResponseBoatData getLastDataGroup() {
        BoatDataConverter boatDataConverter = new BoatDataConverter();
        return boatDataConverter.convertDataGroupToResponseBoatData(dataGroupRepository.findTopByOrderByIdDesc().orElse(new DataGroup()));
    }

    @Override
    public ArrayList<DataPair<Long, String>> getDatesAndIds() {
        ArrayList<DataPair<Long, String>> list = new ArrayList<>();
        Iterable<DataGroup> it = dataGroupRepository.findAllByIsActiveIsFalseOrderByIdAsc();
        for (DataGroup i : it) {
            list.add(new DataPair<Long, String>(i.getId(), i.getDate()));
        }
        return list;
    }

    @Override
    public void deleteAll() {
        dataGroupRepository.deleteAll();
    }

    @Override
    public void deleteFirst() {
        dataGroupRepository.deleteTopByOrderByIdAsc();
    }

    @Override
    public void deleteById(Long id) {
        dataGroupRepository.deleteById(id);
    }


    @Transactional
    @Override
    public DataGroup addBoatData(BoatData boatData) {
        DataGroup originalDataGroup = dataGroupRepository.findTopByIsActiveIsTrueOrderByIdDesc().orElseThrow(() -> new RuntimeException("Nincs aktív adatcsoport!"));
        double actualVelocity = 0;
        double previousVelocity = 0;
        double distance = 0;
        if (originalDataGroup.getBoatDataList().size() > 0) {
            BoatData previous = originalDataGroup.getBoatDataList().get(originalDataGroup.getBoatDataList().size() - 1);
            previousVelocity = previous.getVelocity();
            actualVelocity = this.calculateVelocity(previous, boatData);
            distance = calculateDistance(previousVelocity, actualVelocity, previous, boatData);
            boatData.setVelocity(actualVelocity);
            boatData.setDistance(distance);
            boatData.setSumDistance(distance + previous.getSumDistance());
        }
        originalDataGroup.addBoatData(boatData);
        originalDataGroup = dataGroupRepository.save(originalDataGroup);
        BoatDataConverter converter = new BoatDataConverter();
        notificationDispatcher.dispatchData(converter.convertBoatDataToResponseBoatData(originalDataGroup.getBoatDataList().get(originalDataGroup.getBoatDataList().size() - 1)));

        return originalDataGroup;
    }

    private double calculateDistance(double v1, double v2, BoatData previous, BoatData actual) {
        int deltaT = (int) ChronoUnit.SECONDS.between(previous.getRawDate(), actual.getRawDate());
        return RiemannIntegration(v1, v2, deltaT);
    }

    private double calculateVelocity(BoatData previous, BoatData actual) {
        int deltaT = (int) ChronoUnit.SECONDS.between(previous.getRawDate(), actual.getRawDate());
        double vx = RiemannIntegration(previous.getAcceleration().getX(), actual.getAcceleration().getX(), deltaT);
        double vy = RiemannIntegration(previous.getAcceleration().getY(), actual.getAcceleration().getY(), deltaT);

        return Math.sqrt(vx * vx + vy * vy);
    }

    public static double RiemannIntegration(Double x1, Double x2, int deltaT) {
        return (x2 + x1) * deltaT / 2;
    }

    @Override
    public void exportAll(HttpServletResponse response) throws Exception {
        Iterable<DataGroup> it = dataGroupRepository.findAll();
        ArrayList<DataGroup> list = new ArrayList<>();
        for (DataGroup b : it) {
            list.add(b);
        }
        this.exportList = list;
        exportCSV(response);
    }

    @Override
    public void exportById(Long id, HttpServletResponse response) throws Exception {
        Optional<DataGroup> it = dataGroupRepository.findById(id);
        ArrayList<DataGroup> list = new ArrayList<>();
        if (it.isPresent()) {

            list.add(it.get());
            this.exportList = list;
            exportCSV(response);
        }

    }

    @Override
    public void exportLast(HttpServletResponse response) throws Exception {
        Optional<DataGroup> opt = dataGroupRepository.findTopByOrderByIdDesc();
        ArrayList<DataGroup> list = new ArrayList<>();
        list.add(opt.get());
        this.exportList = list;
        exportCSV(response);
    }

    @Override
    public void exportCSV(HttpServletResponse response) throws Exception {

        //set file name and content type
        String filename = "boatdata.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        char CSV_SEPARATOR = ';'; // it could be a comma or a semi colon


        try (BufferedWriter writer = new BufferedWriter(new FileWriter("../solarboat-app/src/assets/boatdata.csv"))) {
            //  try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/uploaded/boatdata.csv"))) {
            writer.append("groupid").append(CSV_SEPARATOR)
                    .append("date").append(CSV_SEPARATOR)
                    .append("dataid").append(CSV_SEPARATOR)
                    .append("datadate").append(CSV_SEPARATOR)
                    .append("tilt_x").append(CSV_SEPARATOR)
                    .append("tilt_y").append(CSV_SEPARATOR)
                    .append("tilt_z").append(CSV_SEPARATOR)
                    .append("acceleration_x").append(CSV_SEPARATOR)
                    .append("acceleration_y").append(CSV_SEPARATOR)
                    .append("acceleration_z").append(CSV_SEPARATOR)
                    .append("compass_x").append(CSV_SEPARATOR)
                    .append("compass_y").append(CSV_SEPARATOR)
                    .append("compass_z").append(CSV_SEPARATOR)
                    .append("motor_RpM").append(CSV_SEPARATOR)
                    .append("motor_temp").append(CSV_SEPARATOR)
                    .append("battery_in").append(CSV_SEPARATOR)
                    .append("battery_out").append(CSV_SEPARATOR)
                    .append("battery_SoC").append(CSV_SEPARATOR)
                    .append("battery_temp").append(CSV_SEPARATOR)
                    .append("error_source").append(CSV_SEPARATOR)
                    .append("error_message").append(CSV_SEPARATOR)
                    .append("ExtraTemps").append(System.lineSeparator());
            exportList.forEach(data -> {
                try {
                    writer.append(data.printCsv());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
