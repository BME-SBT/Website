package hu.schdesign.solarboat.model.Boat;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.schdesign.solarboat.csv.CsvPrintable;

import javax.persistence.*;

@Entity
@Table
public class Error implements CsvPrintable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private final String source;
    private final String message;

    public Error(@JsonProperty("source") String source, @JsonProperty("message") String message) {
        this.source = source;
        this.message = message;
    }

    public Error() {
        this.source = null;
        this.message = null;
    }

    public String getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String printCsv() {
        return new StringBuilder().append(this.source).append(CSV_SEPARATOR)
                .append(this.message).toString();
    }

    public void setId(Long id) {
        this.id = id;
    }

}
