package example.com.Service.auth;

public interface AuthService {
    String DangNhap(String username, String matkhau);
    String refeshToken(String refeshToken);
    void DangXuat(String token);
    void doiMatKhau(String maTK, String mkCu, String mkMoi);
    String getMaNVFromUsername(String username);
    String getMaTKFromUsername(String username);

}
    