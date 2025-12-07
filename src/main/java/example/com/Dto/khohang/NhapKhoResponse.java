package example.com.Dto.khohang;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class NhapKhoResponse {
    private String maNK;
    private LocalDateTime ngayNhap;
    private String maNV;
    private String nhaCungCap;
    private BigDecimal tongTien;
    private List<CT_NhapKhoResponse> chiTiet;
}
