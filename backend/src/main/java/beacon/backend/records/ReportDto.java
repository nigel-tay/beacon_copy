package beacon.backend.records;

public record ReportDto(
    String id,
    String petId,
    String lat,
    String lng,
    String dateTime,
    String zone,
    String description,
    Integer closed
) {}
