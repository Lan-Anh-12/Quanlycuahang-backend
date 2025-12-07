// NhanVienWithTaiKhoanRequest.java
package example.com.Dto.nhanvien;

import java.time.LocalDate;
import lombok.Data;

@Data
public class NhanVienWithTaiKhoanRequest {
    private String tenNV;
    private String sDT;
    private String email;
    private LocalDate ngayVaoLam;
    private TaiKhoanRequest taiKhoan; // chứa username, password, role
}
