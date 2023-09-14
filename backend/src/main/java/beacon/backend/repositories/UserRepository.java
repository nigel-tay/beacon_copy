package beacon.backend.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import beacon.backend.exceptions.AppException;
import beacon.backend.models.User;

@Repository
public class UserRepository {

    private String SQL_FIND_BY_USERNAME = "SELECT * FROM beacon_user WHERE username = ?;";
    private String SQL_FIND_BY_ID = "SELECT * FROM beacon_user WHERE id = ?;";
    private String SQL_INSERT_NEW_USER = """
                INSERT INTO beacon_user (id, username, email, password, address, lat, lng, image)
                VALUES (?,?,?,?,?,?,?,?);
            """;
    private String SQL_UPDATE_USER_ADDRESS = """
                UPDATE beacon_user 
                SET address = ?, lat = ?, lng = ?
                WHERE id = ?; 
    """;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public Optional<User> findUserByUsername(String username) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_FIND_BY_USERNAME, username);
        if (rs.next()) {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setAddress(rs.getString("address"));
            user.setLat(rs.getString("lat"));
            user.setLng(rs.getString("lng"));
            user.setImage(rs.getString("image"));
            
            return Optional.of(user);
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<User> findUserById(String id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_FIND_BY_ID, id);
        if (rs.next()) {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setAddress(rs.getString("address"));
            user.setLat(rs.getString("lat"));
            user.setLng(rs.getString("lng"));
            user.setImage(rs.getString("image"));
            
            return Optional.of(user);
        }
        else {
            return Optional.empty();
        }
    }

    public Integer insertNewUser(User user) {
        if (user.getAddress() == null) {
            throw new AppException("Address format incorrect, please select an address from the dropdown", HttpStatus.BAD_REQUEST);
        }
        return jdbcTemplate.update(SQL_INSERT_NEW_USER, 
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getAddress(),
                        user.getLat(),
                        user.getLng(),
                        user.getImage());
    }

    public User putUserById(User user) {
        Optional<User> optionalUser = findUserById(user.getId());
        if (!optionalUser.isEmpty()) {
            User returnedUser = optionalUser.get();
            jdbcTemplate.update(SQL_UPDATE_USER_ADDRESS, 
                            user.getAddress(),
                            user.getLat(),
                            user.getLng(),
                            user.getId());

            return returnedUser;
        }
        else {
            throw new AppException("Something went wrong updating address, please try again later", HttpStatus.BAD_REQUEST);
        }
    }
}
