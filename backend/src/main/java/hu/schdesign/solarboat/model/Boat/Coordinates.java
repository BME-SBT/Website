package hu.schdesign.solarboat.model.Boat;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.schdesign.solarboat.csv.CsvPrintable;

import javax.persistence.*;

@Entity
@Table
public class Coordinates implements CsvPrintable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final double x;
    private final double y;
    private final double z;

    public Coordinates(@JsonProperty("x") int x, @JsonProperty("y") int y, @JsonProperty("z") int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinates() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public String printCsv() {
        return new StringBuilder()
                .append(this.x).append(CSV_SEPARATOR)
                .append(this.y).append(CSV_SEPARATOR)
                .append(this.z).toString();

    }

    public void setId(Long id) {
        this.id = id;
    }
}
