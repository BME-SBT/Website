package hu.schdesign.solarboat.model.Boat;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.schdesign.solarboat.csv.CsvPrintable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class Battery implements CsvPrintable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private final double _in;
    private final double _out;
    private final double SoC;
    private final double temp;

    public Battery(@JsonProperty("in") double in, @JsonProperty("out") double out,
                   @JsonProperty("soC") double soC, @JsonProperty("temp") double temp) {
        this._in = in;
        this._out = out;
        this.SoC = soC;
        this.temp = temp;
    }

    public Battery() {
        this._in = 0;
        this._out = 0;
        this.SoC = 0;
        this.temp = 0;
    }

    public Long getId() {
        return id;
    }

    public double get_in() {
        return _in;
    }

    public double get_out() {
        return _out;
    }

    public double getSoC() {
        return SoC;
    }

    public double getTemp() {
        return temp;
    }

    @Override
    public String printCsv() {
        return new StringBuilder().append(this._in).append(CSV_SEPARATOR)
                .append(this._out).append(CSV_SEPARATOR)
                .append(this.SoC).append(CSV_SEPARATOR)
                .append(this.temp).toString();
    }

    public void setId(Long id) {
        this.id = id;
    }
}
