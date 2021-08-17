package hu.schdesign.solarboat.model.Boat;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.schdesign.solarboat.csv.CsvPrintable;

import javax.persistence.*;

@Table
@Entity
public class Motor implements CsvPrintable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private final double RpM;
    private final double temp;

    public Motor(@JsonProperty("rpM") double rpM, @JsonProperty ("temp") double temp) {
        RpM = rpM;
        this.temp = temp;
    }

    public Motor() {
        RpM = 0;
        this.temp = 0;
    }

    public double getRpM() {
        return RpM;
    }

    public double getTemp() {
        return temp;
    }

    @Override
    public String printCsv() {
        return new StringBuilder()
                .append(this.RpM).append(CSV_SEPARATOR)
                .append(this.temp).toString();
    }

    public void setId(Long id) {
        this.id = id;
    }
}
