package example.com.Dto.khohang;

import java.util.List;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NhapKhoRequest {
    private String maNV;
    private String nhaCungCap;
    private LocalDateTime ngayNhap;
    private List<CT_NhapKhoRequest> chiTiet;
}

