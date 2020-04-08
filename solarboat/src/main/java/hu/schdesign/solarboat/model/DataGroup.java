package hu.schdesign.solarboat.model;

import hu.schdesign.solarboat.csv.CsvPrintable;
import hu.schdesign.solarboat.model.Boat.Coordinates;

import javax.persistence.*;
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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoatData> boatDataList;
    private LocalDateTime date;
    private final char CSV_SEPARATOR = ';';

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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return dtf.format(date);
    }

    public void setDate() {
        this.date = LocalDateTime.now();
    }

    @Override
    public String printCsv() {

        StringBuilder data = new StringBuilder();


        for(BoatData b : boatDataList){
            data.append(id.toString()).append(CSV_SEPARATOR)
                    .append(getDate()).append(CSV_SEPARATOR)
                    .append(b.printCsv())
                    .append(System.lineSeparator());
            }
        return data.toString();

    }
}