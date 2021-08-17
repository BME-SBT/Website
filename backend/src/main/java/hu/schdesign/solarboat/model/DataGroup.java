package hu.schdesign.solarboat.model;

import hu.schdesign.solarboat.csv.CsvPrintable;
import hu.schdesign.solarboat.model.Boat.Coordinates;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class DataGroup implements CsvPrintable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoatData> boatDataList;
    @DateTimeFormat(pattern = "yyyy:MM:dd'T'HH:mm")
    private LocalDateTime date;
    private boolean isActive;

    public DataGroup() {
        this.boatDataList = new ArrayList<>();
        this.date = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public List<BoatData> getBoatDataList() {
        return boatDataList;
    }

    public void addBoatData(BoatData boatData) {
        this.boatDataList.add(boatData);
    }

    public String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return dtf.format(date);
    }

    public void setDate() {
        this.date = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBoatDataList(List<BoatData> boatDataList) {
        this.boatDataList = boatDataList;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String printCsv() {

        StringBuilder data = new StringBuilder();


        for (BoatData b : boatDataList) {
            data.append(id.toString()).append(CSV_SEPARATOR)
                    .append(getDate()).append(CSV_SEPARATOR)
                    .append(b.printCsv())
                    .append(System.lineSeparator());
        }
        return data.toString();

    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
