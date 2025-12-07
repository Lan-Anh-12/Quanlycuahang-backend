package example.com.Dto.khohang;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CT_NhapKhoResponse {
    private String maCTNK;
    private String maSP;
    private String tenSP;
    private Integer soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
}

