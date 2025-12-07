package example.com.Controller;

import example.com.Dto.auth.LoginRequest;
import example.com.Dto.auth.ChangePasswordRequest;
import example.com.Dto.auth.RefreshRequest;
import example.com.Service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String jwtToken = authService.DangNhap(request.getUsername(), request.getMatkhau());
            return ResponseEntity.ok(jwtToken);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request) {
        try {
            String newToken = authService.refeshToken(request.getRefreshToken());
            return ResponseEntity.ok(newToken);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        authService.doiMatKhau(request.getMaTK(), request.getMkCu(), request.getMkMoi());
        return ResponseEntity.ok("Đổi mật khẩu thành công!");
    }

    @GetMapping("/me/manv")
    public String getMaNVFromToken(HttpServletRequest request) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (username == null) throw new RuntimeException("Chưa login");
        return authService.getMaNVFromUsername(username);
    }
    @GetMapping("/me/matk")
    public String getMaTKFromToken(HttpServletRequest request) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (username == null) throw new RuntimeException("Chưa login");
        return authService.getMaTKFromUsername(username);
    }
}
