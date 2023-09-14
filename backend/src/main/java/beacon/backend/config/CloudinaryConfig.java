package beacon.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud.name}")
    private String CLOUD_NAME;

    @Value("${cloudinary.api.key}")
    private String API_KEY;

    @Value("${cloudinary.api.secret}")
    private String API_SECRET;
    
    @Bean
    public Cloudinary cloudinary(){
        
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name",CLOUD_NAME,
                            "api_key",API_KEY,
                            "api_secret",API_SECRET));
    }
}
