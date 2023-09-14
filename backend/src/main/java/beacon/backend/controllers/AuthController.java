package beacon.backend.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import beacon.backend.config.UserAuthProvider;
import beacon.backend.models.User;
import beacon.backend.records.LoginDto;
import beacon.backend.records.SignUpDto;
import beacon.backend.records.UserDto;
import beacon.backend.services.UserService;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthProvider userAuthProvider;
    
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto) {
        UserDto user = userService.login(loginDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto) {
        User user = userService.register(signUpDto);
        UserDto createdUser = new UserDto();
        createdUser.setId(user.getId());
        createdUser.setUsername(user.getUsername());
        createdUser.setToken(userAuthProvider.createToken(new UserDto(user.getId(), user.getUsername(), "")));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }
}
