package beacon.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import beacon.backend.models.User;
import beacon.backend.services.UserService;
import jakarta.json.JsonObject;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<String> getUserById(@PathVariable String id) {
        JsonObject userJsonObject = userService.getUserById(id);
        return ResponseEntity.ok(userJsonObject.toString());
    }

    @PutMapping(path="/edit", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> putUserById(@RequestBody User user) {
        JsonObject userJsonObject = userService.putUserById(user);
        return ResponseEntity.ok(userJsonObject.toString());
    }
}
