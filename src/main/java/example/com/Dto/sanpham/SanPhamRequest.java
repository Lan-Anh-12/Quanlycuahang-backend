package example.com.Dto.sanpham;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SanPhamRequest {
    private String tenSP;
    private String phanLoai;
    private BigDecimal giaBan;  
    private String moTa;  
    private Integer soLuong;
    private String url;
}
