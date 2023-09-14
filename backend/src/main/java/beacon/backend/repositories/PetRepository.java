package beacon.backend.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import beacon.backend.exceptions.AppException;
import beacon.backend.models.Features;
import beacon.backend.models.Pet;
import beacon.backend.models.Report;
import beacon.backend.models.Sighting;
import beacon.backend.records.PetDto;
import beacon.backend.records.ReportDto;

@Repository
public class PetRepository {

    private String SQL_GET_FEATURES = "SELECT * FROM features;";
    private String SQL_GET_FEATURES_BY_ID = """
        SELECT f.id AS id, f.feature AS feature
        FROM pet p
        INNER JOIN pet_features pf ON p.id = pf.pet_id
        INNER JOIN features f ON pf.features_id = f.id
        WHERE p.id = ?;        
    """;
    private String SQL_GET_FEATURES_BY_COLUMN = "SELECT * FROM features WHERE feature = ?;";
    private String SQL_GET_PETS_BY_USER_ID = "SELECT * FROM pet WHERE owner_id = ?;";
    private String SQL_GET_PET_BY_ID = "SELECT * FROM pet WHERE id = ?;";
    private String SQL_GET_OPEN_REPORT_BY_ID = """
        SELECT * FROM report
        WHERE pet_id = ?
        AND closed = ?;
    """;
    private String SQL_SELECT_REPORTS_BY_REGION="""
        SELECT * FROM report
        WHERE zone = ?
        AND closed = 0
        LIMIT ? OFFSET ?;        
    """;

    private String SQL_SELECT_ALL_REPORTS="""
        SELECT * FROM report WHERE closed = 0 LIMIT ? OFFSET ?;
    """;
    private String SQL_GET_TOTAL_COUNT = "SELECT COUNT(*) FROM report WHERE closed = 0;";

    private String SQL_INSERT_FEATURES = """
        INSERT INTO features (id, feature)
        VALUES (?,?);
    """;
    private String SQL_INSERT_PET_FEATURES = """
        INSERT INTO pet_features (id, pet_id, features_id)
        VALUES (?,?,?);
    """;
    private String SQL_INSERT_PET = """
        INSERT INTO pet (id, owner_id, name, type, image, lost)
        VALUES (?,?,?,?,?,?);
    """;
    private String SQL_INSERT_REPORT = """
        INSERT INTO report (id, pet_id, lat, lng, date_time, zone, description, closed)
        VALUES (?,?,?,?,?,?,?,?);
    """;
    private String SQL_UPDATE_PET_LOST = """
        UPDATE pet 
        SET lost = ?
        WHERE id = ?;        
    """;
    private String SQL_UPDATE_REPORT_CLOSED = """
        UPDATE report 
        SET closed = ?
        WHERE id = ?;        
    """;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<List<Features>> getAllFeatures() {
        List<Features> featureList = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_FEATURES);
        
        while (rs.next()) {
            Features pf = new Features();
            pf.setId(rs.getString("id"));
            pf.setFeature(rs.getString("feature"));
            featureList.add(pf);
        }
    
