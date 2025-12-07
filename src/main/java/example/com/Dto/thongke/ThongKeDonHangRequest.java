package example.com.Dto.thongke;

import java.math.BigDecimal;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ThongKeDonHangRequest {
    private int soLuongKhach;
    private int soLuongDon;
    private BigDecimal tongDoanhThu;
}
