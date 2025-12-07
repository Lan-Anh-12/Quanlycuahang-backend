package example.com.Dto.thongke;
import java.util.List;

import lombok.Data;

import example.com.model.CT_DonHang;
import example.com.model.DonHang;

@Data
public class TaoDonHangRequest {
    private DonHang donHang;
    private List<CT_DonHang> chiTietDonHangs;
    
   
    
}
