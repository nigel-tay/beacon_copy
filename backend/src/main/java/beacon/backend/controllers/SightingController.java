package beacon.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import beacon.backend.exceptions.AppException;
import beacon.backend.models.Sighting;
import beacon.backend.records.SightingDto;
import beacon.backend.services.PetService;
import beacon.backend.services.SightingService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
@RequestMapping("/api")
public class SightingController {

    @Autowired
    private SightingService sightingService;

    @Autowired
    private PetService petService;
    
    
    @GetMapping("/sightings")
    public ResponseEntity<String> getAllSightings(
        @RequestParam int page,
        @RequestParam int pageSize,
        @RequestParam String reportId) {
        Optional<List<Sighting>> sightings = sightingService.getAllSightings(page, pageSize, reportId);
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        
        if (sightings.isPresent()) {
            sightings.ifPresent(pets -> {
                JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
                pets.forEach(sighting -> {
                    if (sighting != null) {
                        JsonObjectBuilder sightingBuilder = Json.createObjectBuilder()
                        .add("id", sighting.getId())
                        .add("user_id", sighting.getUser_id())
                        .add("report_id", sighting.getReport_id())
                        .add("content", sighting.getContent())
                        .add("date_time", sighting.getDate_time())
                        .add("image", sighting.getImage())
                        .add("deleted", sighting.getDeleted());
                        jsonArrayBuilder.add(sightingBuilder);
                    }
                });
                
                JsonArray sightingArray = jsonArrayBuilder.build();
                jsonObjectBuilder.add("sightings", sightingArray);
            });
        }
        else {
            throw new AppException("Requested Page does not exist", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(jsonObjectBuilder.build().toString());
    }
            
    @GetMapping("/sightings/count")
    public ResponseEntity<String> getTotalPages() {
        JsonObject jo = Json.createObjectBuilder()
        .add("pages", sightingService.getTotalPages())
        .build();
        return ResponseEntity.ok(jo.toString());
    }
    
    @PostMapping("/sightings")
    public ResponseEntity<String> postSighting(@RequestBody SightingDto sightingDto) {
        sightingService.postSighting(sightingDto);
        JsonObject jo = Json.createObjectBuilder()
                        .add("message", "Sighting created")
                        .build();
        return ResponseEntity.ok(jo.toString());
    }

    @PutMapping("/sightings/{petId}")
    public ResponseEntity<String> setPetAsLost(@PathVariable String petId, @RequestBody String lostValue) {
        petService.putPetLostValue(petId, lostValue);
        JsonObject jo = Json.createObjectBuilder()
                        .add("message", "Pet set as lost")
                        .build();
        return ResponseEntity.ok(jo.toString());
    }
}
