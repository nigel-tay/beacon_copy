package beacon.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetFeatures {
    private String id;
    private String pet_id;
    private String features_id;    
}
