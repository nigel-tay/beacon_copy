package beacon.backend.repositories;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;


@Repository
public class ImageRepository {

    @Value("${cloudinary.cloud.name}")
    private String CLOUD_NAME;

    @Value("${cloudinary.api.key}")
    private String API_KEY;

    @Value("${cloudinary.api.secret}")
    private String API_SECRET;

    @Autowired
    Cloudinary cloudinary;

    public String uploadToCloudinary(MultipartFile file) throws IOException {
        String urlString = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url").toString();
        String[] urlStringArray = urlString.split("/upload/");
        String firstHalf = urlStringArray[0];
        String secondHalf = urlStringArray[1].split("/")[1];
        return firstHalf + "/upload/c_thumb,g_face,z_0.8,h_400,w_400/" + secondHalf;
    }
}