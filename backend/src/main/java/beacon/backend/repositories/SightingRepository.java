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
import beacon.backend.models.Sighting;
import beacon.backend.records.SightingDto;

@Repository
public class SightingRepository {

    private String SQL_GET_TOTAL_COUNT = "SELECT COUNT(*) FROM sighting;";
    private String SQL_INSERT_SIGHTING = """
        INSERT INTO sighting (id, user_id, report_id, content, date_time, image, deleted)
        VALUES (?,?,?,?,?,?,?);
    """;
    private String SQL_SELECT_SIGHTINGS_WITH_PAGINATION = """
        SELECT * FROM sighting WHERE report_id = ? LIMIT ? OFFSET ?;
    """;
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void postSighting(SightingDto sightingDto) {
        int result = jdbcTemplate.update(
            SQL_INSERT_SIGHTING,
            sightingDto.id(),
            sightingDto.user_id(),
            sightingDto.report_id(),
            sightingDto.content(),
            sightingDto.date_time(),
            sightingDto.image(),
            sightingDto.deleted());
        if (result < 1) {
            throw new AppException("Sighting could not be lodged", HttpStatus.BAD_REQUEST);
        }
    }

    public Optional<List<Sighting>> getAllSightingsWithPagination(int offset, int pageSize, String reportId) {
        List<Sighting> returnedList = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_SIGHTINGS_WITH_PAGINATION, reportId, pageSize, offset);
        while(rs.next()) {
            Sighting s = new Sighting(
                rs.getString("id"),
                rs.getString("user_id"),
                rs.getString("report_id"),
                rs.getString("content"),
                rs.getString("date_time"),
                rs.getString("image"),
                rs.getInt("deleted"));
            returnedList.add(s);
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
}
