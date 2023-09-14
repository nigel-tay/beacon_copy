package beacon.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    private String id;
    private String owner_id;
    private String name;
    private String type;
    private String image;
    private Integer lost;
}
