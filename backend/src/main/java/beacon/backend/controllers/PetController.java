package beacon.backend.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import beacon.backend.exceptions.AppException;
import beacon.backend.models.Report;
import beacon.backend.records.FeaturesDto;
import beacon.backend.records.LostDto;
import beacon.backend.records.PetDto;
import beacon.backend.records.ReportDto;
import beacon.backend.services.PetService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    
    @Autowired
    private PetService petService;

    @GetMapping("/{petId}")
    public ResponseEntity<String> getPetById(@PathVariable String petId) {
        JsonObject petJo = petService.getPetById(petId);
        return ResponseEntity.ok(petJo.toString());
    }

    @GetMapping("/features")
    public ResponseEntity<String> getAllFeatures() {
        JsonArray ja = petService.getAllFeatures();
        return ResponseEntity.ok(ja.toString());
    }

    @GetMapping("/features/{petId}")
    public ResponseEntity<String> getAllFeaturesById(@PathVariable String petId) {
        JsonArray ja = petService.getAllFeaturesById(petId);
        return ResponseEntity.ok(ja.toString());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<String> getPetsByUserId(@PathVariable String userId) {
        JsonObject petListJson = petService.getPetsByUserId(userId);
        return ResponseEntity.ok(petListJson.toString());
    }

    @GetMapping("/reports/{petId}")
    public ResponseEntity<String> getOpenReportByPetId(@PathVariable String petId) {
        JsonObject jo = petService.getOpenReportByPetId(petId);
        
        if (jo != null) {
            return ResponseEntity.ok(jo.toString());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/reports")
    public ResponseEntity<String> getReportsByRegion(
        @RequestParam int page,
        @RequestParam int pageSize,
        @RequestParam String region) {
        Optional<List<Report>> reportOptional = petService.getReportsByRegion(page, pageSize, region);
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        
        if (reportOptional.isPresent()) {
            reportOptional.ifPresent(reports -> {
                JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
                reports.forEach(report -> {
                    if (report != null) {
                        JsonObjectBuilder reportBuilder = Json.createObjectBuilder()
                        .add("id", report.getId())
                        .add("pet_id", report.getPet_id())
                        .add("lat", report.getLat())
                        .add("lng", report.getLng())
                        .add("date_time", report.getDate_time())
                        .add("zone", report.getZone())
                        .add("description", report.getDescription())
                        .add("closed", report.getClosed());
                        jsonArrayBuilder.add(reportBuilder);
                    }
                });
                
                JsonArray reportArray = jsonArrayBuilder.build();
                jsonObjectBuilder.add("reports", reportArray);
            });
        }
        else {
            throw new AppException("Requested Reports do not exist", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(jsonObjectBuilder.build().toString());
    }

    @GetMapping("/count/reports")
    public ResponseEntity<String> getTotalPages() {
        JsonObject jo = Json.createObjectBuilder()
        .add("pages", petService.getTotalPages())
        .build();
        return ResponseEntity.ok(jo.toString());
    }

    @PostMapping("/features")
    public ResponseEntity<String> postFeatures(@RequestBody FeaturesDto featuresDto) {
        petService.postFeatures(featuresDto);
        JsonObject jo = Json.createObjectBuilder()
                        .add("message", "Feature added")
                        .build();
        return ResponseEntity.ok(jo.toString());
    }

    @PostMapping("/add")
    public ResponseEntity<String> postNewPet(@RequestBody PetDto petDto) {
        JsonObject jo = petService.postNewPet(petDto);
        if (jo == null) {
            throw new AppException("Pet could not be registered, please try again", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(jo.toString());
    }

    @PostMapping("/reports")
    public ResponseEntity<String> postReport(@RequestBody ReportDto reportDto) {
        petService.postReport(reportDto);
        JsonObject jo = Json.createObjectBuilder()
                        .add("message", "Report created")
                        .build();
        return ResponseEntity.ok(jo.toString());
    }

    @PutMapping("/reports")
    public ResponseEntity<String> putReportClosed(@RequestParam String reportId, @RequestParam String petId) {
        petService.putReportClosed(reportId, petId);
        JsonObject jo = Json.createObjectBuilder()
                        .add("message", "Report closed successfully")
                        .build();
        return ResponseEntity.ok(jo.toString());
    }

    @PutMapping(path="/lost/{petId}", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> putPetLostValue(@PathVariable String petId, @RequestBody Map<String, Object> requestBody) {
        String lostValue = requestBody.get("lostValue").toString();
        petService.putPetLostValue(petId, lostValue);
        JsonObject jo = Json.createObjectBuilder()
                        .add("message", "Lost value amended to " + lostValue)
                        .build();
        return ResponseEntity.ok(jo.toString());
    }


}