package beacon.backend.records;

public record SightingDto(String id, String user_id, String report_id, String content, String date_time, String image, Integer deleted) {}
