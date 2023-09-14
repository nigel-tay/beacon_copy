package beacon.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sighting {
    private String id;
    private String user_id;
    private String report_id;
    private String content;
    private String date_time;
    private String image;
    private Integer deleted;
}
