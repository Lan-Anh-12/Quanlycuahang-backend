package example.com.Dto.sanpham;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SanPhamResponse {
    private String maSP;
    private String tenSP;
    private Integer soLuongTon;
    private String moTa;
    private BigDecimal giaBan;
    private String phanLoai;
    private String url;
    
    public Integer getTonKho() {
        return soLuongTon;
    }
}
