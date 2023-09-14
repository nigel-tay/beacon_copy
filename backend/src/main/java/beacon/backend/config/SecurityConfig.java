package beacon.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserAuthProvider userAuthProvider;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new JwtAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(HttpMethod.POST, "/api/login", "/api/register", "/api/upload").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/pets/**", "/api/pets/count/reports").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/pets/features", "/api/pets/add", "/api/sightings").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/pets/lost/**", "/api/users/edit", "/api/sightings/**", "/api/pets/reports**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/sightings**").authenticated()
                .anyRequest().authenticated());
        return http.build();
    }
}