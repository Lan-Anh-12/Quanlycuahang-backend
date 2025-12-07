// NhanVienResponse.java
package example.com.Dto.nhanvien;

import java.time.LocalDate;
import lombok.Data;

@Data
public class NhanVienResponse {
    private String maNV;
    private String tenNV;
    private String sDT;
    private String email;
    private LocalDate ngayVaoLam;
    private String maTK; // FK tài khoản
}
