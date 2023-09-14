package beacon.backend.records;

public record SignUpDto(String id, String address, String email, String username, char[] password, String lat, String lng, String image) {}