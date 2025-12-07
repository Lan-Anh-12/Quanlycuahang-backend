package example.com.Service.auth;

import example.com.model.TaiKhoan;
import example.com.model.NhanVien;
import example.com.Repository.TaiKhoanRepository;
import example.com.Repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepo;

    @Autowired
    private NhanVienRepository nhanVienRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String DangNhap(String username, String matkhau) {
        TaiKhoan tk = taiKhoanRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username không tồn tại"));
        if (!matkhau.equals(tk.getMatKhau())) {
            throw new RuntimeException("Mật khẩu sai");
        }
        // Token chỉ chứa username
        return jwtUtil.generateToken(tk.getUsername());
    }

    @Override
    public String refeshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Token không hợp lệ");
        }
        String username = jwtUtil.extractUsername(refreshToken);
        // Token mới vẫn chỉ chứa username
        return jwtUtil.generateToken(username);
    }

    @Override
    public void DangXuat(String token) {
        // Không cần xử lý
    }

    @Override
    public void doiMatKhau(String maTK, String mkCu, String mkMoi) {
        TaiKhoan tk = taiKhoanRepo.findById(maTK)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        if (!mkCu.equals(tk.getMatKhau())) {
            throw new RuntimeException("Mật khẩu cũ sai");
        }
        tk.setMatKhau(mkMoi);
        taiKhoanRepo.save(tk);
    }
    public String getMaNVFromUsername(String username) {
        TaiKhoan tk = taiKhoanRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
        NhanVien nv = nhanVienRepo.findByMaTK(tk.getMaTK())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        return nv.getMaNV();
    }
    public String getMaTKFromUsername(String username) {
        TaiKhoan tk = taiKhoanRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
        return tk.getMaTK();
    }
}