        if (featureList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(featureList);
        }
    }

    public Optional<List<Features>> getAllFeaturesById(String petId) {
        List<Features> featureList = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_FEATURES_BY_ID, petId);
        
        while (rs.next()) {
            Features pf = new Features();
            pf.setId(rs.getString("id"));
            pf.setFeature(rs.getString("feature"));
            featureList.add(pf);
        }
    
        if (featureList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(featureList);
        }
    }

    public Optional<List<Pet>> getPetsByUserId(String userId) {
        List<Pet> petList = new ArrayList<>();

        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_PETS_BY_USER_ID, userId);
        
        while (rs.next()) {
            Pet pet = new Pet();
            pet.setId(rs.getString("id"));
            pet.setOwner_id(rs.getString("owner_id"));
            pet.setName(rs.getString("name"));
            pet.setType(rs.getString("type"));
            pet.setImage(rs.getString("image"));
            pet.setLost(rs.getInt("lost"));
            petList.add(pet);
        }
        
        if (petList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(petList);
        }
    }

    public Optional<Pet> getPetById(String petId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_PET_BY_ID, petId);
        if (rs.next()) {
            Pet pet = new Pet(
                rs.getString("id"),
                rs.getString("owner_id"),
                rs.getString("name"),
                rs.getString("type"),
                rs.getString("image"),
                rs.getInt("lost")
                );
            return Optional.of(pet);
        }
        return Optional.empty();
    }

    public Optional<Report> getOpenReportByPetId(String petId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_OPEN_REPORT_BY_ID, petId, 0);
        if (rs.next()) {
            Report r = new Report(
                rs.getString("id"),
                rs.getString("pet_id"),
                rs.getString("lat"),
                rs.getString("lng"),
                rs.getString("date_time"),
                rs.getString("zone"),
                rs.getString("description"),
                rs.getString("closed")
                );
            return Optional.of(r);
        }
        return Optional.empty();
    }

    public Optional<List<Report>> getReportsByRegion(int offset, int pageSize, String region) {
        List<Report> returnedList = new ArrayList<>();
        if (region.isEmpty()) {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_ALL_REPORTS, pageSize, offset);
            while(rs.next()) {
            Report r = new Report(
                rs.getString("id"),               
                rs.getString("pet_id"),               
                rs.getString("lat"),               
                rs.getString("lng"),               
                rs.getString("date_time"),               
                rs.getString("zone"),               
                rs.getString("description"),               
                rs.getString("closed"));
                returnedList.add(r);
            }
        }
        else {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_REPORTS_BY_REGION, region, pageSize, offset);
            while(rs.next()) {
            Report r = new Report(
                rs.getString("id"),               
                rs.getString("pet_id"),               
                rs.getString("lat"),               
                rs.getString("lng"),               
                rs.getString("date_time"),               
                rs.getString("zone"),               
                rs.getString("description"),               
                rs.getString("closed"));
            returnedList.add(r);
            }
        }
        if (returnedList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(returnedList);
        }
    }

    public Integer getTotalCount() {
        return jdbcTemplate.queryForObject(SQL_GET_TOTAL_COUNT, Integer.class);
    }

    public String postFeatures(String feature, String featuresUuid) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_FEATURES_BY_COLUMN, feature);
        if (!rs.next()) {
            jdbcTemplate.update(SQL_INSERT_FEATURES, featuresUuid, feature);
            return featuresUuid;
        }
        else {
            return rs.getString("id");
        }
    }

    public void insertPetFeature(String petFeaturesUuid, String petId, String featuresId) {
        int result = jdbcTemplate.update(SQL_INSERT_PET_FEATURES, petFeaturesUuid, petId, featuresId);
        if (result < 1) {
            throw new AppException("Could not create pet feature", HttpStatus.BAD_REQUEST);
        }
    }

    public void insertNewPet(PetDto petDto) {
        int result = jdbcTemplate.update(
            SQL_INSERT_PET,
            petDto.id(),
            petDto.ownerId(),
            petDto.name(),
            petDto.type(),
            petDto.image(),
            petDto.lost());
        if (result < 1) {
            throw new AppException("Pet could not be registered", HttpStatus.BAD_REQUEST);
        }
    }


    public void insertNewReport(ReportDto reportDto) {
        if (reportDto.lat() == null) {
            throw new AppException("Address format incorrect, please select an address from the dropdown", HttpStatus.BAD_REQUEST);
        }
        int result = jdbcTemplate.update(
                    SQL_INSERT_REPORT,
                    reportDto.id(),
                    reportDto.petId(),
                    reportDto.lat(),
                    reportDto.lng(),
                    reportDto.dateTime(),
                    reportDto.zone(),
                    reportDto.description(),
                    reportDto.closed());
        if (result < 1) {
            throw new AppException("There was an issue lodging your report, try again later", HttpStatus.BAD_REQUEST);
        }
    }

    public void putPetLostValue(String petId, String lostValue) {
        int result = jdbcTemplate.update(SQL_UPDATE_PET_LOST, Integer.parseInt(lostValue), petId);

        if (result < 1) {
            throw new AppException("Lost could not be updated", HttpStatus.BAD_REQUEST);
        }
    }

    public void putReportClosed(String reportId) {
        int result = jdbcTemplate.update(SQL_UPDATE_REPORT_CLOSED, "1", reportId);

        if (result < 1) {
            throw new AppException("Report could not be marked as closed", HttpStatus.BAD_REQUEST);
        }
    }
}
