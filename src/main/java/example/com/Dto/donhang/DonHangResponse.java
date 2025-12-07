package example.com.Dto.donhang;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;


// Dùng cho danh sách đơn hàng

@Data
public class DonHangResponse {
    private String maDH;
    private String maKH;
    private String maNV;
    private LocalDateTime ngayLap;
    private BigDecimal tongTien;

    private List<ChiTietDonHangResponse> chiTiet;

}


