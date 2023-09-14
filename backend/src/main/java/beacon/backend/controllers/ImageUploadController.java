package beacon.backend.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import beacon.backend.services.UserService;

@Controller
@RequestMapping("/api")
public class ImageUploadController {

    @Autowired
    private UserService userService;

    @PostMapping(path="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, String>> uploadImage(@RequestPart MultipartFile imageFile) throws IOException {
        String url = userService.uploadImage(imageFile);

        Map<String, String> response = new HashMap<>();
        response.put("image", url);
        return ResponseEntity.ok(response);
    }
}
