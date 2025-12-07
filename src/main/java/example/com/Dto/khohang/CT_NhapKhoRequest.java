package example.com.Dto.khohang;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CT_NhapKhoRequest {
    private String maSP;
    private Integer soLuong;
    private BigDecimal donGia;
    
}

