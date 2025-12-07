package example.com.Config;

import example.com.Service.auth.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // 1. Tắt CSRF vì sử dụng JWT (stateless)
            .csrf(csrf -> csrf.disable())
            
            // 2. Kích hoạt CORS và sử dụng CorsConfigurationSource Bean bên dưới
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 3. Cấu hình Session là STATELESS (quan trọng cho JWT)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 4. Cấu hình ủy quyền (Authorization)
            .authorizeHttpRequests(auth -> auth
                // CỰC KỲ QUAN TRỌNG: Cho phép Pre-flight OPTIONS qua mọi đường dẫn
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                
                // Cho phép công khai các API Auth (Login/Refresh)
                .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()
                
                // Các request khác yêu cầu xác thực
                .anyRequest().authenticated());

        // 5. Thêm JWT Filter trước bộ lọc xác thực Username/Password tiêu chuẩn
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    /**
     * Cấu hình chi tiết cho CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 🔑 Cần liệt kê chính xác origin frontend của bạn
        config.setAllowedOrigins(Arrays.asList(
            "https://quanlycuahang-frontend.vercel.app", // Domain Vercel của bạn
            "http://localhost:3000" // Để phát triển cục bộ
        ));
        
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 🔑 SỬ DỤNG "*" CHO HEADERS để đảm bảo tất cả các header (Authorization, Content-Type, v.v.) được chấp nhận
        config.setAllowedHeaders(List.of("*"));
        
        // Cho phép gửi cookie hoặc Authorization header
        config.setAllowCredentials(true); 
        
        // Đặt thời gian tồn tại của Pre-flight request (trong giây)
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config); // Áp dụng cấu hình cho tất cả các đường dẫn
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
