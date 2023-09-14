package beacon.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private String id;
    private String pet_id;
    private String lat;
    private String lng;
    private String date_time;
    private String zone;
    private String description;
    private String closed;
}
