// NhanVienRequest.java
package example.com.Dto.nhanvien;

import java.time.LocalDate;
import lombok.Data;

@Data
public class NhanVienRequest {
    private String tenNV;
    private String sDT;
    private String email;
    private LocalDate ngayVaoLam;
    // Không chứa thông tin tài khoản
}
