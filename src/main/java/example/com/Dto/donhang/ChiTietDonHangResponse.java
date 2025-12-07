package example.com.Dto.donhang;


import java.math.BigDecimal;
import lombok.Data;

@Data
public class ChiTietDonHangResponse {
    private String maCTDH;
    private String maSP;
    private Integer soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private BigDecimal giaVon; // Thêm trường này để tính lợi nhuận
}
