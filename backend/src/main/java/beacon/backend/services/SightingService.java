package beacon.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beacon.backend.models.Sighting;
import beacon.backend.records.SightingDto;
import beacon.backend.repositories.SightingRepository;

@Service
public class SightingService {

    @Autowired
    SightingRepository sightingRepository;
    
    public void postSighting(SightingDto sightingDto) {
        sightingRepository.postSighting(sightingDto);
    }

    public Optional<List<Sighting>> getAllSightings(int page, int pageSize, String reportId) {
        int offset = (page - 1) * pageSize;
        return sightingRepository.getAllSightingsWithPagination(offset, pageSize, reportId);
    }

    public Integer getTotalPages() {
        int totalCount = sightingRepository.getTotalCount();
        return (int) Math.ceil((double) totalCount / 3);
    }
}
