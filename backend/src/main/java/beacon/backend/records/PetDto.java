package beacon.backend.records;

public record PetDto(
    String id,
    String ownerId,
    String name,
    String type,
    String image,
    Integer lost
) {}
